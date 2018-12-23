package net.nwc.sys.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import net.nwc.sys.pojo.Order;

public interface OrderMapper {
	int deleteByPrimaryKey(Integer orderid);

	int insert(Order record);

	int insertSelective(Order record);

	Order selectByPrimaryKey(Integer orderid);

	int updateByPrimaryKeySelective(Order record);

	int updateByPrimaryKey(Order record);

	int getOrderCountsByMap(Map<String, Object> searchMap);

	List<Order> getOrderByMap(Map<String, Object> searchMap);

	@Select("select count(1) from sorder where isdelete = 1 ")
	int getDeleteOrderCount();
	
	@Update("update sorder set isdelete = 2 where isdelete = 1 ")
	int updateDeleteStatus();

	Map<String, Object> getOrderSum(Map<String, Object> searchMap);
}