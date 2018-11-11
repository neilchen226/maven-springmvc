package net.kingtrans.Task;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HistoryDataProcessTask {

	public static Logger logger = Logger.getLogger(HistoryDataProcessTask.class);

	/**
	 * 每个月10号 20号凌晨1点清理历史数据。
	 */
	@Scheduled(cron = "0 0 1 10,20 * ?")
	public void process() {}

	

}
