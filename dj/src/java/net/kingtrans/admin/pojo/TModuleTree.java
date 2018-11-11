package net.kingtrans.admin.pojo;

import java.util.ArrayList;
import java.util.List;

public class TModuleTree extends TModule {
	private List<TModuleTree> children = new ArrayList<TModuleTree>();

	private boolean hasRole;

	public List<TModuleTree> getChildren() {
		return children;
	}

	public void setChildren(List<TModuleTree> children) {
		this.children = children;
	}

	public boolean isHasRole() {
		return hasRole;
	}

	public void setHasRole(boolean hasRole) {
		this.hasRole = hasRole;
	}

}
