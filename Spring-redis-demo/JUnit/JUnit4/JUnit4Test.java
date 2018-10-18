package JUnit4;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(JUnit4ClassRunner.class)
// 表示继承了SpringJUnit4ClassRunner类
@WebAppConfiguration
@ContextConfiguration(locations = { "classpath:config/spring/appContext.xml"})
public class JUnit4Test {

}
