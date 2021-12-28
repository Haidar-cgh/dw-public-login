package cn.com.dwsoft.login.process.zxtapp.wiFi.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author haider
 * @date 2021年07月15日 16:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("wifi 明细")
public class ZxtWifiDetailed {
    @TableId
    private String id;

    private String regionId;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("下载速度")
    private String downSpeed;

    @ApiModelProperty("上传速度")
    private String uploadSpeed;

    @ApiModelProperty("信号强度")
    private String signalIntensity;

    @ApiModelProperty("丢包率")
    private String lossRate;

    @ApiModelProperty("延迟")
    private String delay;

    @ApiModelProperty("解析时间")
    private String parserTime;

    private Date createTime;
}
