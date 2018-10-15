package net.kingtrans.aware;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AwareTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AwareConfig.class);
		AwareConfig aat = ac.getBean(AwareConfig.class);
		aat.showResource();
		AwareService s = ac.getBean(AwareService.class);
		System.out.println(s.toString());
		ac.close();
	}
}
