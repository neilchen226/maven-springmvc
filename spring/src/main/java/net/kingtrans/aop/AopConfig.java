package net.kingtrans.aop;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@ComponentScan("net.kingtrans.aop")
@EnableAspectJAutoProxy
public class AopConfig {
	
}
