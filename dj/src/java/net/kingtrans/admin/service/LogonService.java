package net.kingtrans.admin.service;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.kingtrans.admin.Enum.TLoggerTypeEnum;
import net.kingtrans.admin.dao.TLoggerMapper;
import net.kingtrans.admin.dao.TUserMapper;
import net.kingtrans.admin.pojo.LogonUser;
import net.kingtrans.admin.pojo.TLogger;
import net.kingtrans.admin.pojo.TUser;
import net.kingtrans.admin.util.RandomImageUtil;
import net.kingtrans.util.CharCodeUtil;
import net.kingtrans.util.ConvertUtil;
import net.kingtrans.util.SystemUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class LogonService {
	private static Logger logger = Logger.getLogger(LogonService.class);

	@Autowired
	TUserMapper tuserMapper;

	@Autowired
	TLoggerMapper tLoggerMapper;

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

	public String logonin(LogonUser user, HttpSession session, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		String svalicode = (String) session.getAttribute(valicode);
		if (svalicode == null) {
			json.put("status", 500);
			json.put("message", "验证码失效");
		} else if (!svalicode.equalsIgnoreCase(user.getValicode())) {
			json.put("status", 501);
			json.put("message", "验证码输入有误");
		} else {
			TUser tUser = tuserMapper.selectByPrimaryKey(user.getUsername());
			if (tUser == null || !tUser.getPassword().equals(CharCodeUtil.MD5(user.getPassword()))) {
				json.put("status", 510);
				json.put("message", "用户名密码不匹配");
			} else {
				json.put("status", 200);
				json.put("message", "登录成功");

				JSONObject userinfo = JSONObject.fromObject(tUser);
				userinfo.remove("password");// 移除密码字段
				json.put("userinfo", userinfo);

				// 保存到session
				setSessionUser(session, tUser);
				// 保存登录记录
				TLogger tlog = new TLogger(tUser.getUserid(), TLoggerTypeEnum.logon.getCode(),
						ConvertUtil.getNowTime(), "用户【" + tUser.getUsername() + "】登录系统",
						SystemUtil.getRemortIP(request));
				tLoggerMapper.insertSelective(tlog);

			}
		}
		return json.toString();
	}

	public void valicode(HttpServletResponse response, HttpSession session) throws Exception {
		// 利用图片工具生成图片
		Object[] objs = RandomImageUtil.createImage(); // 第一个参数是生成的验证码，第二个参数是生成的图片
		session.setAttribute(valicode, objs[0]); // 将验证码存入Session
		if (logger.isInfoEnabled()) {
			logger.info("生成的验证码： " + objs[0]);
		}
		BufferedImage image = (BufferedImage) objs[1]; // 将图片输出给浏览器
		response.setContentType("image/png");
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "png", os);

	}

	public ModelAndView logonout(HttpSession session,HttpServletRequest request) {
		
		TUser tUser = getSessionUser(session);
		// 保存退出记录
		TLogger tlog = new TLogger(tUser.getUserid(), TLoggerTypeEnum.logonout.getCode(),
				ConvertUtil.getNowTime(), "用户【" + tUser.getUsername() + "】退出系统",
				SystemUtil.getRemortIP(request));
		tLoggerMapper.insertSelective(tlog);
		
		removeSessionUser(session);
		ModelAndView model = new ModelAndView();
		model.setViewName("/admin/logon");// 退回网站首页
		return model;
	}

	public static TUser getSessionUser(HttpSession session) {
		Object obj = session.getAttribute(logonuser);
		if (null != obj)
			return (TUser) obj;
		return null;
	}

	public static void setSessionUser(HttpSession session, Object obj) {
		session.setAttribute(logonuser, obj);
	}

	private static void removeSessionUser(HttpSession session) {
		session.removeAttribute(logonuser);
	}

	private static final String valicode = "SESSION_VARCODE";
	private static final String logonuser = "SESSION_LOGON_USER";
}
