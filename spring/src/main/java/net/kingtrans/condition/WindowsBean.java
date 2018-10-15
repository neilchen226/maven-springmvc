package net.kingtrans.condition;

import org.springframework.beans.factory.annotation.Value;

public class WindowsBean extends ConditionBean {

	@Value("WindowsBean")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
