package sonomon.jndi.factory;


public class CommonsDbcp1Factory extends DBFactory{
	//commons-dbcp-1.4.jar/commons-pool-1.6.jar
	CommonsDbcp1Factory() {
		super("org.apache.commons.dbcp.BasicDataSourceFactory");
	}
}
