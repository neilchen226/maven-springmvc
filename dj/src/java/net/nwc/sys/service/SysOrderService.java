package net.nwc.sys.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanMap;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.admin.service.LogonService;
import net.nwc.sys.dao.OrderMapper;
import net.nwc.sys.dao.OrderRecordMapper;
import net.nwc.sys.dao.UserMapper;
import net.nwc.sys.pojo.Order;
import net.nwc.sys.pojo.OrderRecord;
import net.nwc.sys.pojo.User;
import net.nwc.util.ConvertUtil;
import net.nwc.util.ExcelUtils;
import net.nwc.util.Pagination;
import net.nwc.util.SystemUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class SysOrderService {
	@Autowired
	OrderMapper orderMapper;
	@Autowired
	UserMapper userMapper;
	@Autowired
	OrderRecordMapper orderRecordMapper;

	public ModelAndView orders(HttpSession session) {
		ModelAndView model = new ModelAndView();

		// 所有在职的制图员
		List<User> drawers = userMapper.getDrawer();
		model.addObject("drawers", drawers);

		// 当前登录用户
		User user = LogonService.getSessionUser(session);
		model.addObject("user", user);

		String nowdate = ConvertUtil.getNowDate();
		model.addObject("startDate", nowdate.substring(0, 7) + "-01");
		model.addObject("endDate", nowdate);
		model.setViewName("/sys/orders");
		return model;
	}

	@SuppressWarnings("unchecked")
	public String orders(Pagination page, Order order, String startDate, String endDate) {
		JSONObject json = new JSONObject();
		page.init("editdate", 0);
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(startDate)) {
			searchMap.put("startDate", startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			searchMap.put("endDate", endDate);
		}
		if (StringUtils.isNotEmpty(order.getUserid()) && order.getUserid().indexOf(",") > 0) {
			String[] userids = order.getUserid().split(",");
			searchMap.put("userids", userids);
			order.setUserid(null);
		}
		if (StringUtils.isNotEmpty(order.getClient())) {
			String clientstr = order.getClient().replace("；", ";").replace(" ", ";").replace(",", ";").replace("，", ";");
			String[] clients = clientstr.split(";");
			searchMap.put("clients", clients);
			order.setClient(null);
		}
		searchMap.putAll(new BeanMap(order));
		searchMap.putAll(new BeanMap(page));
		int count = orderMapper.getOrderCountsByMap(searchMap);
		if (count > 0) {
			List<Order> orders = orderMapper.getOrderByMap(searchMap);
			JSONArray array = JSONArray.fromObject(orders);
			json.put("data", array);
			Map<String, Object> orderSumMap = orderMapper.getOrderSum(searchMap);
			json.put("ordersInfo", orderSumMap);
		}
		json.put("count", count);
		json.put("code", 0);
		json.put("msg", "");
		return json.toString();
	}

	public ModelAndView init(int orderid) {
		ModelAndView model = new ModelAndView();

		// 制图员
		List<User> drawers = userMapper.getDrawer();
		model.addObject("drawers", drawers);
		Order order = orderMapper.selectByPrimaryKey(orderid);
		if (order != null) {
			model.addObject("order", order);
		} else {
			order = new Order();
			order.setEditdate(ConvertUtil.getNowDate());
			model.addObject("order", order);
		}

		model.setViewName("/sys/orderInit");
		return model;
	}

	@Transactional
	public String save(Order order, User user) {
		JSONObject json = new JSONObject();
		if (order.getOrderid() == null) {
			order.setOrderid(0);
		}
		Order dborder = orderMapper.selectByPrimaryKey(order.getOrderid());
		if (dborder == null) {
			order.setStatus("0");
			if(StringUtils.isEmpty(order.getEditdate())) {
				order.setEditdate(ConvertUtil.getNowDate());
			}
			order.setEditor(user.getUsernumber());
			order.setIsdelete(0);
			int flag = orderMapper.insertSelective(order);
			if (flag > 0 && order.getOrderid() > 0) {
				OrderRecord or = new OrderRecord();
				or.setEditdate(ConvertUtil.getNowDateTime());
				or.setEditor(user.getUsernumber());
				or.setOrderid(order.getOrderid());
				or.setNote("创建订单");
				orderRecordMapper.insert(or);
				json.put("status", 200);
				json.put("message", "创建成功");
			} else {
				json.put("status", 500);
				json.put("message", "创建订单失败，稍后再试！");
			}
		} else {
			int flag = orderMapper.updateByPrimaryKeySelective(order);
			if (flag > 0) {
				String compareResult = compareOrder(order, dborder);
				if (StringUtils.isNotEmpty(compareResult)) {
					OrderRecord or = new OrderRecord();
					or.setEditdate(ConvertUtil.getNowDateTime());
					or.setEditor(user.getUsernumber());
					or.setOrderid(order.getOrderid());
					or.setNote(compareResult);
					orderRecordMapper.insert(or);
				}
				json.put("status", 200);
				json.put("message", "修改成功");
			} else {
				json.put("status", 500);
				json.put("message", "修改订单失败，稍后再试！");
			}
		}
		return json.toString();
	}

	String compareOrder(Order newOrder, Order oldOrder) {
		StringBuffer sb = new StringBuffer();
		String result = null;
		result = compareItem(oldOrder.getClient(), newOrder.getClient());
		if (result != null) {
			sb.append("客户" + result + "<br/>");
		}
		result = compareItem(oldOrder.getProjectname(), newOrder.getProjectname());
		if (result != null) {
			sb.append("项目名" + result + "<br/>");
		}
		result = compareItem(oldOrder.getUserid(), newOrder.getUserid());
		if (result != null) {
			sb.append("制图员" + result + "<br/>");
		}
		result = compareItem(oldOrder.getVmap(), newOrder.getVmap());
		if (result != null) {
			sb.append("视频图" + result + "<br/>");
		}
		result = compareItem(oldOrder.getSmap(), newOrder.getSmap());
		if (result != null) {
			sb.append("静态图" + result + "<br/>");
		}
		result = compareItem(oldOrder.getCost(), newOrder.getCost());
		if (result != null) {
			sb.append("总费用" + result + "<br/>");
		}
		result = compareItem(oldOrder.getPrecost(), newOrder.getPrecost());
		if (result != null) {
			sb.append("定金" + result + "<br/>");
		}
		result = compareItem(oldOrder.getPaycost(), newOrder.getPaycost());
		if (result != null) {
			sb.append("已付费用" + result + "<br/>");
		}
		result = compareItem(oldOrder.getPayway(), newOrder.getPayway());
		if (result != null) {
			sb.append("支付方式" + result + "<br/>");
		}
		result = compareItem(oldOrder.getNote(), newOrder.getNote());
		if (result != null) {
			sb.append("备注信息" + result + "<br/>");
		}
		return sb.toString();
	}

	String compareItem(Object old, Object newObj) {
		String oldStr = old + "";
		String newStr = newObj + "";
		System.out.println("[" + oldStr + "]--[" + newStr + "]");
		if (!oldStr.equals(newStr)) {
			return "旧值为[" + oldStr + "],修改为[" + newStr + "]";
		}
		return null;
	}

	public ModelAndView detail(Integer orderid) {
		ModelAndView model = new ModelAndView();
		Order order = orderMapper.selectByPrimaryKey(orderid);
		model.addObject("order", order);
		List<OrderRecord> records = orderRecordMapper.selectByOrderid(orderid);
		model.addObject("records", records);
		model.setViewName("/sys/orderDetail");
		return model;
	}

	@Transactional
	public String deleteItem(int orderid, User user) {
		JSONObject json = new JSONObject();
		json.put("status", 500);
		json.put("message", "设置失败，请刷新页面再试");

		Order order = new Order();
		order.setOrderid(orderid);
		order.setIsdelete(1);
		order.setDeletedate(ConvertUtil.getNowDate());
		int flag = orderMapper.updateByPrimaryKeySelective(order);
		if (flag > 0) {
			OrderRecord or = new OrderRecord();
			or.setEditdate(ConvertUtil.getNowDateTime());
			or.setEditor(user.getUsernumber());
			or.setOrderid(orderid);
			or.setNote("设置为废弃订单");
			orderRecordMapper.insert(or);
			json.put("status", 200);
			json.put("message", "设置成功");
		}
		return json.toString();
	}

	@Transactional
	public String cancelDelete(int orderid, User user) {
		JSONObject json = new JSONObject();
		json.put("status", 500);
		json.put("message", "设置失败，请刷新页面再试");

		Order order = new Order();
		order.setOrderid(orderid);
		order.setIsdelete(0);
		order.setDeletedate("empty");
		int flag = orderMapper.updateByPrimaryKeySelective(order);
		if (flag > 0) {
			OrderRecord or = new OrderRecord();
			or.setEditdate(ConvertUtil.getNowDateTime());
			or.setEditor(user.getUsernumber());
			or.setOrderid(orderid);
			or.setNote("订单取消废弃");
			orderRecordMapper.insert(or);
			json.put("status", 200);
			json.put("message", "设置成功");
		}
		return json.toString();
	}

	@Transactional
	public String destory(Integer orderid) {
		JSONObject json = new JSONObject();
		json.put("status", 500);
		json.put("message", "设置失败，请刷新页面再试");
		int flag = orderMapper.deleteByPrimaryKey(orderid);
		if(flag >0) {
			flag = orderRecordMapper.deleteByOrderid(orderid);
			if(flag>0) {
				json.put("status", 200);
				json.put("message", "删除成功");
			}
		}
		return json.toString();
	}
	
	public ModelAndView deleteOrders(HttpSession session) {
		ModelAndView model = new ModelAndView();

		// 所有在职的制图员
		List<User> drawers = userMapper.getDrawer();
		model.addObject("drawers", drawers);

		// 当前登录用户
		User user = LogonService.getSessionUser(session);
		model.addObject("user", user);

		String nowdate = ConvertUtil.getNowDate();
		model.addObject("startDate", nowdate.substring(0, 7) + "-01");
		model.addObject("endDate", nowdate);

		orderMapper.updateDeleteStatus();
		model.setViewName("/sys/deleteOrders");
		return model;
	}

	@SuppressWarnings("unchecked")
	public String deleteOrders(Pagination page, Order order, String startDate, String endDate) {
		JSONObject json = new JSONObject();
		page.init("deletedate", 0);
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (StringUtils.isNotEmpty(startDate)) {
			searchMap.put("deleteDate_start", startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			searchMap.put("deleteDate_end", endDate);
		}
		if (StringUtils.isNotEmpty(order.getUserid()) && order.getUserid().indexOf(",") > 0) {
			String[] userids = order.getUserid().split(",");
			searchMap.put("userids", userids);
			order.setUserid(null);
		}
		if (StringUtils.isNotEmpty(order.getClient())) {
			String clientstr = order.getClient().replace("；", ";").replace(" ", ";").replace(",", ";").replace("，", ";");
			String[] clients = clientstr.split(";");
			searchMap.put("clients", clients);
			order.setClient(null);
		}
		searchMap.putAll(new BeanMap(order));
		searchMap.putAll(new BeanMap(page));
		int count = orderMapper.getOrderCountsByMap(searchMap);
		if (count > 0) {
			List<Order> orders = orderMapper.getOrderByMap(searchMap);
			JSONArray array = JSONArray.fromObject(orders);
			json.put("data", array);
			Map<String, Object> orderSumMap = orderMapper.getOrderSum(searchMap);
			json.put("ordersInfo", orderSumMap);
		}
		json.put("count", count);
		json.put("code", 0);
		json.put("msg", "");
		return json.toString();
	}

	@SuppressWarnings("unchecked")
	public void export(Order order, User user, HttpServletRequest request, HttpServletResponse response,
			String startDate, String endDate) {
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			try {
				response.setContentType("text/html;charset=UTF-8");
				response.getOutputStream().write("<script>alert('请选择导出的时间范围')</script>".getBytes());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return;
		}
		if (StringUtils.isNotEmpty(startDate)) {
			searchMap.put("startDate", startDate);
		}
		if (StringUtils.isNotEmpty(endDate)) {
			searchMap.put("endDate", endDate);
		}
		if (StringUtils.isNotEmpty(order.getUserid()) && order.getUserid().indexOf(",") > 0) {
			String[] userids = order.getUserid().split(",");
			searchMap.put("userids", userids);
			order.setUserid(null);
		}
		if (StringUtils.isNotEmpty(order.getClient())) {
			String clientstr = order.getClient().replace("；", ";").replace(" ", ";").replace(",", ";").replace("，", ";");
			String[] clients = clientstr.split(";");
			searchMap.put("clients", clients);
			order.setClient(null);
		}
		searchMap.putAll(new BeanMap(order));
		searchMap.put("sortfield", "editdate");
		searchMap.put("sorttype", "desc");
		List<Order> orders = orderMapper.getOrderByMap(searchMap);
		Map<String, Object> orderSumMap = orderMapper.getOrderSum(searchMap);
		if (orders.size() == 0) {
			try {
				response.setContentType("text/html;charset=UTF-8");
				response.getOutputStream().write("<script>alert('无订单可导出')</script>".getBytes());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			return;
		}
		try {
			XSSFWorkbook wb = null;
			try {
				File templateFile = new File(SystemUtil.getWebRoot() + "/excel/templet_order.xlsx");
				wb = new XSSFWorkbook(new FileInputStream(templateFile));
			} catch (Exception e) {
				try {
					response.setContentType("text/html;charset=UTF-8");
					response.getOutputStream().write("<script>alert('导出报表错误：未找到导出模板文件')</script>".getBytes());
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
				return;
			}
			XSSFSheet sheet = wb.getSheetAt(0);

			XSSFFont font = wb.createFont();
//			font.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
			font.setFontHeightInPoints((short) 18);

			XSSFCellStyle style = wb.createCellStyle();
			style.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderTop(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderLeft(XSSFCellStyle.BORDER_MEDIUM);
			style.setBorderBottom(XSSFCellStyle.BORDER_MEDIUM);
			style.setFont(font);

			XSSFCell cell = null;
			cell = sheet.getRow(1).createCell(8);
			cell.setCellStyle(style);
			cell.setCellValue(startDate + " 至 " + endDate);
			int i = 3;
			for (Order dborder : orders) {
				XSSFRow row = sheet.createRow(i++);
				row.setHeightInPoints(26);

				cell = row.createCell(0);
				cell.setCellStyle(style);
				cell.setCellValue(dborder.getEditdate());

				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue(dborder.getClient());

				cell = row.createCell(2);
				cell.setCellStyle(style);
				cell.setCellValue(dborder.getUserid());

				String mapNum = "";
				if (dborder.getVmap() != null && dborder.getVmap() != 0) {
					mapNum += dborder.getVmap() + "+";
				}
				if(dborder.getSmap() == null) {
					mapNum +="0";
				}else {
					mapNum += dborder.getSmap();
				}
				cell = row.createCell(3);
				cell.setCellStyle(style);
				cell.setCellValue(mapNum);

				cell = row.createCell(4);
				cell.setCellStyle(style);
				if (dborder.getPrecost() != null) {
					cell.setCellValue(dborder.getPrecost().doubleValue());
				} else {
					cell.setCellValue("0");
				}

				cell = row.createCell(5);
				cell.setCellStyle(style);
				if (dborder.getPrecost() != null) {
					cell.setCellValue(dborder.getPrecost().doubleValue());
				} else {
					cell.setCellValue("0");
				}

				cell = row.createCell(6);
				cell.setCellStyle(style);
				if (dborder.getPaycost() != null) {
					cell.setCellValue(dborder.getPaycost().doubleValue());
				} else {
					cell.setCellValue("0");
				}

				cell = row.createCell(7);
				cell.setCellStyle(style);
				cell.setCellValue(dborder.getPayway());

				cell = row.createCell(8);
				cell.setCellStyle(style);
				cell.setCellValue(dborder.getProjectname());
				if (dborder.getIsdelete() != 0) {
					cell = row.createCell(9);
					cell.setCellStyle(style);
					cell.setCellValue("订单已作废，作废时间：" + dborder.getDeletedate().substring(0, 10));
				}
			}
			XSSFRow row = sheet.createRow(i);
			row.setHeightInPoints(26);
			cell = row.createCell(0);
			cell.setCellStyle(style);
			cell.setCellValue("合计");
			cell = row.createCell(1);
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellStyle(style);
			cell = row.createCell(3);
			cell.setCellStyle(style);
			cell.setCellValue(orderSumMap.get("vmap") + " + " + orderSumMap.get("smap"));
			cell = row.createCell(4);
			cell.setCellStyle(style);
			cell.setCellValue("" + orderSumMap.get("cost"));
			cell = row.createCell(5);
			cell.setCellStyle(style);
			cell.setCellValue("" + orderSumMap.get("precost"));
			cell = row.createCell(6);
			cell.setCellStyle(style);
			cell.setCellValue("" + orderSumMap.get("paycost"));
			cell = row.createCell(7);
			cell.setCellStyle(style);
			cell = row.createCell(8);
			cell.setCellStyle(style);

			// 导出文件名
			String fileName = ConvertUtil.getNowDate("yyyy年MM月dd日") + "导出订单列表.xlsx";
			String filename = "";
			try {
				filename = ExcelUtils.encodeChineseDownloadFileName(request, fileName);
			} catch (Exception e) {
				e.printStackTrace();
			}

			response.setHeader("Content-disposition", filename);
			response.setContentType("application/vnd.ms-excel");
			response.setHeader("Content-disposition", "attachment;filename=" + filename);
			response.setHeader("Pragma", "No-cache");
			OutputStream ouputStream = response.getOutputStream();
			wb.write(ouputStream);
			ouputStream.flush();
			ouputStream.close();

		} catch (Exception e) {
			try {
				response.setContentType("text/html;charset=UTF-8");
				response.getOutputStream()
						.write(("<script>alert('导出报表错误：+" + e.getMessage() + "')</script>").getBytes());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			e.printStackTrace();
		}

	}

}
