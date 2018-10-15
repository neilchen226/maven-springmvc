package net.kingtrans.EL;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ELTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ELConfig.class);
		ELConfig aat = ac.getBean(ELConfig.class);
		System.out.println(aat.toString());
		ac.close();
	}
}
