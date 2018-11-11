package net.kingtrans.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.kingtrans.admin.Enum.TLoggerTypeEnum;
import net.kingtrans.admin.cache.TUserCache;
import net.kingtrans.admin.cache.TmoduleCache;
import net.kingtrans.admin.dao.TLoggerMapper;
import net.kingtrans.admin.dao.TModuleRoleMapper;
import net.kingtrans.admin.dao.TRoleMapper;
import net.kingtrans.admin.dao.TUserMapper;
import net.kingtrans.admin.pojo.TLogger;
import net.kingtrans.admin.pojo.TModule;
import net.kingtrans.admin.pojo.TModuleRole;
import net.kingtrans.admin.pojo.TModuleTree;
import net.kingtrans.admin.pojo.TRole;
import net.kingtrans.admin.pojo.TUser;
import net.kingtrans.admin.service.LogonService;
import net.kingtrans.util.CharCodeUtil;
import net.kingtrans.util.ConvertUtil;
import net.kingtrans.util.Pagination;
import net.kingtrans.util.SystemUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SysUserService {

	@Autowired
	TUserMapper tUserMapper;
	@Autowired
	TUserCache tUserCache;
	@Autowired
	TLoggerMapper tLoggerMapper;
	@Autowired
	TRoleMapper tRoleMapper;
	@Autowired
	TModuleRoleMapper tModuleRoleMapper;
	@Autowired
	TmoduleCache tmoduleCache;

	public ModelAndView userlist_get() {
		ModelAndView model = new ModelAndView();
		model.setViewName("/sys/tuser/userlist");
		return model;
	}

	@SuppressWarnings("unchecked")
	public String userlist_post(Pagination page, TUser user) {
		JSONObject json = new JSONObject();
		page.init("userid", 0);
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.putAll(new BeanMap(user));
		searchMap.putAll(new BeanMap(page));
		int count = tUserMapper.getUserCountsByMap(searchMap);
		if (count > 0) {
			List<Map<String, Object>> users = tUserMapper.getUsersByMap(searchMap);
			JSONArray array = JSONArray.fromObject(users);
			json.put("data", array);
		}
		json.put("count", count);
		json.put("code", 0);
		json.put("msg", "");
		return json.toString();
	}

	public ModelAndView userinit(TUser user) {
		ModelAndView model = new ModelAndView();
		user = tUserMapper.selectByPrimaryKey(user.getUserid());
		model.addObject("user", user);
		if (user == null) {
			model.addObject("action", "add");
		} else {
			model.addObject("action", "update");
		}
		List<TRole> roles = tRoleMapper.selectAll(null);
		model.addObject("roles", roles);

		model.setViewName("/sys/tuser/userInit");
		return model;
	}

	public String uploadUserFace(MultipartFile userFace) {
		JSONObject json = new JSONObject();
		if (userFace != null) {
			String userFacePath = "/resource/upload/userFace";// 头像保存路径
			try {
				String userFaceName = SystemUtil.saveFileFromMultipartFile(userFace, userFacePath);
				json.put("status", 200);
				json.put("message", "上传成功");
				json.put("userFaceUrl", userFacePath + "/" + userFaceName);
			} catch (Exception e) {
				e.printStackTrace();
				json.put("status", 500);
				json.put("message", "上传头像保存失败");
			}
		}
		return json.toString();
	}

	@Transactional
	public String userSave(HttpSession session, HttpServletRequest request, String action, String changePassFlag,
			TUser user) {
		JSONObject json = new JSONObject();
		TUser logonUser = LogonService.getSessionUser(session);
		if (logonUser == null) {
			json.put("status", 500);
			json.put("message", "用户登录超时，无法操作。请重新登录");
			return json.toString();
		}
		if ("add".equals(action)) {
			TUser dbUser = tUserMapper.selectByPrimaryKey(user.getUserid());
			if (dbUser != null) {
				json.put("status", 500);
				json.put("message", "该登录名已使用，保存失败");
				return json.toString();
			}
			user.setPassword(CharCodeUtil.MD5(user.getUserid() + "123"));// 默认密码为登录名+123
			tUserMapper.insertSelective(user);
		} else if ("update".equals(action)) {
			if (!ConvertUtil.isNullOrEmpty(changePassFlag) && "1".equals(changePassFlag)) {
				user.setPassword(CharCodeUtil.MD5(user.getUserid() + "123"));// 默认密码为登录名+123

				// 日志记录
				TLogger tlog = new TLogger(logonUser.getUserid(), TLoggerTypeEnum.changePass.getCode(),
						ConvertUtil.getNowTime(), "用户【" + logonUser.getUsername() + "】通过修改用户信息初始化了【"
								+ user.getUsername() + "】的登录密码", SystemUtil.getRemortIP(request));
				tLoggerMapper.insertSelective(tlog);
			}
			tUserMapper.updateByPrimaryKeySelective(user);
			tUserCache.reload(user);
		}
		json.put("status", 200);
		json.put("message", "保存成功");
		return json.toString();
	}

	public ModelAndView rolelist_get() {
		ModelAndView model = new ModelAndView();
		model.setViewName("/sys/tuser/rolelist");
		return model;
	}

	public String rolelist_post(TRole role) {
		JSONObject json = new JSONObject();
		List<TRole> roles = tRoleMapper.selectAll(role);
		JSONArray array = JSONArray.fromObject(roles);
		json.put("data", array);
		json.put("count", roles.size());
		json.put("code", 0);
		json.put("msg", "");
		return json.toString();
	}

	public ModelAndView roleinit(TRole role) {
		ModelAndView model = new ModelAndView();
		role = tRoleMapper.selectByPrimaryKey(role.getRoleid());
		model.addObject("role", role);
		Set<String> moduleroles = new HashSet<String>();
		if (role != null) {
			model.addObject("action", "update");
			moduleroles = tModuleRoleMapper.getRoleByRoleid(role.getRoleid());// 用户组权限集
		} else {
			model.addObject("action", "add");
		}
		model.addObject("moduleroles", moduleroles);
		List<TModule> list = tmoduleCache.getTModules();// 所有菜单模块
		List<TModuleTree> tmoduleTree = tmoduleCache.getModuleTree(list, moduleroles, "0");
		model.addObject("tmoduleTree", tmoduleTree);

		model.setViewName("/sys/tuser/roleInit");
		return model;
	}

	@Transactional
	public String roleSave(TRole role, String moduleid, String action) {
		if ("update".equals(action)) {
			tRoleMapper.updateByPrimaryKeySelective(role);
			tModuleRoleMapper.deleteByRoleid(role.getRoleid());
		} else {
			if (tRoleMapper.selectByPrimaryKey(role.getRoleid()) != null) {
				JSONObject json = new JSONObject();
				json.put("status", 500);
				json.put("message", "该用户组编码已存在，不可添加！");
				return json.toString();
			}
			tRoleMapper.insertSelective(role);
		}
		String[] moduleids = moduleid.split(",");
		List<TModuleRole> mrlist = new ArrayList<TModuleRole>();
		for (String string : moduleids) {
			mrlist.add(new TModuleRole(role.getRoleid(), string));
		}
		tModuleRoleMapper.insertList(mrlist);

		JSONObject json = new JSONObject();
		json.put("status", 200);
		json.put("message", "保存成功");
		return json.toString();
	}

	@Transactional
	public String roleDel(String roleids) {
		JSONObject json = new JSONObject();
		List<TRole> roles = tRoleMapper.getUsingRole();// 正在使用的用户组
		String rolename = "";
		for (TRole tRole : roles) {
			if (roleids.indexOf(tRole.getRoleid()) > -1) {
				rolename += "," + tRole.getRolename();
			}
		}
		if (rolename.length() > 0) {
			json.put("status", 500);
			json.put("message", "【" + rolename.substring(1) + "】正在使用中，无法删除!");
		} else {
			String[] roleid = roleids.split(",");
			tRoleMapper.deleteByPrimaryKeys(roleid);
			tModuleRoleMapper.deleteByRoleids(roleid);
			json.put("status", 200);
			json.put("message", "删除成功");
		}
		return json.toString();
	}

}
