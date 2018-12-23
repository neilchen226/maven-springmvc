package net.nwc.sys.dao;

import java.util.List;
import java.util.Map;

import net.nwc.sys.pojo.WorkRecord;

public interface WorkRecordMapper {
    int insert(WorkRecord record);

    int insertSelective(WorkRecord record);
    
    List<WorkRecord> selectByMap(Map<String, Object> map);

	int updateSelective(WorkRecord wr);
}