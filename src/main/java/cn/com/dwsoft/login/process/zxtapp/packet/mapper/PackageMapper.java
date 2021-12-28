package cn.com.dwsoft.login.process.zxtapp.packet.mapper;

import cn.com.dwsoft.login.process.zxtapp.packet.pojo.PackageDO;
import cn.com.dwsoft.login.process.zxtapp.packet.pojo.SmsEstimateDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author sqw
 * @version 1.0
 * @description 套餐持久层
 * @ClassName IPacketMapper
 * @Date 2020/12/8
 * @since jdk1.8
 */
@Repository
public interface PackageMapper {


    /**
     *功能描述 省分和运营商查询
     * @author sqw
     * @param province 参数实体
     * @param operators 参数实体
     * @return java.util.List<cn.com.dwsoft.login.process.zxtapp.packet.pojo.PricePacketDO>
     * @date 2020/12/8
     */
    List<PackageDO> findByProOpr(@Param("province") String province, @Param("operators") String operators);
    /**
     *功能描述 通过条件获取套餐
     * @author sqw
     * @param obj 参数实体
     * @return java.util.List<cn.com.dwsoft.login.process.zxtapp.packet.pojo.PricePacketDO>
     * @date 2020/12/8
     */
    List<PackageDO> findByCondition(@Param("whereSql") String whereSql, @Param("obj") PackageDO obj);

    PackageDO findById(String id);

    /**
     *功能描述 
     * @author sqw
     * @param smsEstimateDO 预估值实体
     * @return int
     * @date 2020/12/11
     */
    int insertSmsEstimate(SmsEstimateDO smsEstimateDO);

    SmsEstimateDO getSmsEstimate(String mdn);
}
