package net.nwc.sys.pojo;

public class WorkRecord {
    private Integer serialid;

    private String createdate;

    private String userid;

    private String fisrttime;

    private String endtime;

    public Integer getSerialid() {
        return serialid;
    }

    public void setSerialid(Integer serialid) {
        this.serialid = serialid;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getFisrttime() {
        return fisrttime;
    }

    public void setFisrttime(String fisrttime) {
        this.fisrttime = fisrttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}