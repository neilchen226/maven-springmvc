package net.nwc.admin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.admin.service.LogonService;
import net.nwc.sys.pojo.User;

@Controller
public class LogonController {

	@Autowired
	LogonService logonService;

	// 登录页面
	@RequestMapping(value = "/logon", produces = "text/html;charset=UTF-8")
	public ModelAndView logon(String redirect, HttpSession session) {
		return logonService.logon(redirect, session);
	}

	// 登录校验
	@ResponseBody
	@RequestMapping(value = "/logon/check", produces = "text/html;charset=UTF-8")
	public String logoncheck(User user,String valicode, HttpSession session,HttpServletRequest request) {
		return logonService.logonin(user, valicode , session,request);
	}

	// 登录验证码
	@RequestMapping(value = "/logon/valicode", produces = "text/html;charset=UTF-8")
	public void valicode(HttpServletResponse response, HttpSession session) throws Exception {
		logonService.valicode(response, session);
	}
	
	// 主页
	@RequestMapping("/main")
	public ModelAndView main(HttpSession session,HttpServletRequest request) {
		return logonService.main(session,request);
	}
	
	// 退出登录
	@RequestMapping(value = "/logon/out")
	public ModelAndView logonout(HttpSession session,HttpServletRequest request) {
		return logonService.logonout(session,request);
	}
}
