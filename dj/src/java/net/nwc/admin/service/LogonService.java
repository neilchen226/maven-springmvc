package net.nwc.admin.service;

import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.admin.util.RandomImageUtil;
import net.nwc.sys.dao.OrderMapper;
import net.nwc.sys.dao.UserMapper;
import net.nwc.sys.pojo.User;
import net.nwc.sys.pojo.WorkRecord;
import net.nwc.sys.service.SysWorkRecordService;
import net.nwc.util.CharCodeUtil;
import net.nwc.util.ConvertUtil;
import net.sf.json.JSONObject;

@Service
public class LogonService {
	private static Logger logger = Logger.getLogger(LogonService.class);

	@Autowired
	UserMapper userMapper;
	@Autowired
	OrderMapper orderMapper;
	@Autowired
	SysWorkRecordService sysWorkRecordService;

	public ModelAndView logon(String redirect, HttpSession session) {
		ModelAndView model = new ModelAndView();
		String url = "/main";
		if (redirect != null) {
			url = redirect;
		}
		model.addObject("redirect", url);
		model.setViewName("/admin/logon");
		return model;
	}

	public String logonin(User user, String valicode, HttpSession session, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String svalicode = (String) session.getAttribute(sys_valicode);
		if (svalicode == null) {
			json.put("status", 500);
			json.put("message", "验证码失效");
		} else if (!svalicode.equalsIgnoreCase(valicode)) {
			json.put("status", 501);
			json.put("message", "验证码输入有误");
		} else {
			User tUser = userMapper.selectByPrimaryKey(user.getUserid());
			if (tUser == null || !tUser.getPass().equals(CharCodeUtil.MD5(user.getPass()))) {
				json.put("status", 510);
				json.put("message", "用户名密码不匹配");
			} else {
				if (tUser.getIswork() != 1) {
					json.put("status", 510);
					json.put("message", "用户名已离职，无法登录系统");
					return json.toString();
				}
				json.put("status", 200);
				json.put("message", "登录成功");

				JSONObject userinfo = JSONObject.fromObject(tUser);
				userinfo.remove("password");// 移除密码字段
				json.put("userinfo", userinfo);

				// 保存到session
				setSessionUser(session, tUser);
				// 添加登录上班打卡
				sysWorkRecordService.addLogonWorkRecord(tUser);
			}
		}
		return json.toString();
	}

	public void valicode(HttpServletResponse response, HttpSession session) throws Exception {
		// 利用图片工具生成图片
		Object[] objs = RandomImageUtil.createImage(); // 第一个参数是生成的验证码，第二个参数是生成的图片
		session.setAttribute(sys_valicode, objs[0]); // 将验证码存入Session
		if (logger.isInfoEnabled()) {
			logger.info("生成的验证码： " + objs[0]);
		}
		BufferedImage image = (BufferedImage) objs[1]; // 将图片输出给浏览器
		response.setContentType("image/png");
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "png", os);

	}

	public ModelAndView logonout(HttpSession session, HttpServletRequest request) {
		removeSessionUser(session);
		ModelAndView model = new ModelAndView();
		model.setViewName("/admin/logon");// 退回网站首页
		return model;
	}

	public static User getSessionUser(HttpSession session) {
		Object obj = session.getAttribute(logonuser);
		if (null != obj)
			return (User) obj;
		return null;
	}

	public static void setSessionUser(HttpSession session, Object obj) {
		session.setAttribute(logonuser, obj);
	}

	private static void removeSessionUser(HttpSession session) {
		session.removeAttribute(logonuser);
	}

	public ModelAndView main(HttpSession session, HttpServletRequest request) {
		ModelAndView model = new ModelAndView();
		User user = getSessionUser(session);
		model.addObject("user", user);

		String date = ConvertUtil.getNowDate();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("createdate", date);
		searchMap.put("userid", user.getUserid());
		model.addObject("firstStatus", "0");
		model.addObject("lastStatus", "0");
		List<WorkRecord> wrList = sysWorkRecordService.selectByMap(searchMap);
		if (wrList.size() > 0) {
			WorkRecord wr = wrList.get(0);
			if (StringUtils.isNotEmpty(wr.getFisrttime())) {
				model.addObject("firstStatus", "1");
			}
			if (StringUtils.isNotEmpty(wr.getEndtime())) {
				model.addObject("lastStatus", "1");
			}
		}
		if (user.getIswork() != null && user.getIswork() == 1) {
			int deleteOrderCount = orderMapper.getDeleteOrderCount();
			model.addObject("deleteOrderCount", deleteOrderCount);
		}
		model.setViewName("/admin/main");
		return model;
	}

	private static final String sys_valicode = "SESSION_VARCODE";
	private static final String logonuser = "SESSION_LOGON_USER";
}
