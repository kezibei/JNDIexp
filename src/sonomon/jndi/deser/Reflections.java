package sonomon.jndi.deser;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import sun.reflect.ReflectionFactory;
import com.nqzero.permit.Permit;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

public class Reflections {
	private static  ClassPool classPool = ClassPool.getDefault();

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

	public static Field getField(final Class<?> clazz, final String fieldName) {
        Field field = null;
	try {
	    field = clazz.getDeclaredField(fieldName);
	    setAccessible(field);
        }
        catch (NoSuchFieldException ex) {
            if (clazz.getSuperclass() != null)
                field = getField(clazz.getSuperclass(), fieldName);
        }
		return field;
	}

	public static void setFieldValue(final Object obj, final String fieldName, final Object value) throws Exception {
		final Field field = getField(obj.getClass(), fieldName);
		field.set(obj, value);
	}

	public static Object getFieldValue(final Object obj, final String fieldName) throws Exception {
		final Field field = getField(obj.getClass(), fieldName);
		return field.get(obj);
	}

	public static Constructor<?> getFirstCtor(final String name) throws Exception {
		final Constructor<?> ctor = Class.forName(name).getDeclaredConstructors()[0];
	    setAccessible(ctor);
	    return ctor;
	}

	public static Object newInstance(String className, Object ... args) throws Exception {
        return getFirstCtor(className).newInstance(args);
    }

    public static Object createWithoutConstructor ( String clazz )
            throws Exception {
        return (Object) createWithoutConstructor(Class.forName(clazz));
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
    
    
    public static void insertField(String classname, String sUID) throws Exception {
        CtClass ctClass = classPool.get(classname);
    	insertField(ctClass, "serialVersionUID", "private static final long serialVersionUID = "+sUID+"L;");
    	
    }
    
	public static void insertField(CtClass ctClass, String fieldName, String fieldCode) throws Exception {
		if (ctClass.isFrozen()) {
			ctClass.defrost();
		}
		try {
			CtField field = ctClass.getDeclaredField(fieldName);
			ctClass.removeField(field);
		} catch (javassist.NotFoundException ignored) {
		}
		ctClass.addField(CtField.make(fieldCode, ctClass));
		//javassist改SUID有缺陷,只能改一次
		try {
			ctClass.toClass();
		} catch (Exception e) {
		}
	}
    

}