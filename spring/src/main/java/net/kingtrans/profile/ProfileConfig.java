package net.kingtrans.profile;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class ProfileConfig {

	@Bean
	@Profile("Dev")
	public ProfileDemo devProfileDemo() {
		return new ProfileDemo("Dev");
	}
	@Bean
	@Profile("Release")
	public ProfileDemo releaseProfileDemo() {
		return new ProfileDemo("Release");
	}
}
