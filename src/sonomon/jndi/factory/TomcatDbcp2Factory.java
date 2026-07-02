package sonomon.jndi.factory;


public class TomcatDbcp2Factory extends DBFactory{
	//tomcat-dbcp=>8.0.1
	TomcatDbcp2Factory() {
		super("org.apache.tomcat.dbcp.dbcp2.BasicDataSourceFactory");
	}
}
