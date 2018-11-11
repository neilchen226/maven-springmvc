package net.kingtrans.admin.Enum;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum TLoggerTypeEnum {
	logon(10, "登录"), //
	changePass(20, "修改密码"), //
	logonout(0, "退出登录"), //
	init(-1, "系统初始化"), //
	;

	public static List<Map<String, Object>> getList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		for (TLoggerTypeEnum e : TLoggerTypeEnum.values()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("code", e.getCode());
			map.put("name", e.getName());
			list.add(map);
		}
		return list;
	}

	public static String getNameByCode(int code) {
		for (TLoggerTypeEnum e : TLoggerTypeEnum.values()) {
			if (e.getCode() == code)
				return e.getName();
		}
		return "";
	}

	private TLoggerTypeEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	private int code;
	private String name;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
