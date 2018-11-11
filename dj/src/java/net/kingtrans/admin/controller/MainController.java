package net.kingtrans.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.kingtrans.admin.pojo.TUser;
import net.kingtrans.admin.service.MainService;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

	@Resource
	MainService mainService;

	// 初始化系统
	@ResponseBody
	@RequestMapping(value = "/init", produces = "text/html;charset=UTF-8")
	public String init(HttpSession session, HttpServletRequest request) {
		return mainService.init(session, request);
	}

	// 登录系统后跳转地址
	@RequestMapping(value = "/main")
	public ModelAndView main(HttpSession session) {
		return mainService.main(session);
	}

	// 加载功能模块
	@ResponseBody
	@RequestMapping(value = "/getTModules", produces = "text/html;charset=UTF-8")
	public String getTModules(HttpSession session) {
		return mainService.getTModules(session);
	}

	// 用户修改密码
	@RequestMapping(value = "/changePass", method = RequestMethod.GET)
	public ModelAndView changePass(HttpSession session) {
		return mainService.changePass_init(session);
	}

	// 用户修改密码
	@ResponseBody
	@RequestMapping(value = "/changePass", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String changePass_post(HttpSession session, HttpServletRequest request , String oldpass, String newpass) {
		return mainService.changePass_do(session, request, oldpass, newpass);
	}

	// 用户信息修改
	@RequestMapping(value = "/changeUserInfo", method = RequestMethod.GET)
	public ModelAndView changeInfo(HttpSession session) {
		return mainService.changeInfo_init(session);
	}

	// 用户信息修改
	@ResponseBody
	@RequestMapping(value = "/changeUserInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String changeInfo(HttpSession session, TUser user) {
		return mainService.changeInfo_do(session, user);
	}


}
