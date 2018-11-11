package net.kingtrans.admin.service;

import java.util.List;
import java.util.Map;

import net.kingtrans.admin.cache.TUserCache;
import net.kingtrans.admin.dao.TUserMapper;
import net.kingtrans.admin.pojo.TUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TUserService {

	@Autowired
	TUserMapper tUserMapper;

	@Autowired
	TUserCache tUserCache;

	public int deleteByPrimaryKey(String userid) {
		int flag = tUserMapper.deleteByPrimaryKey(userid);// 删除
		if (flag > 0) {
			tUserCache.clear(userid);// 清除缓存
		}
		return flag;
	}

	public int insert(TUser record) {
		int flag = tUserMapper.insert(record);// 新增
		if (flag > 0) {
			tUserCache.reload(record);// 刷新缓存
		}
		return flag;
	}

	public int insertSelective(TUser record) {
		int flag = tUserMapper.insertSelective(record);// 新增
		if (flag > 0) {
			tUserCache.reload(record);// 刷新缓存
		}
		return flag;
	}

	public TUser selectByPrimaryKey(String userid) {
		return tUserCache.getTUser(userid);
	}

	public int updateByPrimaryKeySelective(TUser record) {
		int flag = tUserMapper.updateByPrimaryKeySelective(record);// 更新
		if (flag > 0) {
			tUserCache.reload(record);// 刷新缓存
		}
		return flag;

	}

	public int updateByPrimaryKey(TUser record) {
		int flag = tUserMapper.updateByPrimaryKey(record);// 更新
		if (flag > 0) {
			tUserCache.reload(record);// 刷新缓存
		}
		return flag;
	}


	public List<TUser> getTUsersByMap(Map<String, Object> searchMap) {
		return tUserMapper.getTUsersByMap(searchMap);
	}
}
