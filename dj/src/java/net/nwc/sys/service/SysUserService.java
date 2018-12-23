package net.nwc.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.admin.service.LogonService;
import net.nwc.sys.dao.UserMapper;
import net.nwc.sys.pojo.User;
import net.nwc.sys.pojo.UserJobEnum;
import net.nwc.sys.pojo.WorkRecord;
import net.nwc.util.CharCodeUtil;
import net.nwc.util.ConvertUtil;
import net.nwc.util.Pagination;
import net.nwc.util.SystemUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SysUserService {

	@Autowired
	UserMapper userMapper;
	@Autowired
	SysWorkRecordService sysWorkRecordService;

	public String signFirst(HttpSession session) {
		JSONObject json = new JSONObject();
		User user = LogonService.getSessionUser(session);
		int flag = sysWorkRecordService.addLogonWorkRecord(user);
		if (flag == -1) {
			json.put("status", 500);
			json.put("message", "用户登录异常，请退出重新登录！");
		} else if (flag == 0) {
			json.put("status", 300);
			json.put("message", "已经打卡");
		} else {
			json.put("status", 200);
			json.put("message", "打卡成功");
		}
		return json.toString();
	}

	public String signLast(HttpSession session) {
		JSONObject json = new JSONObject();
		User user = LogonService.getSessionUser(session);
		String date = ConvertUtil.getNowDate();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("createdate", date);
		searchMap.put("userid", user.getUserid());
		List<WorkRecord> wrList = sysWorkRecordService.selectByMap(searchMap);
		if (wrList.size() == 0) {
			json.put("status", 500);
			json.put("message", "请先打上班卡");
			return json.toString();
		}
		WorkRecord wr = wrList.get(0);
		wr.setEndtime(ConvertUtil.getNowTime());
		int flag = sysWorkRecordService.updateWorkRecord(wr);
		if(flag>0) {
			json.put("status", 200);
			json.put("message", "打卡成功");
		}else {
			json.put("status", 500);
			json.put("message", "打卡失败，请刷新页面重试");
		}
		return json.toString();
	}

	public ModelAndView changePass_init(HttpSession session) {
		ModelAndView model = new ModelAndView();
		User user = (User) LogonService.getSessionUser(session);
		model.addObject("user", user);
		model.setViewName("/admin/changePass");
		return model;
	}

	public String changePass_do(HttpSession session, HttpServletRequest request, String oldpass, String newpass) {
		JSONObject json = new JSONObject();
		User user = (User) LogonService.getSessionUser(session);
		user = userMapper.selectByPrimaryKey(user.getUserid());
		if (!user.getPass().equals(CharCodeUtil.MD5(oldpass))) {
			json.put("status", 501);
			json.put("message", "旧密码输入错误！");
			return json.toString();
		}
		user.setPass(CharCodeUtil.MD5(newpass));
		int flag = userMapper.updateByPrimaryKeySelective(user);
		if (flag > 0) {
			json.put("status", 200);
			json.put("message", "修改成功");
			LogonService.setSessionUser(session, user);
		} else {
			json.put("status", 500);
			json.put("message", "修改失败");

		}
		return json.toString();
	}

	public ModelAndView changeInfo_init(HttpSession session) {
		ModelAndView model = new ModelAndView();
		User user = (User) LogonService.getSessionUser(session);

		model.addObject("jobnames", UserJobEnum.getList());
		model.addObject("user", user);
		model.setViewName("/admin/userInfo");
		return model;
	}

	public String changeInfo_do(HttpSession session, User user) {
		JSONObject json = new JSONObject();
		User sessionuser = (User) LogonService.getSessionUser(session);
		user.setUserid(sessionuser.getUserid());
		int flag = userMapper.updateByPrimaryKeySelective(user);
		if (flag > 0) {
			json.put("status", 200);
			json.put("message", "修改成功");
			LogonService.setSessionUser(session, userMapper.selectByPrimaryKey(user.getUserid()));
		} else {
			json.put("status", 500);
			json.put("message", "修改失败");
		}
		return json.toString();
	}

	public ModelAndView userlist_get() {
		ModelAndView model = new ModelAndView();
		model.setViewName("/sys/users");
		return model;
	}

	@SuppressWarnings("unchecked")
	public String userlist_post(Pagination page, User user) {
		JSONObject json = new JSONObject();
		page.init("userid", 0);
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.putAll(new BeanMap(user));
		searchMap.putAll(new BeanMap(page));
		int count = userMapper.geUserCountsByMap(searchMap);
		if (count > 0) {
			List<User> users = userMapper.geUsersByMap(searchMap);
			JSONArray array = JSONArray.fromObject(users);
			for (int i = 0; i < array.size(); i++) {
				JSONObject item = array.getJSONObject(i);
				item.put("jobname", UserJobEnum.getNameByCode(item.getString("jobname")));
			}
			json.put("data", array);
		}
		json.put("count", count);
		json.put("code", 0);
		json.put("msg", "");
		return json.toString();
	}

	public ModelAndView userinit(User user) {
		ModelAndView model = new ModelAndView();
		user = userMapper.selectByPrimaryKey(user.getUserid());
		model.addObject("user", user);
		if (user == null) {
			model.addObject("action", "add");
		} else {
			model.addObject("action", "update");
		}
		model.addObject("jobnames", UserJobEnum.getList());
		model.setViewName("/sys/userInit");
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
			User user) {
		JSONObject json = new JSONObject();
		User logonUser = LogonService.getSessionUser(session);
		if (logonUser == null) {
			json.put("status", 500);
			json.put("message", "用户登录超时，无法操作。请重新登录");
			return json.toString();
		}
		if (!UserJobEnum.admin.getCode().equals(logonUser.getJobname())) {
			json.put("status", 500);
			json.put("message", "您无权限修改用户信息");
			return json.toString();
		}
		if ("add".equals(action)) {
			User dbUser = userMapper.selectByPrimaryKey(user.getUserid());
			if (dbUser != null) {
				json.put("status", 500);
				json.put("message", "该登录名已使用，保存失败");
				return json.toString();
			}
			dbUser = userMapper.selectByUsernumber(user.getUsernumber());
			if (dbUser != null) {
				json.put("status", 500);
				json.put("message", "该代号已使用，不可重复使用！");
				return json.toString();
			}
			if(user.getIswork() ==null || user.getIswork() != 1) {
				user.setIswork(0);
			}
			user.setPass(CharCodeUtil.MD5("123456"));// 默认密码为123456
			userMapper.insertSelective(user);
		} else if ("update".equals(action)) {

			User dbUser = userMapper.selectByUsernumber(user.getUsernumber());
			if (dbUser != null && !dbUser.getUserid().equals(user.getUserid())) {
				json.put("status", 500);
				json.put("message", "该代号已使用，不可重复使用！");
				return json.toString();
			}
			if (!ConvertUtil.isNullOrEmpty(changePassFlag) && "1".equals(changePassFlag)) {
				user.setPass(CharCodeUtil.MD5("123456"));// 默认密码为123456
			}
			if (user.getIswork() ==null || user.getIswork() != 1) {
				user.setIswork(0);
			}
			userMapper.updateByPrimaryKeySelective(user);
		}
		json.put("status", 200);
		json.put("message", "保存成功");
		return json.toString();
	}

}
