package JUnit4;

import javax.annotation.Resource;

import org.junit.Before;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import net.nwc.util.HttpRequestUtil;

public class TestInstance extends JUnit4Test {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	@Resource
	HttpRequestUtil httpRequestUtil;

	@Before
	public void before() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
	}

	

}
