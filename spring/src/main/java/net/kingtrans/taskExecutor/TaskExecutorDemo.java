package net.kingtrans.taskExecutor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskExecutorDemo {

	@Async
	public void add(int i) {
		System.out.println("add: "+(++i));
	}
	@Async
	public void sub(int i) {
		System.out.println("sub: "+(--i));
	}
	
}
