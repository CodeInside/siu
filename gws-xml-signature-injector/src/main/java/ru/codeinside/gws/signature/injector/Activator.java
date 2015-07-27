package ru.codeinside.gws.signature.injector;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

  static BundleContext CONTEXT;

  @Override
  public void start(BundleContext bundleContext) throws Exception {
    CONTEXT = bundleContext;
  }

  @Override
  public void stop(BundleContext bundleContext) throws Exception {
    CONTEXT = null;
  }
}
