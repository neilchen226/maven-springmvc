package net.kingtrans.admin.pojo;

public class TLogger {
	private Integer serialid;//序列号

	private String userid;//用户名

	private Integer logtype;//操作类型

	private String editdate;//操作日期

	private String note;//操作说明

	private String logip;//日志ip

	public Integer getSerialid() {
		return serialid;
	}

	public void setSerialid(Integer serialid) {
		this.serialid = serialid;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public Integer getLogtype() {
		return logtype;
	}

	public void setLogtype(Integer logtype) {
		this.logtype = logtype;
	}

	public String getEditdate() {
		return editdate;
	}

	public void setEditdate(String editdate) {
		this.editdate = editdate;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getLogip() {
		return logip;
	}

	public void setLogip(String logip) {
		this.logip = logip;
	}

	public TLogger(String userid, Integer logtype, String editdate, String note, String logip) {
		super();
		this.userid = userid;
		this.logtype = logtype;
		this.editdate = editdate;
		this.note = note;
		this.logip = logip;
	}

	public TLogger() {
		super();
	}

}