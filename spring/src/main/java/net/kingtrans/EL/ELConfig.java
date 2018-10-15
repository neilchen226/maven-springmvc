package net.kingtrans.EL;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;

@Configuration
@ComponentScan("net.kingtrans.EL")
@PropertySource("classpath:net/kingtrans/El/el.properties")
public class ELConfig {

	@Value("I Love You")
	private String words;

	@Value("#{systemProperties['os.name']}")
	private String OS;

	@Value("#{ T(java.lang.Math).random() * 100.0}")
	private double random;

	@Value("${el.name}")
	private String name;

	@Value("classpath:net/kingtrans/El/el.properties")
	private Resource testFile;

	@Value("https://www.baidu.com/")
	private Resource testUrl;

	@Value("#{other.other}")
	private String other;

	@Autowired
	private Environment environment;

	@Override
	public String toString() {
		try {
			return "ELConfig:\n" + "words=" + words + "\n OS=" + OS + "\n random=" + random + "\n name=" + name
					+ "\n testFile=" + IOUtils.toString(testFile.getInputStream()) + "\n testUrl="
					+ IOUtils.toString(testUrl.getInputStream()) + "\n other=" + other + "\n environment="
					+ environment;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "IOException";
		}
	}

}
