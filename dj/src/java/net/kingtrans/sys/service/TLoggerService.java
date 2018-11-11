package net.kingtrans.sys.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.kingtrans.admin.Enum.TLoggerTypeEnum;
import net.kingtrans.admin.dao.TLoggerMapper;
import net.kingtrans.admin.pojo.TLogger;
import net.kingtrans.admin.pojo.TUser;
import net.kingtrans.admin.service.TUserService;
import net.kingtrans.util.Pagination;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

@Service
public class TLoggerService {

	@Autowired
	TLoggerMapper tLoggerMapper;
	@Autowired
	TUserService tUserService;
	

	public ModelAndView loglist() {
		ModelAndView model = new ModelAndView();
		model.addObject("logtypes", TLoggerTypeEnum.getList());
		model.setViewName("/sys/tlog/loglist");
		return model;
	}

	@SuppressWarnings("unchecked")
	public String loglist(Pagination page, TLogger tLogger, String starttime, String endtime) {
		JSONObject json = new JSONObject();
		page.init("editdate", 0);
		Map<String, Object> searchMap = new HashMap<String, Object>();
		if (starttime != null) {
			searchMap.put("createtime_start", starttime + " 00:00:00");
		}
		if (endtime != null) {
			searchMap.put("createtime_end", endtime + " 23:59:59");
		}
		searchMap.putAll(new BeanMap(tLogger));
		searchMap.putAll(new BeanMap(page));
		int count = tLoggerMapper.getCountsByMap(searchMap);
		JSONArray array = null;
		if (count > 0) {
			List<Map<String, Object>> datas = tLoggerMapper.getDatasByMap(searchMap);
			for (Map<String, Object> map : datas) {
				map.put("editdate", (map.get("editdate").toString()).substring(0, 19));
				map.put("logtype", TLoggerTypeEnum.getNameByCode(Integer.parseInt(""+map.get("logtype"))));
				TUser user = tUserService.selectByPrimaryKey((map.get("userid") + ""));
				if (user != null) {
					map.put("userid", user.getUsername());
				}
			}
			array = JSONArray.fromObject(datas);
		}
		json.put("data", array);
		json.put("count", count);
		json.put("code", 0);
		json.put("msg", "");
		return json.toString();
	}

}
