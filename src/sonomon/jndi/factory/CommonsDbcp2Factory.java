package sonomon.jndi.factory;


public class CommonsDbcp2Factory extends DBFactory{
	//commons-dbcp2-2.9.0.jar/commons-pool2-2.11.1.jar
	CommonsDbcp2Factory() {
		super("org.apache.commons.dbcp2.BasicDataSourceFactory");
	}
}
