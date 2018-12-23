package net.nwc.sys.pojo;

import java.math.BigDecimal;

public class Order {
    private Integer orderid;

    private String client;

    private String projectname;

    private String userid;

    private Integer vmap;

    private Integer smap;

    private BigDecimal cost;

    private BigDecimal precost;

    private BigDecimal paycost;

    private String payway;

    private String status;

    private String editor;

    private String editdate;

    private String checkdate;
    
    private String note;

    private Integer isdelete;

    private String deletedate;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Integer getVmap() {
        return vmap;
    }

    public void setVmap(Integer vmap) {
        this.vmap = vmap;
    }

    public Integer getSmap() {
        return smap;
    }

    public void setSmap(Integer smap) {
        this.smap = smap;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrecost() {
        return precost;
    }

    public void setPrecost(BigDecimal precost) {
        this.precost = precost;
    }

    public BigDecimal getPaycost() {
        return paycost;
    }

    public void setPaycost(BigDecimal paycost) {
        this.paycost = paycost;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
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

    public Integer getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    public String getDeletedate() {
        return deletedate;
    }

    public String getCheckdate() {
		return checkdate;
	}

	public void setCheckdate(String checkdate) {
		this.checkdate = checkdate;
	}

	public void setDeletedate(String deletedate) {
        this.deletedate = deletedate;
    }
}