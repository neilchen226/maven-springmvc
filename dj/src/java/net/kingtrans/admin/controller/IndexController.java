package net.kingtrans.admin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.kingtrans.admin.service.IndexService;
import net.kingtrans.util.Pagination;

@Controller
public class IndexController {
	@Resource
	IndexService indexService;

	// 首页
	@RequestMapping(value = "/index")
	public ModelAndView index(HttpSession session) {
		return indexService.index(session);
	}

	// 首页请求数据
	@ResponseBody
	@RequestMapping(value = "/index/getSortcodeStatus", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String getSortcodeStatus(String cityid, Pagination page) {
		return "{}";
	}
	
}
