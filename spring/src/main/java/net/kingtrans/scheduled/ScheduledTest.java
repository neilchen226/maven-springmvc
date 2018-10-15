package net.kingtrans.scheduled;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ScheduledTest {

	public static void main(String[] args) throws InterruptedException {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ScheduledConfig.class);
		ScheduledDemo ted = ac.getBean(ScheduledDemo.class);
		ted.fixedRate();
		ted.cron();
		Thread.sleep(50000);
		ac.close();
	}
}
