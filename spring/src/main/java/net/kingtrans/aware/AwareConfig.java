package net.kingtrans.aware;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@ComponentScan("net.kingtrans.aware")
public class AwareConfig {

	@Value("#{awareService.getResource('classpath:net/kingtrans/aware/test.txt')}")
	private Resource resource;

	public void showResource() {
		try {
			System.out.println(IOUtils.toString(resource.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	};
}
