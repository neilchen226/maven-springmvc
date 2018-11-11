package net.kingtrans.admin.pojo;

public class TModuleRole {
	private String roleid;// 权限ID

	private String moduleid;// 模块编码

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public TModuleRole(String roleid, String moduleid) {
		super();
		this.roleid = roleid;
		this.moduleid = moduleid;
	}

	public TModuleRole() {
		super();
	}

}