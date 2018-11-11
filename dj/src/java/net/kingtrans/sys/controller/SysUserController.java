package net.kingtrans.sys.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.kingtrans.admin.pojo.TRole;
import net.kingtrans.admin.pojo.TUser;
import net.kingtrans.sys.service.SysUserService;
import net.kingtrans.util.Pagination;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SysUserController {

	@Resource
	SysUserService sysUserService;

	// 用户列表
	@RequestMapping(value = "/sys/tuser/userlist", method = RequestMethod.GET)
	public ModelAndView userlist_get() {
		return sysUserService.userlist_get();
	}

	// 用户列表json
	@ResponseBody
	@RequestMapping(value = "/sys/tuser/userlist", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String userlist_post(Pagination page, TUser user) {
		return sysUserService.userlist_post(page, user);
	}

	// 编辑用户
	@RequestMapping(value = "/sys/tuser/userinit")
	public ModelAndView userinit(TUser user) {
		return sysUserService.userinit(user);
	}

	// 上传用户头像
	@ResponseBody
	@RequestMapping(value = "/sys/tuser/uploadUserFace", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String uploadUserFace(MultipartFile userFaceField) {
		return sysUserService.uploadUserFace(userFaceField);
	}

	// 保存用户
	@ResponseBody
	@RequestMapping(value = "/sys/tuser/userSave", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String userSave(HttpSession session, HttpServletRequest request, String action, String changePassFlag,
			TUser user) {
		return sysUserService.userSave(session, request, action, changePassFlag, user);
	}


	// 用户组列表
	@RequestMapping(value = "/sys/tuser/rolelist", method = RequestMethod.GET)
	public ModelAndView rolelist_get() {
		return sysUserService.rolelist_get();
	}

	// 用户组列表-json
	@ResponseBody
	@RequestMapping(value = "/sys/tuser/rolelist", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String rolelist_post(TRole role) {
		return sysUserService.rolelist_post(role);
	}

	// 编辑用户组
	@RequestMapping(value = "/sys/tuser/roleinit")
	public ModelAndView roleinit(TRole role) {
		return sysUserService.roleinit(role);
	}

	// 保存用户组
	@ResponseBody
	@RequestMapping(value = "/sys/tuser/roleSave", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String roleSave(TRole role, String moduleid, String action) {
		return sysUserService.roleSave(role, moduleid, action);
	}

	// 删除用户组
	@ResponseBody
	@RequestMapping(value = "/sys/tuser/roleDel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String roleDel(String roleids) {
		return sysUserService.roleDel(roleids);
	}

}
