package net.kingtrans.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import net.kingtrans.admin.pojo.TModule;

public interface TModuleMapper {

    int insert(TModule record);

	int insertList(List<TModule> list);
	
	int deleteAll();
	
	@Select("select * from t_module order by moduleid, pos")
	List<TModule> selectAll();
    
}