package com.youngdatafan.dataintegration.file.management.mapper;

import com.youngdatafan.dataintegration.file.management.model.DmDemandFile;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DmDemandFileMapper {
    /**
     * delete by primary key.
     * @param id primaryKey
     * @return deleteCount
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * insert record to table.
     * @param record the record
     * @return insert count
     */
    int insert(DmDemandFile record);

    /**
     * insert record to table selective.
     * @param record the record
     * @return insert count
     */
    int insertSelective(DmDemandFile record);

    /**
     * select by primary key.
     * @param id primary key
     * @return object by primary key
     */
    DmDemandFile selectByPrimaryKey(Integer id);

    /**
     * update record selective.
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKeySelective(DmDemandFile record);

    /**
     * update record.
     * @param record the updated record
     * @return update count
     */
    int updateByPrimaryKey(DmDemandFile record);

    /**
     * 根据用户id与文件路径统计需求文件数量.
     * @param record 文件记录.
     * @return 查询到的需求文件.
     */
    DmDemandFile countByUserIdAndPath(DmDemandFile record);
}
