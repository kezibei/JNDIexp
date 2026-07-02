package sonomon.jndi.factory;

public class DruidFactory extends DBFactory{

	//druid-1.2.6.jar
	DruidFactory() {
		super("com.alibaba.druid.pool.DruidDataSourceFactory");
	}


	
}
