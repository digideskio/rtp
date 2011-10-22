package org.eclipse.rtp.core.internal;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class RTPCoreActivator implements BundleActivator {

  public static String BUNDLE_ID = "org.eclipse.rtp.coree";
  private static BundleContext bundleContext;

  @Override
  public void start( BundleContext context ) throws Exception {
    bundleContext = context;
  }

  @Override
  public void stop( BundleContext context ) throws Exception {
    bundleContext = null;
  }

  public static BundleContext getBundleContext() {
    return bundleContext;
  }
}
