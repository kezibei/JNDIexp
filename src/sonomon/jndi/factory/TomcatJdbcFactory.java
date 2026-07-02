package sonomon.jndi.factory;


public class TomcatJdbcFactory extends DBFactory{
	//tomcat-jdbc-8.5.57.jar/tomcat-juli-8.5.16.jar
	TomcatJdbcFactory() {
		super("org.apache.tomcat.jdbc.pool.DataSourceFactory");
	}
}
