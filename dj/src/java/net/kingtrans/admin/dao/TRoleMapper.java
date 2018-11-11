package net.kingtrans.admin.dao;

import java.util.List;

import net.kingtrans.admin.pojo.TRole;

import org.apache.ibatis.annotations.Param;

public interface TRoleMapper {
	int insertSelective(TRole record);

	int deleteByPrimaryKey(String roleid);
	
	int deleteByPrimaryKeys(@Param("roleid") String[] roleid);

	TRole selectByPrimaryKey(String roleid);

	int updateByPrimaryKeySelective(TRole record);

	List<TRole> selectAll(TRole record);

	List<TRole> getUsingRole();

}