package net.kingtrans.aop;

import org.springframework.stereotype.Component;

@Component
public class AopAnnotationTest {

	@AopAnnotation
	public void testAnnotation() {
		System.out.println("run testAnnotation");
	}
	
}
