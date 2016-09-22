package com.hazelcast.ibm.dynacache;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.hazelcast.client.osgi.HazelcastClientOSGiService;
import com.hazelcast.client.osgi.HazelcastOSGiClientInstance;

//import com.hazelcast.core.Hazelcast;




public class Activator implements BundleActivator {
	public static HazelcastOSGiClientInstance hazelcastOSGiInstance;
	public static HazelcastClientOSGiService hazelcastClientOSGiService;
	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	static{
//		System.setProperty("hazelcast.osgi.start", "true");
	}
	public void start(BundleContext context) throws Exception {
		
		System.out.println("Hello MARS");
		for(Bundle bundle: context.getBundles()){
			System.out.println(bundle.toString());
			if(bundle.getBundleId()==29){
				System.out.println("Starting Bundle");
//				System.setProperty("hazelcast.osgi.start", "true");
				bundle.start();
//			
			System.out.println("Started");
			}
			if(bundle!=null && bundle.getRegisteredServices()!=null)
			for(ServiceReference<?> service: bundle.getRegisteredServices()){
				System.out.println(service);
			}
		}
		System.out.println("hazelcast starting 1");
	//	Hazelcast.newHazelcastInstance();
		System.out.println("hazelcast starting 2");
		Thread.sleep(3000);
		ServiceReference serviceRef =
                context.getServiceReference(HazelcastClientOSGiService.class.getName());
		System.out.println("hazelcast starting 3");
        if (serviceRef == null) {
    		System.out.println("no registered osgi service");

//            throw new IllegalStateException("There is no registered `HazelcastOSGiService`!");
        }
        System.out.println("hazelcast starting 4");
		  
        hazelcastClientOSGiService = (HazelcastClientOSGiService) context.getService(serviceRef);
        System.out.println("hazelcast starting 5");
//        hazelcastOSGiInstance = hazelcastClientOSGiService.getDefaultHazelcastInstance();
        System.out.println("hazelcast starting 6");
        System.out.println("Default Hazelcast instance (available when `"
                + HazelcastClientOSGiService.HAZELCAST_OSGI_START + "` flag is enabled): " + hazelcastOSGiInstance);
        System.out.println("hazelcast starting 7");
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
