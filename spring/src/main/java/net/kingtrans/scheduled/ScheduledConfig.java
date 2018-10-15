package net.kingtrans.scheduled;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@ComponentScan("net.kingtrans.scheduled")
@EnableScheduling
public class ScheduledConfig {

}
