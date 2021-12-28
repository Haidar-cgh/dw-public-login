package cn.com.dwsoft.login.process.zxtapp.task.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author tlk
 * @date 2021/2/4-14:01
 */
@Data
@TableName("zxt_game_info")
public class GameInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("主键id")
    private Long id;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mdn;
    /**
     * 获奖级别
     */
    @ApiModelProperty("获奖级别")
    private String level;
    /**
     * 奖励类型
     */
    @ApiModelProperty("奖励金币或者银币1：银币，2：金币")
    private String coin;
    /**
     * 奖励数量
     */
    @ApiModelProperty("奖励数量")
    private String  counts;
    /**
     * 日期
     */
    @ApiModelProperty("日期")
    private String optionDate;
    /**
     *游戏类别
     */
    @ApiModelProperty("游戏类别 1：砸银蛋，2：砸金蛋")
    private String gameStype;
}
