package net.kingtrans.sys.controller;

import net.kingtrans.admin.pojo.TLogger;
import net.kingtrans.sys.service.TLoggerService;
import net.kingtrans.util.Pagination;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TLoggerController {

	@Autowired
	TLoggerService tLoggerService;

	@RequestMapping(value = "/sys/log/list", method = RequestMethod.GET)
	ModelAndView loglist() {
		return tLoggerService.loglist();
	}

	@ResponseBody
	@RequestMapping(value = "/sys/log/list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	String loglist(Pagination page, TLogger tLogger, String starttime, String endtime) {
		return tLoggerService.loglist(page, tLogger, starttime, endtime);
	}

}
