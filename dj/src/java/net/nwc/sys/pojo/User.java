package net.nwc.sys.pojo;

public class User {
    private String userid;

    private String pass;

    private String username;

    private String userface;

    private String usernumber;

    private String jobname;

    private Integer iswork;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserface() {
        return userface;
    }

    public void setUserface(String userface) {
        this.userface = userface;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getJobname() {
        return jobname;
    }

    public void setJobname(String jobname) {
        this.jobname = jobname;
    }

    public Integer getIswork() {
        return iswork;
    }

    public void setIswork(Integer iswork) {
        this.iswork = iswork;
    }
}