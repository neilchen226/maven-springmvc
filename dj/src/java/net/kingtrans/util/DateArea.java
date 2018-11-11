package net.kingtrans.util;

public class DateArea {
	String starttime;
	String endtime;
	String timeinfo;

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getTimeinfo() {
		return timeinfo;
	}

	public void setTimeinfo(String timeinfo) {
		this.timeinfo = timeinfo;
	}

	public DateArea(String starttime, String endtime, String timeinfo) {
		super();
		this.starttime = starttime;
		this.endtime = endtime;
		this.timeinfo = timeinfo;
	}

}