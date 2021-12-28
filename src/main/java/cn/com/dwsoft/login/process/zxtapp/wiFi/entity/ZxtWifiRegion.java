package cn.com.dwsoft.login.process.zxtapp.wiFi.entity;

import cn.com.dwsoft.login.process.zxtapp.wiFi.entity.ZxtWifiDetailed;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author haider
 * @date 2021年07月15日 16:37
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("wifi 地域")
public class ZxtWifiRegion {
    @TableId
    private String id;

    @ApiModelProperty("地域名称")
    private String name;

    @ApiModelProperty("解析时间")
    private String parserTime;

    private Date createTime;

    @TableField(exist = false)
    @ApiModelProperty("解析明细")
    private List<ZxtWifiDetailed> detailedList;
}
