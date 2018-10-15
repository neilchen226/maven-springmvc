package net.kingtrans.event;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class SpringEventListener2 implements ApplicationListener<SpringEvent> {

	@Override
	public void onApplicationEvent(SpringEvent event) {
		String msg= event.getMsg();
		System.out.println("事件监听器2接收到事件信息： "+ msg);
		System.out.println("event.getSource()： "+ event.getSource());
	}

}
