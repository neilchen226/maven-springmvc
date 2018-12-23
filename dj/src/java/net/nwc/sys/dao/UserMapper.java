package net.nwc.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;

import net.nwc.sys.pojo.User;

public interface UserMapper {
    int deleteByPrimaryKey(String userid);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(String userid);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

	int geUserCountsByMap(Map<String, Object> searchMap);

	List<User> geUsersByMap(Map<String, Object> searchMap);

	User selectByUsernumber(String username);

	@Select("select * from suser where iswork = 1 order by usernumber ")
	List<User> getAllUsers();

	@Select("select * from suser where iswork = 1 and  jobname = 'drawer' order by usernumber ")
	List<User> getDrawer();
}