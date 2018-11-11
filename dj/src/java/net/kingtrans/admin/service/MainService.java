package net.kingtrans.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.kingtrans.admin.Enum.TLoggerTypeEnum;
import net.kingtrans.admin.cache.TmoduleCache;
import net.kingtrans.admin.dao.TLoggerMapper;
import net.kingtrans.admin.dao.TModuleMapper;
import net.kingtrans.admin.dao.TModuleRoleMapper;
import net.kingtrans.admin.dao.TRoleMapper;
import net.kingtrans.admin.pojo.TLogger;
import net.kingtrans.admin.pojo.TModule;
import net.kingtrans.admin.pojo.TRole;
import net.kingtrans.admin.pojo.TUser;
import net.kingtrans.util.CharCodeUtil;
import net.kingtrans.util.ConvertUtil;
import net.kingtrans.util.SystemUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

@Service
public class MainService {

	public static Logger logger = Logger.getLogger(MainService.class);

	@Autowired
	TmoduleCache tmoduleCache;
	@Autowired
	TModuleRoleMapper tModuleRoleMapper;
	@Autowired
	TModuleMapper tModuleMapper;
	@Autowired
	TRoleMapper tRoleMapper;
	@Autowired
	TLoggerMapper tLoggerMapper;
	@Autowired
	TUserService tUserService;

	public ModelAndView main(HttpSession session) {
		ModelAndView model = new ModelAndView();

		model.setViewName("/admin/main");
		return model;
	}

	public String getTModules(HttpSession session) {
		JSONObject json = new JSONObject();
		Object sessionUser = LogonService.getSessionUser(session);
		if (null == sessionUser) {
			return "";
		}
		TUser user = (TUser) sessionUser;// 当前用户

		Set<String> roles = tModuleRoleMapper.getRoleByRoleid(user.getRoleid());// 用户权限集
		List<TModule> list = tmoduleCache.getTModules();// 所有菜单模块
		JSONArray array = tmoduleCache.getUserModuleTree(list, roles, "0");// 用户权限树
		if (logger.isDebugEnabled()) {
			logger.debug("获取【" + user.getUsername() + "】的菜单\n" + array.toString());
		}
		json.put("status", 200);
		json.put("menu", array);
		return json.toString();
	}

	@Transactional
	public String init(HttpSession session, HttpServletRequest request) {
		TUser admin = tUserService.selectByPrimaryKey("admin");
		if (admin != null) {
			TUser user = LogonService.getSessionUser(session);
			if (user == null || !"admin".equals(user.getUserid())) {
				return "初始化失败, 系统管理员才有权限初始化！";
			}
		}

		List<TModule> list = new ArrayList<TModule>();
		// 依次为： 模块ID, 标题, 图标, 链接, 是否展开, 是否叶子, 父模块, 同级顺序, 是否独立功能

		// 订单系统
		list.add(new TModule("000100", "订单系统", "", "", 1, 0, "0", 0));
		list.add(new TModule("100100", "订单", "", "/order/list", 0, 1, "000100", 1));

		// 系统资料
		list.add(new TModule("000800", "系统资料", "", "", 0, 0, "0", 8));
		list.add(new TModule("800100", "客户资料", "", "/data/client/list", 0, 1, "000800", 1));
		
		// 系统设置
		list.add(new TModule("000900", "系统设置", "", "", 0, 0, "0", 9));
		list.add(new TModule("900100", "用户管理", "", "/sys/tuser/userlist", 0, 1, "000900", 1));
		list.add(new TModule("900150", "用户组管理", "", "/sys/tuser/rolelist", 0, 1, "000900", 2));
		list.add(new TModule("900200", "日志记录", "", "/sys/log/list", 0, 1, "000900", 3));

		if (admin == null) {
			admin = new TUser("admin", CharCodeUtil.MD5("admin123"), "系统管理员大神", "/resource/zmh/img/head.jpg", "admin");
			// 管理员用户组
			TRole role = new TRole(admin.getUserid(), "管理员", "默认拥有所有权限的用户组，请谨慎添加用户到该组。");
			tUserService.insertSelective(admin);
			tRoleMapper.insertSelective(role);
		}

		tModuleMapper.deleteAll();// 先清理菜单
		tModuleMapper.insertList(list);// 插入所有菜单
		tmoduleCache.refreshTModules();
		tModuleRoleMapper.deleteByRoleid("admin");// 删除admin权限
		tModuleRoleMapper.initAdminRole();// 初始化admin权限
		// 保存登录记录
		TLogger tlog = new TLogger(admin.getUserid(), TLoggerTypeEnum.init.getCode(), ConvertUtil.getNowTime(), "用户【"
				+ admin.getUsername() + "】执行了系统初始化操作", SystemUtil.getRemortIP(request));
		tLoggerMapper.insertSelective(tlog);

		return "系统初始化完成！  <a href='/main'>返回首页</a>";
	}

	public ModelAndView changePass_init(HttpSession session) {
		ModelAndView model = new ModelAndView();
		TUser user = (TUser) LogonService.getSessionUser(session);
		model.addObject("user", user);
		model.setViewName("/admin/changePass");
		return model;
	}

	public String changePass_do(HttpSession session, HttpServletRequest request, String oldpass, String newpass) {
		JSONObject json = new JSONObject();
		TUser user = (TUser) LogonService.getSessionUser(session);
		user = tUserService.selectByPrimaryKey(user.getUserid());
		if (!user.getPassword().equals(CharCodeUtil.MD5(oldpass))) {
			json.put("status", 501);
			json.put("message", "旧密码输入错误！");
			return json.toString();
		}
		user.setPassword(CharCodeUtil.MD5(newpass));
		tUserService.updateByPrimaryKeySelective(user);
		// 日志记录
		TLogger tlog = new TLogger(user.getUserid(), TLoggerTypeEnum.changePass.getCode(), ConvertUtil.getNowTime(),
				"用户【" + user.getUsername() + "】修改了登录密码", SystemUtil.getRemortIP(request));
		tLoggerMapper.insertSelective(tlog);
		json.put("status", 200);
		json.put("message", "修改成功");
		return json.toString();
	}

	public ModelAndView changeInfo_init(HttpSession session) {
		ModelAndView model = new ModelAndView();
		TUser user = (TUser) LogonService.getSessionUser(session);
		model.addObject("user", user);
		TRole role = tRoleMapper.selectByPrimaryKey(user.getRoleid());
		model.addObject("role", role);
		model.setViewName("/admin/userInfo");
		return model;
	}

	public String changeInfo_do(HttpSession session, TUser user) {
		JSONObject json = new JSONObject();
		TUser sessionuser = (TUser) LogonService.getSessionUser(session);
		user.setUserid(sessionuser.getUserid());
		tUserService.updateByPrimaryKeySelective(user);
		json.put("status", 200);
		json.put("message", "修改成功");
		return json.toString();
	}

}
