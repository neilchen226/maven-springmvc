package net.kingtrans.admin.cache;

import net.kingtrans.admin.dao.TUserMapper;
import net.kingtrans.admin.pojo.TUser;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class TUserCache {

	@Autowired
	TUserMapper tUserMapper;

	private static Logger logger = Logger.getLogger(TUserCache.class);

	// Cacheable其结果进行缓存,每次先到tuserCache缓存中去取这个key,有则直接返回，无则调用该方法获取并缓存
	@Cacheable(value = { "tUserCache" }, key = "'tUserCache_'+#userid")
	public TUser getTUser(String userid) {
		if (userid == null || userid.trim().length() == 0) {
			return null;
		}
		if (logger.isDebugEnabled()) {
			logger.debug("取数据库数据：【" + userid + "】");
		}
		return tUserMapper.selectByPrimaryKey(userid);
	}

	// 清空 accountCache 缓存
	// allEntries 为true则清除整个tUserCache下的缓存，为false则仅清除 ,默认 false
	// beforeInvocation 为true则方法执行前清除，为false则当方法执行完毕清除（未正常执行不清除）
	@CacheEvict(value = "tUserCache", key = "'tUserCache_'+#userid", allEntries = false, beforeInvocation = false)
	public void clear(String userid) {
	}

	// CachePut每次都会触发真实方法的调用,并放入缓存
	@CachePut(value = { "tUserCache" }, key = "'tUserCache_'+#user.userid")
	public TUser reload(TUser user) {
		logger.debug("刷新缓存数据：【" + user.getUserid() + "】");
		return user;
	}

}
