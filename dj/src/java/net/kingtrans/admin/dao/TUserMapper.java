package net.kingtrans.admin.dao;

import java.util.List;
import java.util.Map;

import net.kingtrans.admin.pojo.TUser;

import org.apache.ibatis.annotations.Select;

public interface TUserMapper {
    int deleteByPrimaryKey(String userid);

    int insert(TUser record);

    int insertSelective(TUser record);

    TUser selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(TUser record);

    int updateByPrimaryKey(TUser record);

   	int getUserCountsByMap(Map<String, Object> searchMap);
	List<Map<String, Object>> getUsersByMap(Map<String, Object> searchMap);

	@Select("select * from t_user")
	List<TUser> selectAll();

	List<TUser> getTUsersByMap(Map<String, Object> searchMap);
	
}