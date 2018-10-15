package net.kingtrans.condition;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class WindowsCondition implements Condition {

	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		// TODO Auto-generated method stub
		String osName = context.getEnvironment().getProperty("os.name");
		System.out.println(osName);
		return osName.contains("Windows");
	}

}
