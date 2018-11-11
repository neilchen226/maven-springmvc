package net.kingtrans.util;

/**
 * @Description 分页使用字段
 * @author Cheng
 */
public class Pagination {

	public String sortfield;
	public String sorttype;

	public Integer page;// 当前查询页数
	public Integer limit;// 每页记录数

	public Integer searchPosition;// 搜索开始位置
	public Integer resultNum;// 记录总数数
	public Integer resultPage; // 记录页数

	public Pagination() {
		this.page = 1;
		this.limit = 20;// 默认二十
	}

	public void init(String sortfield, Integer resultNum) {
		if (this.sortfield == null && sortfield != null) {
			this.sortfield = sortfield;
			this.sorttype = "desc";
		}
		this.searchPosition = (this.page - 1) * limit;
		this.resultNum = resultNum;
		this.resultPage = (int) Math.ceil(1.0*resultNum / limit);
	}

	public String getSortfield() {
		return sortfield;
	}

	public void setSortfield(String sortfield) {
		this.sortfield = sortfield;
	}

	public String getSorttype() {
		return sorttype;
	}

	public void setSorttype(String sorttype) {
		this.sorttype = sorttype;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getSearchPosition() {
		return searchPosition;
	}

	public void setSearchPosition(Integer searchPosition) {
		this.searchPosition = searchPosition;
	}

	public Integer getResultNum() {
		return resultNum;
	}

	public void setResultNum(Integer resultNum) {
		this.resultNum = resultNum;
	}

	public Integer getResultPage() {
		return resultPage;
	}

	public void setResultPage(Integer resultPage) {
		this.resultPage = resultPage;
	}

	@Override
	public String toString() {
		return "Pagination [sortfield=" + sortfield + ", sorttype=" + sorttype + ", page=" + page + ", limit=" + limit
				+ ", searchPosition=" + searchPosition + ", resultNum=" + resultNum + ", resultPage=" + resultPage
				+ "]";
	}

}
