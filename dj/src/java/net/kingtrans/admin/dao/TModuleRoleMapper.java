package net.kingtrans.admin.dao;

import java.util.List;
import java.util.Set;

import net.kingtrans.admin.pojo.TModuleRole;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface TModuleRoleMapper {
	int insert(TModuleRole record);

	int initAdminRole();

    int deleteByRoleid(String roleid);
    
    int deleteByRoleids(@Param("roleid") String[] roleid);

    @Select("select distinct moduleid from t_module_role where roleid =#{roleid}")
    Set<String> getRoleByRoleid(String roleid);

	int insertList(List<TModuleRole> mrlist);

    
}