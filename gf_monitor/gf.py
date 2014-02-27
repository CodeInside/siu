import collectd
import urllib2
import json
import thread
import socket
import httplib
import base64

host = None
domains = {}


class ConnectionManager:
    def __init__(self):
        self._lock = thread.allocate_lock()
        self._hostmap = {} # map hosts to a list of connections
        self._connmap = {} # map connections to host
        self._readymap = {} # map connection to ready state

    def add(self, host, connection, ready):
        self._lock.acquire()
        try:
            if not self._hostmap.has_key(host): self._hostmap[host] = []
            self._hostmap[host].append(connection)
            self._connmap[connection] = host
            self._readymap[connection] = ready
        finally:
            self._lock.release()

    def remove(self, connection):
        self._lock.acquire()
        try:
            try:
                host = self._connmap[connection]
            except KeyError:
                pass
            else:
                del self._connmap[connection]
                del self._readymap[connection]
                self._hostmap[host].remove(connection)
                if not self._hostmap[host]: del self._hostmap[host]
        finally:
            self._lock.release()

    def set_ready(self, connection, ready):
        try:
            self._readymap[connection] = ready
        except KeyError:
            pass

    def get_ready_conn(self, host):
        conn = None
        self._lock.acquire()
        try:
            if self._hostmap.has_key(host):
                for c in self._hostmap[host]:
                    if self._readymap[c]:
                        self._readymap[c] = 0
                        conn = c
                        break
        finally:
            self._lock.release()
        return conn

    def get_all(self, host=None):
        if host:
            return list(self._hostmap.get(host, []))
        else:
            return dict(self._hostmap)


class KeepAliveHandler:
    def __init__(self):
        self._cm = ConnectionManager()

    def open_connections(self):
        return [(host, len(li)) for (host, li) in self._cm.get_all().items()]

    def close_connection(self, host):
        for h in self._cm.get_all(host):
            self._cm.remove(h)
            h.close()

    def close_all(self):
        for host, conns in self._cm.get_all().items():
            for h in conns:
                self._cm.remove(h)
                h.close()

    def _request_closed(self, request, host, connection):
        self._cm.set_ready(connection, 1)

    def _remove_connection(self, host, connection, close=0):
        if close: connection.close()
        self._cm.remove(connection)

    def do_open(self, req):
        host = req.get_host()
        if not host:
            raise urllib2.URLError('no host given')

        try:
            h = self._cm.get_ready_conn(host)
            while h:
                r = self._reuse_connection(h, req, host)
                if r: break
                h.close()
                self._cm.remove(h)
                h = self._cm.get_ready_conn(host)
            else:
                h = self._get_connection(host)
                self._cm.add(host, h, 0)
                self._start_transaction(h, req)
                r = h.getresponse()
        except (socket.error), err:
            raise urllib2.URLError(err)

        # if not a persistent connection, don't try to reuse it
        if r.will_close:
            self._cm.remove(h)

        r._handler = self
        r._host = host
        r._url = req.get_full_url()
        r._connection = h
        r.code = r.status
        r.headers = r.msg
        r.msg = r.reason

        if r.status == 200:
            return r
        else:
            return self.parent.error('http', req, r, r.status, r.msg, r.headers)

    def _reuse_connection(self, h, req, host):
        try:
            self._start_transaction(h, req)
            r = h.getresponse()
        except (socket.error):
            r = None
        except:
            self._cm.remove(h)
            h.close()
            raise

        if r is None or r.version == 9:
            r = None

        return r

    def _start_transaction(self, h, req):
        try:
            if req.has_data():
                data = req.get_data()
                h.putrequest('POST', req.get_selector())
                if not req.headers.has_key('Content-type'):
                    h.putheader('Content-type', 'application/x-www-form-urlencoded')
                if not req.headers.has_key('Content-length'):
                    h.putheader('Content-length', '%d' % len(data))
            else:
                h.putrequest('GET', req.get_selector())
        except (socket.error), err:
            raise urllib2.URLError(err)

        for args in self.parent.addheaders:
            h.putheader(*args)
        for k, v in req.headers.items():
            h.putheader(k, v)
        h.endheaders()
        if req.has_data():
            h.send(data)

    def _get_connection(self, host):
        return NotImplementedError


class HTTPResponse(httplib.HTTPResponse):
    def __init__(self, sock, debuglevel=0, strict=0, method=None):
        if method:
            httplib.HTTPResponse.__init__(self, sock, debuglevel, method)
        else:
            httplib.HTTPResponse.__init__(self, sock, debuglevel)
        self.fileno = sock.fileno
        self.code = None
        self._rbuf = ''
        self._rbufsize = 8096
        self._handler = None # inserted by the handler later
        self._host = None    # (same)
        self._url = None     # (same)
        self._connection = None # (same)

    _raw_read = httplib.HTTPResponse.read

    def close(self):
        if self.fp:
            self.fp.close()
            self.fp = None
            if self._handler:
                self._handler._request_closed(self, self._host,
                                              self._connection)

    def close_connection(self):
        self._handler._remove_connection(self._host, self._connection, close=1)
        self.close()

    def info(self):
        return self.headers

    def geturl(self):
        return self._url

    def read(self, amt=None):
        if self._rbuf and not amt is None:
            L = len(self._rbuf)
            if amt > L:
                amt -= L
            else:
                s = self._rbuf[:amt]
                self._rbuf = self._rbuf[amt:]
                return s

        s = self._rbuf + self._raw_read(amt)
        self._rbuf = ''
        return s

    def readline(self, limit=-1):
        data = ""
        i = self._rbuf.find('\n')
        while i < 0 and not (0 < limit <= len(self._rbuf)):
            new = self._raw_read(self._rbufsize)
            if not new: break
            i = new.find('\n')
            if i >= 0: i = i + len(self._rbuf)
            self._rbuf = self._rbuf + new
        if i < 0:
            i = len(self._rbuf)
        else:
            i = i + 1
        if 0 <= limit < len(self._rbuf): i = limit
        data, self._rbuf = self._rbuf[:i], self._rbuf[i:]
        return data

    def readlines(self, sizehint=0):
        total = 0
        list = []
        while 1:
            line = self.readline()
            if not line: break
            list.append(line)
            total += len(line)
            if sizehint and total >= sizehint:
                break
        return list


class HTTPConnection(httplib.HTTPConnection):
    response_class = HTTPResponse


class HTTPHandler(KeepAliveHandler, urllib2.HTTPHandler):
    def __init__(self):
        KeepAliveHandler.__init__(self)

    def http_open(self, req):
        return self.do_open(req)

    def _get_connection(self, host):
        return HTTPConnection(host)


class Fetcher:
    def __init__(self, user, password):
        self.headers = {
            'Connection': 'keep-alive',
            'Accept': 'application/json'
        }
        if user:
            raw = "%s:%s" % (user, password)
            self.headers['Authorization'] = 'Basic %s' % base64.b64encode(raw).strip()
        self.opener = urllib2.build_opener(HTTPHandler())

    def close(self):
        self.opener.close()

    def fetch(self, url):
        try:
            resp = self.opener.open(urllib2.Request(url, None, self.headers))
            return json.loads(resp.read())
        except urllib2.HTTPError as e:
            return {'exit_code': 'FAILURE', 'message': e.msg}
        except urllib2.URLError as e:
            return {'exit_code': 'FAILURE', 'message': e.reason}


def fetch_and_push_all():
    global host
    global domains
    for domain in domains:
        (port, user, password) = domains[domain]
        url = 'http://localhost:{0}/monitoring/domain'.format(port)
        fetch_all(domain, host, url, user, password)


def fetch_level(fetcher, url):
    data = fetcher.fetch(url)
    if data['exit_code'] == 'SUCCESS':
        children = {}
        entities = {}
        extra = data['extraProperties']
        for k, v in extra['childResources'].items():
            children[k] = v
        for k, v in extra['entity'].items():
            entities[k] = v
        return {'children': children, 'entities': entities}
    collectd.warning("fetch '{0}': '{1}'".format(url, data['message']))
    return None


replacement = {
    'server': 'srv',
    'applications': 'apps',
    'network': 'net',
    'resources': 'res',
    'request': 'req',
    'thread-system': 'threads',
    'connection-queue': 'queue',
    'http-service': 'http',
    '__admingui': 'adm',
    '__asadmin': 'as',
    '__default-web-module': 'def',
    'class-loading-system': 'classes'
}


def simplify(it):
    global replacement
    r = []
    for item in it:
        if item in replacement:
            v = replacement[item]
            if v:
                r.append(v)
        else:
            r.append(item.replace('.', '_'))
    return r


def push_metric(parent, name, count, unit, last_sample_time):
    val = collectd.Values()
    val.host = '.'.join(simplify(parent[0:4]))
    val.plugin = '.'.join(simplify(parent[4:]))
    val.time = int(last_sample_time / 1000)
    val.type_instance = name
    val.values = [count]
    val.type = 'gauge'
    if unit == 'count':
        val.type = 'derive'
    elif unit == 'millisecond' or 'Milliseconds':
        val.type_instance += "Millis"
    elif unit == 'nanosecond':
        val.type_instance += "Nanos"
    elif unit == 'seconds' or unit == 'byte(s)' or unit == 'bytes':
        pass
    else:
        collectd.warning("invalid unit {0} for {1}".format(unit, val.type_instance))
    val.dispatch()


def fetch_all(domain, top, url, user, password):
    fetcher = Fetcher(user, password)
    try:
        queue = [{domain: ([top], url)}]
        while queue:
            item = queue.pop()
            for path_name, (path_parent, path_url) in item.items():
                data = fetch_level(fetcher, path_url)
                if data:
                    parent = path_parent[:]
                    # optionally:  path_name.replace('.', '_')
                    parent.append(path_name)
                    for child_name, child_url in data['children'].items():
                        queue.append({child_name: (parent, child_url)})
                    entities = data['entities']
                    if entities:
                        for metric, info in entities.items():
                            if 'lastsampletime' in info:
                                last_sample_time = info['lastsampletime']
                                unit = info['unit']
                                if last_sample_time > 0 and unit != 'String' and unit != 'List':
                                    name = info['name']
                                    if 'count' in info:
                                        push_metric(parent, name, info['count'], unit, last_sample_time)
                                    else:
                                        push_metric(parent, name + "Lo", info['lowwatermark'], unit, last_sample_time)
                                        push_metric(parent, name, info['current'], unit, last_sample_time)
                                        push_metric(parent, name + "Hi", info['highwatermark'], unit, last_sample_time)
    finally:
        fetcher.close()


def configure(config):
    global host
    global domains
    host = socket.gethostname()
    domains = {}
    for c in config.children:
        if c.key == "Domain":
            domain = c.values[0]
            port = int(c.values[1])
            if len(c.values) > 2:
                user = c.values[2]
                password = c.values[3]
            else:
                user = None
                password = None
            domains[domain] = (port, user, password)
        elif c.key == 'Host':
            host = c.values[0]


collectd.register_config(configure)
collectd.register_read(fetch_and_push_all)
