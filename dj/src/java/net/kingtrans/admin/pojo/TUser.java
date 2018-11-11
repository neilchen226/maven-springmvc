package net.kingtrans.admin.pojo;

public class TUser {
	private String userid;// 登录名

	private String password;// 密码

	private String username;// 用户名

	private String userFace;// 用户头像

	private String roleid;// 用户组

	public TUser() {
		super();
	}

	public TUser(String userid, String password, String username, String userFace, String roleid) {
		super();
		this.userid = userid;
		this.password = password;
		this.username = username;
		this.userFace = userFace;
		this.roleid = roleid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserFace() {
		return userFace;
	}

	public void setUserFace(String userFace) {
		this.userFace = userFace;
	}

	public String getRoleid() {
		return roleid;
	}

	public void setRoleid(String roleid) {
		this.roleid = roleid;
	}

}