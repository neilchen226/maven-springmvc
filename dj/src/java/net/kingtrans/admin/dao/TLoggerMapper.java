package net.kingtrans.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import net.kingtrans.admin.pojo.TLogger;

public interface TLoggerMapper {
	int deleteByPrimaryKey(Integer serialid);

	int insert(TLogger record);

	int insertSelective(TLogger record);

	TLogger selectByPrimaryKey(Integer serialid);

	int updateByPrimaryKeySelective(TLogger record);

	int updateByPrimaryKey(TLogger record);
	
	

	int getCountsByMap(Map<String, Object> searchMap);
	
	List<Map<String, Object>> getDatasByMap(Map<String, Object> searchMap);
	
	
	 int deleteByPrimaryKeys(@Param("userid") String[] userid);
	 
	 TLogger selectByPrimaryKeys(String userid);
	 

}