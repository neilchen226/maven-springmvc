package net.kingtrans.condition;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("net.kingtrans.condition")
public class ConditionConfig {

	@Bean
	@Conditional(WindowsCondition.class)
	public ConditionBean getWindowConditonBean() {
		return new WindowsBean();
	}

	@Bean
	@Conditional(OtherOperationCondition.class)
	public ConditionBean getOtherOperationBean() {
		return new OtherOperationBean();
	}
}
