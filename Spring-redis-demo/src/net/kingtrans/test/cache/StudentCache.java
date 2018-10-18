package net.kingtrans.test.cache;

import net.kingtrans.test.dao.StudentMapper;
import net.kingtrans.test.pojo.Student;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class StudentCache {
	private static final String cachename = "studentCache";

	@Autowired
	StudentMapper mapper;

	private static Logger logger = Logger.getLogger(StudentCache.class);

	// Cacheable其结果进行缓存,每次先到tuserCache缓存中去取这个key,有则直接返回，无则调用该方法获取并缓存
	@Cacheable(value = { cachename }, key = cachename + "'_'+#id")
	public Student getStudent(int id) {
		System.out.println("取数据库数据：【" + id + "】");
		return this.mapper.selectByPrimaryKey(id);
	}

	// 清空 accountCache 缓存
	// allEntries 为true则清除整个tUserCache下的缓存，为false则仅清除 ,默认 false
	// beforeInvocation 为true则方法执行前清除，为false则当方法执行完毕清除（未正常执行不清除）
	@CacheEvict(value = cachename, key = cachename + "'_'+#id", allEntries = false, beforeInvocation = false)
	public void clear(int id) {
	}

	// CachePut每次都会触发真实方法的调用,并放入缓存
	@CachePut(value = { cachename }, key = cachename + "'_'+#user.id")
	public Student reload(Student user) {
		logger.debug("刷新缓存数据：【" + user.getId() + "】");
		return user;
	}

	@CacheEvict(value = cachename, allEntries = true, beforeInvocation = false)
	public void clearAll() {

	}

}
