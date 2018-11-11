package JUnit4;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import net.kingtrans.admin.dao.TRoleMapper;
import net.kingtrans.admin.pojo.TRole;
import net.kingtrans.admin.pojo.TUser;
import net.kingtrans.admin.service.MainService;
import net.kingtrans.admin.service.TUserService;
import net.kingtrans.util.CharCodeUtil;
import net.kingtrans.util.HttpRequestUtil;

public class TestInstance extends JUnit4Test {
	private MockHttpServletRequest request;
	private MockHttpServletResponse response;

	@Resource
	MainService mainService;
	@Resource
	HttpRequestUtil httpRequestUtil;

	@Before
	public void before() {
		request = new MockHttpServletRequest();
		request.setCharacterEncoding("UTF-8");
		response = new MockHttpServletResponse();
		response.setCharacterEncoding("UTF-8");
	}

	@Autowired
	TUserService tUserService;
	TRoleMapper tRoleMapper;
	
	
	@Test
	public void SystemInit(){

		TUser admin = new TUser("admin", CharCodeUtil.MD5("admin123"), "系统管理员大神", "/resource/zmh/img/head.jpg", "admin");
		// 管理员用户组
		TRole role = new TRole(admin.getUserid(), "管理员", "默认拥有所有权限的用户组，请谨慎添加用户到该组。");
		tUserService.insertSelective(admin);
		tRoleMapper.insertSelective(role);
	}

}
