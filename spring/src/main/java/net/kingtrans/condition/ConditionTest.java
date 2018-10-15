package net.kingtrans.condition;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConditionTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ConditionConfig.class);
		ConditionBean cb = ac.getBean(ConditionBean.class);
		System.out.println(cb.getName());
		ac.close();
	}

}
