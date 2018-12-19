package com.effective.common.osgi.felix;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;

public class Activator implements BundleActivator, ServiceListener {

    @Override
    public void start(BundleContext bundleContext) throws Exception {
        System.out.println("Starting to listen for service events.");
        bundleContext.addServiceListener(this);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {
        bundleContext.removeServiceListener(this);
        System.out.println("Stopped listening for service events.");
    }

    @Override
    public void serviceChanged(ServiceEvent event) {
        String[] objectClass = (String[]) event.getServiceReference().getProperty("objectClass");

        if (event.getType() == ServiceEvent.REGISTERED) {
            System.out.println("Ex1: Service of type " + objectClass[0] + " registered.");
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
            System.out.println("Ex1: Service of type " + objectClass[0] + " unregistered.");
        } else if (event.getType() == ServiceEvent.MODIFIED) {
            System.out.println("Ex1: Service of type " + objectClass[0] + " modified.");
        }
    }
}
