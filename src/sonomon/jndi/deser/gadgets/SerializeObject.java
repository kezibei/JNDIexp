package sonomon.jndi.deser.gadgets;

public interface SerializeObject {
	Object getJavaSerializeObject(String payload, String jarname)  throws Exception ;
	byte[] getJavaSerializeData(String payload, String jarname)  throws Exception ;
	
}
