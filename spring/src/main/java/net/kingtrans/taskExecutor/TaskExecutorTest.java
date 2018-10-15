package net.kingtrans.taskExecutor;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class TaskExecutorTest {
	
	public static void main(String[] args) throws InterruptedException {
		AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(TaskExecutorConfig.class);
		TaskExecutorDemo ted = ac.getBean(TaskExecutorDemo.class);
		for (int i = 0; i < 20; i++) {
			ted.add(i);
			ted.sub(i);
		}
		Thread.sleep(5000);
		ac.close();
	}
}
