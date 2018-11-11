package net.kingtrans.admin.Enum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum ExcelRightTypeEnum {

	收货统计("SHTJ", "收货统计"), //
	货量时效分析统计("HLSXFXTJ", "货量时效分析统计"), //
	航空公司走货统计报表("HKGSZHTJ", "航空公司走货统计报表"), //
	航空公司年度统计报表("HKGSNDTJ", "航空公司年度统计报表"), //
	客户月结账单报表("KHYJZD", "客户月结账单报表"), //
	客户明细账单报表("KHMXZD", "客户明细账单报表"), //
	代理月结账单报表("DLYJZD", "代理月结账单报表"), //
	代理明细账单报表("DKMXZD", "代理明细账单报表"), //
	;

	private String code;
	private String name;

	private ExcelRightTypeEnum(String code, String name) {
		this.code = code;
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static List<Map<String, String>> getList() {
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		for (ExcelRightTypeEnum e : ExcelRightTypeEnum.values()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", e.getCode());
			map.put("name", e.getName());
			list.add(map);
		}
		return list;
	}

	public static String getNameByCode(String code) {
		for (ExcelRightTypeEnum e : ExcelRightTypeEnum.values()) {
			if (e.getCode().equalsIgnoreCase(code)) {
				return e.getName();
			}
		}
		return "";
	}

}
