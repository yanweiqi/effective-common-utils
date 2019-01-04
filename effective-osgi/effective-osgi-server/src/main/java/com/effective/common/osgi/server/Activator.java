package com.effective.common.osgi.server;


import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {


    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("start");
    }

    public void stop(BundleContext bundleContext) throws Exception {
            System.out.println("stop");

    }
}
