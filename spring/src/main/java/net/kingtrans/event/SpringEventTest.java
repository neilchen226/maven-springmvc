package net.kingtrans.event;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringEventTest {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext aac = new AnnotationConfigApplicationContext(SpringEventConfig.class);
		SpringEventPublisher publisher = aac.getBean(SpringEventPublisher.class);
		publisher.publish("啊哈哈");
		aac.close();
		
	}
}
