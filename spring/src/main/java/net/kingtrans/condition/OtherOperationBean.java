package net.kingtrans.condition;

import org.springframework.beans.factory.annotation.Value;

public class OtherOperationBean extends ConditionBean {
	
	@Value("OtherOperationBean")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
