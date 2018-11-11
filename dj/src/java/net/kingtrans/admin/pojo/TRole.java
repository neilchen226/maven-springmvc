package net.kingtrans.admin.pojo;

public class TRole {
	private String roleid;// 用户组编码

	private String rolename;// 用户组名称

	private String roledesc;// 用户组说明

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	public String getRoledesc() {
		return roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	public TRole() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TRole(String roleid, String rolename, String roledesc) {
		super();
		this.roleid = roleid;
		this.rolename = rolename;
		this.roledesc = roledesc;
	}

}