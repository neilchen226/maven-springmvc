package net.kingtrans.aop;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AopTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AopConfig.class);
		AopAnnotationTest aat = ac.getBean(AopAnnotationTest.class);
		aat.testAnnotation();
		ac.close();
	}
}
