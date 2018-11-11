package JUnit4;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(JUnit4ClassRunner.class)
// 表示继承了SpringJUnit4ClassRunner类
@TransactionConfiguration(transactionManager = "transactionManager")
@ContextConfiguration(locations = { "classpath:config/spring/appContext.xml", "classpath:config/spring/webContext.xml" })
public class JUnit4Test {

}
