package JUnit4;

import java.io.FileNotFoundException;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

@SuppressWarnings("deprecation")
public class JUnit4ClassRunner extends SpringJUnit4ClassRunner {  
	  
    static {  
        try {  
            Log4jConfigurer.initLogging("classpath:config/properties/log4j.properties");  
        } catch (FileNotFoundException ex) {  
            System.err.println("初始化log4j");  
        }  
    }  
      
    public JUnit4ClassRunner(Class<?> clazz) throws InitializationError {  
        super(clazz);  
    }  
  
} 