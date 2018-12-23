package net.nwc.sys.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.sys.dao.UserMapper;
import net.nwc.sys.dao.WorkRecordMapper;
import net.nwc.sys.pojo.User;
import net.nwc.sys.pojo.WorkRecord;
import net.nwc.util.ConvertUtil;
import net.nwc.util.ExcelUtils;
import net.nwc.util.SystemUtil;

@Service
public class SysWorkRecordService {
	
	@Value("#{configProperties['startWorkTime']}")
	String startWorkTime;
	@Value("#{configProperties['finishWorkTime']}")
	String finishWorkTime;

	@Autowired
	WorkRecordMapper workRecordMapper;

	@Transactional
	public int addLogonWorkRecord(User user) {
		String date = ConvertUtil.getNowDate();
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("createdate", date);
		searchMap.put("userid", user.getUserid());
		List<WorkRecord> wrList = selectByMap(searchMap);
		int flag = 0;
		if (wrList.size() == 0) {
			if (user == null || StringUtils.isEmpty(user.getUserid())) {
				flag = -1;
				return flag;
			}
			WorkRecord wr = new WorkRecord();
			wr.setCreatedate(date);
			wr.setUserid(user.getUserid());
			wr.setFisrttime(ConvertUtil.getNowTime());
			flag = workRecordMapper.insertSelective(wr);
		} else if (wrList.size() > 0) {
			flag = 0;
		}
		return flag;
	}

	public List<WorkRecord> selectByMap(Map<String, Object> searchMap) {
		return workRecordMapper.selectByMap(searchMap);
	}

	public int updateWorkRecord(WorkRecord wr) {
		return workRecordMapper.updateSelective(wr);
	}

	public ModelAndView list(String createdate) {
		ModelAndView model = new ModelAndView();
		if (StringUtils.isEmpty(createdate)) {
			createdate = ConvertUtil.getNowDate().substring(0, 7);
		}
		int year = Integer.parseInt(createdate.substring(0, 4));
		int month = Integer.parseInt(createdate.substring(5, 7));
		int days = ConvertUtil.getDaysByYearMonth(year, month);
		List<String> dayList = new ArrayList<>();
		for (int i = 1; i <= days; i++) {
			String daystr = i < 10 ? "0" + i : "" + i;
			dayList.add(createdate + "-" + daystr);
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("month", createdate);
		List<WorkRecord> userRecords = workRecordMapper.selectByMap(searchMap);

		List<User> users = userMapper.getAllUsers();
		String[][] datas = new String[days + 3][users.size() * 2 + 1];
		datas[0][0] = "";
		for (int j = 0; j < users.size(); j++) {
			datas[0][2 * j + 1] = users.get(j).getUsername();
		}
		datas[1][0] = "日期";
		for (int j = 0; j < users.size(); j++) {
			datas[1][2 * j + 1] = "上班时间";
			datas[1][2 * j + 2] = "下班时间";
		}
		Map<String, Integer> sumWr = new HashMap<String, Integer>();
		for (int i = 0; i < dayList.size(); i++) {
			String dayStr = dayList.get(i);
			int line = i + 2;
			datas[line][0] = dayStr;
			for (int j = 0; j < users.size(); j++) {
				User user = users.get(j);
				for (WorkRecord wr : userRecords) {
					if (wr.getCreatedate().contains(dayStr) && wr.getUserid().equals(user.getUserid())) {
						String firsttime = wr.getFisrttime();
						String endtime = wr.getEndtime();
						if (StringUtils.isNotEmpty(firsttime) && firsttime.compareTo(startWorkTime) > 0) {
							firsttime = firsttime + "[迟到]";
							if(sumWr.containsKey(user.getUserid()+"_startWorkLate")) {
								int num = sumWr.get(user.getUserid()+"_startWorkLate");
								sumWr.put(user.getUserid()+"_startWorkLate", num +1);
							}else {
								sumWr.put(user.getUserid()+"_startWorkLate", 1);
							}
						}
						if (StringUtils.isNotEmpty(endtime) && endtime.compareTo(finishWorkTime) < 0) {
							endtime = endtime + "[早退]";
							if(sumWr.containsKey(user.getUserid()+"_finishWorkEarly")) {
								int num = sumWr.get(user.getUserid()+"_finishWorkEarly");
								sumWr.put(user.getUserid()+"_finishWorkEarly", num +1);
							}else {
								sumWr.put(user.getUserid()+"_finishWorkEarly", 1);
							}
						}
						datas[line][2 * j + 1] = firsttime;
						datas[line][2 * j + 2] = endtime;
					}
				}
			}
		}

		datas[days+2][0]= "统计";
		for (int j = 0; j < users.size(); j++) {
			User user = users.get(j);
			if(sumWr.containsKey(user.getUserid()+"_startWorkLate")) {
				datas[days+2][2 * j + 1] = sumWr.get(user.getUserid()+"_startWorkLate")+"";
			}
			if(sumWr.containsKey(user.getUserid()+"_finishWorkEarly")) {
				datas[days+2][2 * j + 2] = sumWr.get(user.getUserid()+"_finishWorkEarly")+"";
			}
		}

		model.addObject("workDatas", datas);
		model.addObject("nowMonth", createdate);
		model.setViewName("/sys/workrecords");
		return model;
	}

	@Autowired
	UserMapper userMapper;

	public void export(HttpServletRequest request, HttpServletResponse response, String createdate) {
		if (StringUtils.isEmpty(createdate)) {
			createdate = ConvertUtil.getNowDate().substring(0, 7);
		}
		int year = Integer.parseInt(createdate.substring(0, 4));
		int month = Integer.parseInt(createdate.substring(5, 7));
		int days = ConvertUtil.getDaysByYearMonth(year, month);
		List<String> dayList = new ArrayList<>();
		for (int i = 1; i <= days; i++) {
			String daystr = i < 10 ? "0" + i : "" + i;
			dayList.add(createdate + "-" + daystr);
		}
		Map<String, Object> searchMap = new HashMap<String, Object>();
		searchMap.put("month", createdate);
		List<WorkRecord> userRecords = workRecordMapper.selectByMap(searchMap);

		List<User> users = userMapper.getAllUsers();
		String[][] datas = new String[days + 3][users.size() * 2 + 1];
		datas[0][0] = "";
		for (int j = 0; j < users.size(); j++) {
			datas[0][2 * j + 1] = users.get(j).getUsername();
		}
		datas[1][0] = "日期";
		for (int j = 0; j < users.size(); j++) {
			datas[1][2 * j + 1] = "上班时间";
			datas[1][2 * j + 2] = "下班时间";
		}

		Map<String, Integer> sumWr = new HashMap<String, Integer>();
		for (int i = 0; i < dayList.size(); i++) {
			String dayStr = dayList.get(i);
			int line = i + 2;
			datas[line][0] = dayStr;
			for (int j = 0; j < users.size(); j++) {
				User user = users.get(j);
				for (WorkRecord wr : userRecords) {
					if (wr.getCreatedate().contains(dayStr) && wr.getUserid().equals(user.getUserid())) {
						String firsttime = wr.getFisrttime();
						String endtime = wr.getEndtime();
						if (StringUtils.isNotEmpty(firsttime) && firsttime.compareTo(startWorkTime) > 0) {
							firsttime = firsttime + "[迟到]";
							if(sumWr.containsKey(user.getUserid()+"_startWorkLate")) {
								int num = sumWr.get(user.getUserid()+"_startWorkLate");
								sumWr.put(user.getUserid()+"_startWorkLate", num +1);
							}else {
								sumWr.put(user.getUserid()+"_startWorkLate", 1);
							}
						}
						if (StringUtils.isNotEmpty(endtime) && endtime.compareTo(finishWorkTime) < 0) {
							endtime = endtime + "[早退]";
							if(sumWr.containsKey(user.getUserid()+"_finishWorkEarly")) {
								int num = sumWr.get(user.getUserid()+"_finishWorkEarly");
								sumWr.put(user.getUserid()+"_finishWorkEarly", num +1);
							}else {
								sumWr.put(user.getUserid()+"_finishWorkEarly", 1);
							}
						}
						datas[line][2 * j + 1] = firsttime;
						datas[line][2 * j + 2] = endtime;
					}
				}
			}
		}

		datas[days+2][0]= "统计";
		for (int j = 0; j < users.size(); j++) {
			User user = users.get(j);
			if(sumWr.containsKey(user.getUserid()+"_startWorkLate")) {
				datas[days+2][2 * j + 1] = sumWr.get(user.getUserid()+"_startWorkLate")+"";
			}
			if(sumWr.containsKey(user.getUserid()+"_finishWorkEarly")) {
				datas[days+2][2 * j + 2] = sumWr.get(user.getUserid()+"_finishWorkEarly")+"";
			}
		}
		try {
			XSSFWorkbook wb = null;
			try {
				File templateFile = new File(SystemUtil.getWebRoot() + "/excel/templet_workRecord.xlsx");
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
			CellStyle yellowstyle= wb.createCellStyle();

			XSSFColor color = new XSSFColor(new java.awt.Color(255, 130, 0));
			XSSFFont font = wb.createFont();
			font.setColor(color);
			yellowstyle.setFont(font);
//			yellowstyle.setFillBackgroundColor(color.getIndexed());
//			yellowstyle.setFillPattern(yellowstyle.getFillPattern());
			
			XSSFCell cell = null;
			cell = sheet.getRow(0).getCell(0);
			String str = cell.getStringCellValue();
			str = "夏印设计" + year+"年"+ month + "月份考勤表";
			cell.setCellValue(str);
			int line = 1;
			for (int i = 0; i < datas.length; i++) {
				XSSFRow row = sheet.createRow(line + i);
				for (int j = 0; j < datas[i].length; j++) {
					if(i==0) {
						sheet.setColumnWidth(j, 20 * 256);
					}
					cell = row.createCell(j);
					String value = datas[i][j];
					if(value != null && (value.contains("迟到") || value.contains("早退"))) {
						cell.setCellStyle(yellowstyle);
					}
					cell.setCellValue(datas[i][j]);
				}
			}

			// 导出文件名
			String fileName = str + ".xlsx";
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
						.write(("<script>alert('导出报表错误："+ e.getCause() + "')</script>").getBytes());
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
			e.printStackTrace();
		}

	}
}
