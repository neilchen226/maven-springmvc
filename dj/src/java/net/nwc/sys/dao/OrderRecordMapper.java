package net.nwc.sys.dao;

import java.util.List;

import net.nwc.sys.pojo.OrderRecord;

public interface OrderRecordMapper {
	int deleteByOrderid(Integer orderid);

	int insert(OrderRecord record);

	OrderRecord selectByPrimaryKey(Integer serialid);

	List<OrderRecord> selectByOrderid(Integer orderid);

}