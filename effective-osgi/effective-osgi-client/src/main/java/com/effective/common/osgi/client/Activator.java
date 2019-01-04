package com.effective.common.osgi.client;

import com.effective.common.osgi.server.Hello;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class Activator implements BundleActivator {

    public void start(BundleContext ctx) throws Exception {
        System.out.println("----------------hello client start---------------------");
        ServiceReference ref = ctx.getServiceReference(Hello.class.getName());
        if (ref != null) {
            Hello hello = null;
            try {
                hello = (Hello) ctx.getService(ref);
                if (hello != null)
                    hello.sayHello();
                else
                    System.out.println("Service:Hello---object null");
            } catch (RuntimeException e) {
                e.printStackTrace();
            } finally {
                ctx.ungetService(ref);
                hello = null;
            }
        } else {
            System.out.println("Service:Hello---not exists");
        }
        System.out.println("----------------hello client start---------------------");
    }

    public void stop(BundleContext bundleContext) throws Exception {

    }
}
