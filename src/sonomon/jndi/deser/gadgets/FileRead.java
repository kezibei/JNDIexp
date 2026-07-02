package sonomon.jndi.deser.gadgets;

import java.io.FileInputStream;

public class FileRead implements SerializeObject{
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		return new Object();
	}
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
    	FileInputStream inputFromFile = new FileInputStream(payload);
    	byte[] bs = new byte[inputFromFile.available()];
    	inputFromFile.read(bs);
    	inputFromFile.close();
    	return bs;
    }



}