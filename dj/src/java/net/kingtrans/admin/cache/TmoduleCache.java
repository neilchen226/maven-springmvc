package net.kingtrans.admin.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.kingtrans.admin.dao.TModuleMapper;
import net.kingtrans.admin.pojo.TModule;
import net.kingtrans.admin.pojo.TModuleTree;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TmoduleCache {

	@Autowired
	TModuleMapper tmoduleMapper;

	private static Logger logger = Logger.getLogger(TmoduleCache.class);

	public JSONArray getUserModuleTree(List<TModule> list, Set<String> roles, String parentid) {
		JSONArray array = new JSONArray();
		for (TModule tm : list) {
			if (parentid.equals(tm.getParentid()) && roles.contains(tm.getModuleid())) {
				JSONObject json = new JSONObject();
				json.put("href", tm.getHref());
				json.put("icon", tm.getIcon());
				json.put("title", tm.getTitle());
				json.put("spread", tm.getSpread() == 0 ? false : true);
				json.put("isleaf", tm.getIsleaf());
				JSONArray cjson = getUserModuleTree(list, roles, tm.getModuleid());
				if (tm.getIsleaf() == 0 && cjson.size() == 0)// 不为叶子节点且无孩子节点，跳过
					continue;
				if (cjson.size() > 0) {
					json.put("children", cjson);
				}
				// 添加菜单节点
				array.add(json);
			}
		}
		return array;
	}

	public List<TModuleTree> getModuleTree(List<TModule> list, Set<String> roles, String parentid) {
		List<TModuleTree> tree = new ArrayList<TModuleTree>();
		for (TModule tm : list) {
			if (parentid.equals(tm.getParentid())) {
				TModuleTree t = new TModuleTree();
				t.setModuleid(tm.getModuleid());
				t.setTitle(tm.getTitle());
				t.setHasRole(roles.contains(tm.getModuleid()) == true ? true : false);
				List<TModuleTree> childTree = getModuleTree(list, roles, tm.getModuleid());
				t.setChildren(childTree);
				tree.add(t);
			}
		}
		return tree;
	}

	// Cacheable其结果进行缓存
	@Cacheable(value = { "tmoduleCache" }, key = "#root.targetClass")
	public List<TModule> getTModules() {
		return getDBTModules();
	}

	// 清空 accountCache 缓存
	// allEntries 为true则清除整个default下的缓存，为false则仅清除 ,默认 false
	// beforeInvocation 为true则一定清除，为false则当方异常时不清除,默认 为false
	@CacheEvict(value = "tmoduleCache", key = "#root.targetClass", allEntries = false, beforeInvocation = false)
	public void refreshTModules() {
	}

	// CachePut每次都会触发真实方法的调用
	@CachePut(value = { "tmoduleCache" }, key = "#root.targetClass")
	public List<TModule> reloadTModules() {
		return getDBTModules();
	}

	// 从数据中取模块信息
	private List<TModule> getDBTModules() {
		logger.info("从数据库读取系统所有模块信息");
		return tmoduleMapper.selectAll();
	}

}
