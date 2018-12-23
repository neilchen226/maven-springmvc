package net.nwc.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.sys.service.SysWorkRecordService;

@Controller
public class SysWorkRecordController {

	@Autowired
	SysWorkRecordService sysWorkRecordService;

	// 订单列表
	@RequestMapping(value = "/sys/workrecord")
	public ModelAndView list(String createdate) {
		return sysWorkRecordService.list(createdate);
	}

	// 导出考勤
	@ResponseBody
	@RequestMapping(value = "/sys/workrecord/export", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public void export(HttpServletRequest request, HttpServletResponse response, String createdate) {
		sysWorkRecordService.export(request, response, createdate);
	}

}
