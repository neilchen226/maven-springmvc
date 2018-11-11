package net.kingtrans.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ConvertUtil {

	public static boolean isNullOrEmpty(String s) {
		return s == null || s.trim().length() == 0 ? true : false;
	}

	public static String getNowTime() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}

	public static String getNowDate() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
	}

	public static String getNowDate(String formatStr) {
		SimpleDateFormat format = new SimpleDateFormat(formatStr);
		return format.format(new Date());
	}

	public static String getDateTimeStr() {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		return format.format(new Date());
	}

	public static String getISO8601DateTimeStr(String dateStr) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.CHINA);
		SimpleDateFormat p = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			dateStr = f.format(p.parse(dateStr)) + "+08:00";
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateStr;
	}

	public static int getDaysByYearMonth(int year, int month) {
		Calendar a = Calendar.getInstance();
		a.set(Calendar.YEAR, year);
		a.set(Calendar.MONTH, month - 1);
		a.set(Calendar.DATE, 1);
		a.roll(Calendar.DATE, -1);
		int maxDate = a.get(Calendar.DATE);
		return maxDate;
	}

	// 计算两个日期相差的天数，精确小数3位
	public static double getDayNumsByDays(Date d1, Date d2) {
		BigDecimal diff = new BigDecimal(d1.getTime() - d2.getTime());
		BigDecimal diffDays = diff.divide(new BigDecimal(60l * 60 * 24 * 1000), 3, BigDecimal.ROUND_HALF_UP);
		return diffDays.doubleValue();
	}

	public static double getDayNumsByDays(String d1, String d2) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return getDayNumsByDays(f.parse(d1), f.parse(d2));
		} catch (ParseException e) {
			return 0.0;
		}
	}

	// 单位换算，t/kg/g 换成KG,其他原样输出
	public static String getWeightWithUnit(String weight, String unit) {
		if ("kg".equalsIgnoreCase(unit) || "千克".equals(unit)) {
			return weight;
		} else if ("g".equalsIgnoreCase(unit) || "克".equals(unit)) {
			BigDecimal b = new BigDecimal(weight);
			return b.divide(new BigDecimal(1000)).toString();
		} else if ("T".equalsIgnoreCase(unit) || "吨".equals(unit)) {
			BigDecimal b = new BigDecimal(weight);
			return b.multiply(new BigDecimal(1000)).toString();
		}
		return weight;
	}

	public static double getVolume(double l, double w, double h) {
		double result = l * w * h;
		result /= 6000.0;
		BigDecimal b = new BigDecimal(result);
		double f1 = b.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		return f1;
	}

	public static String getFormateData(Date date, String format) {
		SimpleDateFormat f = new SimpleDateFormat(format);
		return f.format(date);
	}

}
