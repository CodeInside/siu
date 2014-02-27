import unittest, gf, collectd


class Test(unittest.TestCase):
    def test_direct(self):
        collectd.METRICS = []
        collectd.LOG = ('last', 'print')
        collectd.CONFIG_METHOD(collectd.cfg_list([
            collectd.cfg_value('Host', ['a']),
            collectd.cfg_value('Domain', ['x', '4848'])
        ]))
        collectd.READ_METHOD()
        self.assertTrue(len(collectd.METRICS) > 0)

    def _test_default(self):
        collectd.METRICS = []
        collectd.CONFIG_METHOD(collectd.cfg_list([]))
        collectd.READ_METHOD()
        self.assertTrue(len(collectd.METRICS) > 0)
