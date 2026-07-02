package payload;

import java.io.Serializable;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

public class StubTransletPayload extends AbstractTranslet implements Serializable {
	private static final long serialVersionUID = -5971610431559700674L;
	public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {}
	public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {}
}