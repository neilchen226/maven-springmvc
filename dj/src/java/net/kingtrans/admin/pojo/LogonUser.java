package net.kingtrans.admin.pojo;

public class LogonUser {

	private String username;// 用户名
	private String password;// 密码
	private String isvalidate;// 是否需要填验证码
	private String valicode; // 验证码

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIsvalidate() {
		return isvalidate;
	}

	public void setIsvalidate(String isvalidate) {
		this.isvalidate = isvalidate;
	}

	public String getValicode() {
		return valicode;
	}

	public void setValicode(String valicode) {
		this.valicode = valicode;
	}

}
