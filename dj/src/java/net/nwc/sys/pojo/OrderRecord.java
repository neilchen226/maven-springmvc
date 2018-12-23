package net.nwc.sys.pojo;

public class OrderRecord {
    private Integer serialid;

    private Integer orderid;

    private String editor;

    private String editdate;

    private String note;

    public Integer getSerialid() {
        return serialid;
    }

    public void setSerialid(Integer serialid) {
        this.serialid = serialid;
    }

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
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
}