package net.kingtrans.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AopAspect {


	@Pointcut("@annotation(net.kingtrans.aop.AopAnnotation)")
	public void pointcut() {};
	
	@Before("pointcut()")
	public void beforeAopAnnotation(JoinPoint jp) {
	  MethodSignature s = (MethodSignature) jp.getSignature();
	  Method method = s.getMethod();
	  AopAnnotation ant = method.getAnnotation(AopAnnotation.class);
	  System.out.println("before拦截携带了"+ant.toString()+"注解的"+method.getName()+"方法运行");
	};
	
	
}
