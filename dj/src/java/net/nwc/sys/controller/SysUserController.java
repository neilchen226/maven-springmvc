package net.nwc.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.sys.pojo.User;
import net.nwc.sys.service.SysUserService;
import net.nwc.util.Pagination;

@Controller
public class SysUserController {

	@Resource
	SysUserService sysUserService;

	// 用户修改密码
	@RequestMapping(value = "/sys/user/changePass", method = RequestMethod.GET)
	public ModelAndView changePass(HttpSession session) {
		return sysUserService.changePass_init(session);
	}

	// 用户修改密码
	@ResponseBody
	@RequestMapping(value = "/sys/user/changePass", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String changePass_post(HttpSession session, HttpServletRequest request , String oldpass, String newpass) {
		return sysUserService.changePass_do(session, request, oldpass, newpass);
	}

	// 用户信息修改
	@RequestMapping(value = "/sys/user/changeUserInfo", method = RequestMethod.GET)
	public ModelAndView changeInfo(HttpSession session) {
		return sysUserService.changeInfo_init(session);
	}

	// 用户信息修改
	@ResponseBody
	@RequestMapping(value = "/sys/user/changeUserInfo", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String changeInfo(HttpSession session, User user) {
		return sysUserService.changeInfo_do(session, user);
	}
	
	// 上班签到
	@ResponseBody
	@RequestMapping(value = "/sys/user/signFirst", produces = "text/html;charset=UTF-8")
	public String signFirst(HttpSession session) {
		return sysUserService.signFirst(session);
	}
	// 下班签到
	@ResponseBody
	@RequestMapping(value = "/sys/user/signLast", produces = "text/html;charset=UTF-8")
	public String signLast(HttpSession session) {
		return sysUserService.signLast(session);
	}
	
	

	// 用户列表
	@RequestMapping(value = "/sys/users", method = RequestMethod.GET)
	public ModelAndView userlist_get() {
		return sysUserService.userlist_get();
	}

	// 用户列表json
	@ResponseBody
	@RequestMapping(value = "/sys/users", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String userlist_post(Pagination page, User user) {
		return sysUserService.userlist_post(page, user);
	}

	// 编辑用户
	@RequestMapping(value = "/sys/user/init")
	public ModelAndView userinit(User user) {
		return sysUserService.userinit(user);
	}

	// 上传用户头像
	@ResponseBody
	@RequestMapping(value = "/sys/user/uploadHeadImg", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String uploadUserFace(MultipartFile userFaceField) {
		return sysUserService.uploadUserFace(userFaceField);
	}

	// 保存用户
	@ResponseBody
	@RequestMapping(value = "/sys/user/save", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String userSave(HttpSession session, HttpServletRequest request, String action, String changePassFlag,
			User user) {
		return sysUserService.userSave(session, request, action, changePassFlag, user);
	}

}
