package sonomon.jndi.factory;


public class TomcatDbcp1Factory extends DBFactory{
	//tomcat-dbcp<=7.0.109
	TomcatDbcp1Factory() {
		super("org.apache.tomcat.dbcp.dbcp.BasicDataSourceFactory");
	}
}
