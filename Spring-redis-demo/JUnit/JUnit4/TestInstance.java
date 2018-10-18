package JUnit4;

import java.util.Set;

import net.kingtrans.test.cache.StudentCache;
import net.kingtrans.test.pojo.Student;
import net.kingtrans.utils.RedisUtil;
import net.sf.json.JSONObject;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.fasterxml.jackson.databind.util.JSONPObject;

public class TestInstance extends JUnit4Test {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Autowired
	StudentCache studentCache;
	@Autowired
	RedisUtil redisUtil;

	@Before
	public void before() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
	}
	@Test
	public void myTest(){
		System.out.println("test");
		studentCache.getStudent(0);
		studentCache.getStudent(2);
		System.out.println("1");
		studentCache.getStudent(2);
//		System.out.println("2");
//		studentCache.clear(0);
//		System.out.println("2");
//		studentCache.getStudent(0);
//		studentCache.clearAll();
		studentCache.getStudent(0);
		Object obj = redisUtil.get("studentCache_0");
		if( obj instanceof Student ){
			System.out.println(JSONObject.fromObject(obj));
		}

		Set<Object> objs = redisUtil.sGet("studentCache~keys");
		System.out.println(objs.size());
		System.out.println("end");
	}

}
