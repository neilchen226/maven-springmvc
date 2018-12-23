package net.nwc.sys.pojo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum UserJobEnum {
	
	admin("admin", "管理员"), drawer("drawer", "制图员"), servicer("servicer", "客服");

	private String code;
	private String name;

	private UserJobEnum(String code, String name) {
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
		for (UserJobEnum e : UserJobEnum.values()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("code", e.getCode());
			map.put("name", e.getName());
			list.add(map);
		}
		return list;
	}

	public static String getNameByCode(String code) {
		for (UserJobEnum e : UserJobEnum.values()) {
			if (e.getCode().equalsIgnoreCase(code)) {
				return e.getName();
			}
		}
		return "";
	}
}
