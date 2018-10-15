package net.kingtrans.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ScheduledDemo {

	@Scheduled(fixedRate = 5000)
	public void fixedRate() {
		System.out.println("fixedRate");
	}

	@Scheduled(cron = "0/2 * * * * *")
	public void cron() {
		System.out.println("cron");
	}

}
