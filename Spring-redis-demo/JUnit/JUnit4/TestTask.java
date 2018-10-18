package JUnit4;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;

public class TestTask extends JUnit4Test {

	@Autowired
	TaskExecutor taskExecutor;

	@Test
	public void test() {

	}

}
