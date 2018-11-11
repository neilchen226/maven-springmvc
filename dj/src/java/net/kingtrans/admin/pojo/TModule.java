package net.kingtrans.admin.pojo;

public class TModule {
	private String moduleid;// 模块编码

	private String title;// 标题

	private String icon;// 图标

	private String href;// 链接

	private Integer spread;// 是否默认展开

	private Integer isleaf;// 是否为叶子节点

	private String parentid;// 父模块编码

	private Integer pos;// 同级模块排序顺序

	public String getModuleid() {
		return moduleid;
	}

	public void setModuleid(String moduleid) {
		this.moduleid = moduleid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public Integer getSpread() {
		return spread;
	}

	public void setSpread(Integer spread) {
		this.spread = spread;
	}

	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public Integer getPos() {
		return pos;
	}

	public void setPos(Integer pos) {
		this.pos = pos;
	}


	public TModule(String moduleid, String title, String icon, String href, Integer spread, Integer isleaf,
			String parentid, Integer pos) {
		super();
		this.moduleid = moduleid;
		this.title = title;
		this.icon = icon;
		this.href = href;
		this.spread = spread;
		this.isleaf = isleaf;
		this.parentid = parentid;
		this.pos = pos;
	}

	public TModule() {
		super();
	}

}