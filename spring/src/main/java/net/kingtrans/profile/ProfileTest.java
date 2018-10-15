package net.kingtrans.profile;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ProfileTest {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext();
		ac.getEnvironment().setActiveProfiles("Release");
		ac.register(ProfileConfig.class);
		ac.refresh();
		ProfileDemo demo = ac.getBean(ProfileDemo.class);
		System.out.println(demo.getContent());


		ac.close();
	}
}
