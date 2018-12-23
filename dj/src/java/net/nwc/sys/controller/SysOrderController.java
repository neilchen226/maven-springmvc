package net.nwc.sys.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.admin.service.LogonService;
import net.nwc.sys.pojo.Order;
import net.nwc.sys.pojo.User;
import net.nwc.sys.service.SysOrderService;
import net.nwc.util.Pagination;

@Controller
public class SysOrderController {

	@Autowired
	SysOrderService sysOrderService;

	// 订单列表
	@RequestMapping(value = "/sys/orders", method = RequestMethod.GET)
	public ModelAndView orders(HttpSession session) {
		return sysOrderService.orders(session);
	}

	@ResponseBody
	@RequestMapping(value = "/sys/orders", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String orders(Pagination page, Order order, String startDate, String endDate) {
		return sysOrderService.orders(page, order, startDate, endDate);
	}

	// 初始化订单
	@RequestMapping(value = "/sys/order/init", method = RequestMethod.GET)
	public ModelAndView init(Integer orderid) {
		return sysOrderService.init(orderid);
	}

	// 保存订单
	@ResponseBody
	@RequestMapping(value = "/sys/order/save", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String save(Order order, HttpSession session) {
		User user = LogonService.getSessionUser(session);
		return sysOrderService.save(order, user);
	}

	// 订单详情
	@RequestMapping(value = "/sys/order/detail", method = RequestMethod.GET)
	public ModelAndView detail(Integer orderid) {
		return sysOrderService.detail(orderid);
	}

	// 废弃订单
	@ResponseBody
	@RequestMapping(value = "/sys/order/deleteItem", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String deleteItem(Integer orderid, HttpSession session) {
		User user = LogonService.getSessionUser(session);
		return sysOrderService.deleteItem(orderid, user);
	}
	
	// 彻底删除
	@ResponseBody
	@RequestMapping(value = "/sys/order/destory", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String destory(Integer orderid) {
		return sysOrderService.destory(orderid);
	}

	// 订单列表
	@RequestMapping(value = "/sys/deleteOrders", method = RequestMethod.GET)
	public ModelAndView deleteOrders(HttpSession session) {
		return sysOrderService.deleteOrders(session);
	}

	@ResponseBody
	@RequestMapping(value = "/sys/deleteOrders", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String deleteOrders(Pagination page, Order order, String startDate, String endDate) {
		return sysOrderService.deleteOrders(page, order, startDate, endDate);
	}

	// 废弃订单
	@ResponseBody
	@RequestMapping(value = "/sys/order/cancelDelete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String cancelDelete(Integer orderid, HttpSession session) {
		User user = LogonService.getSessionUser(session);
		return sysOrderService.cancelDelete(orderid, user);
	}

	// 导出订单
	@ResponseBody
	@RequestMapping(value = "/sys/order/export", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public void export(Order order, HttpServletRequest request, HttpServletResponse response, String startDate,
			String endDate) {
		User user = LogonService.getSessionUser(request.getSession());
		sysOrderService.export(order, user, request, response, startDate, endDate);
	}

}
