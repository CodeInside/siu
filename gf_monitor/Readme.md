Collect'd Glassfish Module
-------------------------

Usage
-----

The configuration of the plugin follows collectd's convention:

    <LoadPlugin python>
        Globals true
    </LoadPlugin>
    # ...
    <Plugin python>
        ModulePath "/path/to/your/python/modules"
        LogTraces true
        Import "gf"

        <Module "gf">
            Host   "xx"
            Domain "oep"      4848
            Domain "registry" 4949 'admin' 'secret'
        </Module>
    </Plugin>

Configuration options:

* *Host* `<hostName>` : Name of current host. Default by hostname.
* *Domain* `<domainName>` `<port>` [`<user>` `<password`]: Name of Glassfish domain to monitor, Admin Console port. Optionally: admin user and password

References
----------

* [collectd website](http://collectd.org/)
* [collectd's python plugin](http://collectd.org/wiki/index.php/Plugin:Python)
* [documentation of collectd's python plugin](http://collectd.org/documentation/manpages/collectd-python.5.shtml)
