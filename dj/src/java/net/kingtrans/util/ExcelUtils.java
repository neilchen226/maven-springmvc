package net.kingtrans.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.util.StringUtils;

public class ExcelUtils {
	// excel默认宽度；
	private static int width = 256 * 14;
	// 默认字体
	private static String excelfont = "微软雅黑";

	@SuppressWarnings("deprecation")
	public static void export(String excelName, String sheetName, String[] headers, String[] ds_titles,
			int[] ds_format, int[] widths, List<Map<String, Object>> data, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (widths == null) {
			widths = new int[ds_titles.length];
			for (int i = 0; i < ds_titles.length; i++) {
				widths[i] = width;
			}
		}
		if (ds_format == null) {
			ds_format = new int[ds_titles.length];
			for (int i = 0; i < ds_titles.length; i++) {
				ds_format[i] = 1;
			}
		}
		// 设置文件名
		String fileName = "";
		if (!StringUtils.isEmpty(excelName)) {
			fileName = excelName;
		}
		// 创建一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 创建一个sheet
		HSSFSheet sheet = wb.createSheet(StringUtils.isEmpty(sheetName) ? sheetName : "excel");
		// 创建表头，如果没有跳过
		int headerrow = 0;
		if (headers != null) {
			HSSFRow row = sheet.createRow(headerrow);
			// 表头样式
			HSSFCellStyle style = wb.createCellStyle();
			HSSFFont font = wb.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontName(excelfont);
			font.setFontHeightInPoints((short) 11);
			style.setFont(font);
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			style.setBorderRight(HSSFCellStyle.BORDER_THIN);
			style.setBorderTop(HSSFCellStyle.BORDER_THIN);
			for (int i = 0; i < headers.length; i++) {
				sheet.setColumnWidth((short) i, (short) widths[i]);
				HSSFCell cell = row.createCell(i);
				cell.setCellValue(headers[i]);
				cell.setCellStyle(style);
			}
			headerrow++;
		}
		// 表格主体 解析list
		if (data != null) {
			List<HSSFCellStyle> styleList = new ArrayList<HSSFCellStyle>();

			for (int i = 0; i < ds_titles.length; i++) { // 列数
				HSSFCellStyle style = wb.createCellStyle();
				HSSFFont font = wb.createFont();
				font.setFontName(excelfont);
				font.setFontHeightInPoints((short) 10);
				style.setFont(font);
				style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
				style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
				style.setBorderRight(HSSFCellStyle.BORDER_THIN);
				style.setBorderTop(HSSFCellStyle.BORDER_THIN);
				if (ds_format[i] == 1) {
					style.setAlignment(HSSFCellStyle.ALIGN_LEFT);
				} else if (ds_format[i] == 2) {
					style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				} else if (ds_format[i] == 3) {
					style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					// int类型
				} else if (ds_format[i] == 4) {
					style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					// int类型
					style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));
				} else if (ds_format[i] == 5) {
					// float类型
					style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					style.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));
				} else if (ds_format[i] == 6) {
					// 百分比类型
					style.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00%"));
				}
				styleList.add(style);
			}
			for (int i = 0; i < data.size(); i++) { // 行数
				HSSFRow row = sheet.createRow(headerrow);
				Map<String, Object> map = data.get(i);
				for (int j = 0; j < ds_titles.length; j++) { // 列数
					HSSFCell cell = row.createCell(j);
					Object o = map.get(ds_titles[j]);
					if (o == null || "".equals(o)) {
						cell.setCellValue("");
					} else if (ds_format[j] == 4) {
						// int
						cell.setCellValue((Long.valueOf((map.get(ds_titles[j])) + "")).longValue());
					} else if (ds_format[j] == 5 || ds_format[j] == 6) {
						// float
						cell.setCellValue((Double.valueOf((map.get(ds_titles[j])) + "")).doubleValue());
					} else {
						cell.setCellValue(map.get(ds_titles[j]) + "");
					}

					cell.setCellStyle((HSSFCellStyle) styleList.get(j));
				}
				headerrow++;
			}
		}

		fileName = fileName + ".xls";
		String filename = "";
		try {
			filename = encodeChineseDownloadFileName(request, fileName);
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
	}

	/**
	 * 对文件流输出下载的中文文件名进行编码 屏蔽各种浏览器版本的差异性
	 * 
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeChineseDownloadFileName(HttpServletRequest request, String pFileName) throws Exception {

		String filename = null;
		String agent = request.getHeader("USER-AGENT");
		if (null != agent) {
			if (-1 != agent.indexOf("Firefox")) {// Firefox
				filename = "=?UTF-8?B?"
						+ (new String(org.apache.commons.codec.binary.Base64.encodeBase64(pFileName.getBytes("UTF-8"))))
						+ "?=";
			} else if (-1 != agent.indexOf("Chrome")) {// Chrome
				filename = new String(pFileName.getBytes(), "ISO8859-1");
			} else {// IE7+
				filename = java.net.URLEncoder.encode(pFileName, "UTF-8");
				filename = filename.replace("+", "%20");
			}
		} else {
			filename = pFileName;
		}
		return filename;
	}
	
	public static int getExcelMaxRow(){
		return 40000;
	}
}