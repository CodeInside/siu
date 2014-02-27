READ_METHOD = None
CONFIG_METHOD = None
METRICS = []
LOG = ('print')


def register_read(method):
    global READ_METHOD
    READ_METHOD = method


def register_config(method):
    global CONFIG_METHOD
    CONFIG_METHOD = method


def warning(msg):
    print "warning:", msg


def cfg_list(children):
    return ConfigList(children)


def cfg_value(key, value):
    return ConfigValue(key, value)


class ConfigValue:
    def __init__(self, key, values):
        self.key = key
        self.values = values


class ConfigList:
    def __init__(self, children):
        self.children = children


class Values:
    def __init__(self):
        self.host = None
        self.plugin = None
        self.plugin_instance = None
        self.values = []
        self.type = None
        self.type_instance = None
        self.time = None


    def dispatch(self):
        global METRICS
        if 'all' in LOG:
            METRICS.append(self)
        elif 'last' in LOG:
            METRICS = [self]
        if 'print' in LOG:
            print '\t{} {} {} = {}  {}/{}'.format(
                self.host,
                self.plugin,
                self.plugin_instance,
                self.type_instance,
                self.values[0],
                self.type,
                self.time
            )
