package cn.com.dwsoft.login.process.login.pojo;

import cn.com.dwsoft.login.util.DateUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.vdurmont.emoji.EmojiParser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * UMS_USER_EXTEND
 */
@Data
@ApiModel("")
public class UmsUserExtend implements Serializable {
    @TableId
    private String extendId;

    /**
     * 用户id
     */
    @ApiModelProperty(name = "userId",value = "用户 Id",example = "用户【userId】")
    private String userId;

    /**
     * 手机号
     */
    @ApiModelProperty(name = "phone",value = "手机号",example = "用户【phone】")
    private String phone;

    /**
     * 支付宝号
     */
    @ApiModelProperty(name = "loginAlipay",value = "支付宝号",example = "支付宝【birthTime】")
    private String loginAlipay;

    /**
     * 出生年月日
     */
    @ApiModelProperty(name = "birthTime",value = "birthTime",example = "微信【birthTime】")
    @JsonFormat(pattern = DateUtil.FULL_DATE_TO_THE_DAY)
    private Date birthTime;

    /**
     * 用的手机号 imei
     */
    @ApiModelProperty(name = "phoneImei",value = "用的手机号 imei",example = "手机【phoneImei】")
    private String phoneImei;

    /**
     * 用的手机号 手机型号
     */
    @ApiModelProperty(name = "phoneModel",value = "用的手机号 手机型号",example = "手机【phoneModel】")
    private String phoneModel;

    /**
     * 用的手机号 IMSI
     */
    @ApiModelProperty(name = "phoneImsi",value = "用的手机号 IMSI",example = "手机【phoneImsi】")
    private String phoneImsi;

    /**
     * 手机mac地址
     */
    @ApiModelProperty(name = "phoneMac",value = "手机mac地址",example = "手机【phoneMac】")
    private String phoneMac;

    /**
     * 手机系统版本号
     */
    @ApiModelProperty(name = "phoneSysVer",value = "手机系统版本号",example = "手机【phoneSysVer】")
    private String phoneSysVer;

    /**
     * 本地APK的版本
     */
    @ApiModelProperty(name = "localApk",value = "本地APK的版本",example = "程序【localApk】")
    private String localApk;

    /**
     * 手机厂商
     */
    @ApiModelProperty(name = "phoneFacture",value = "手机厂商",example = "手机【phoneFacture】")
    private String phoneFacture;

    /**
     * 微信 主键
     */
    @ApiModelProperty(name = "unionId",value = "微信 主键",example = "微信【unionId】")
    private String unionId;

    /**
     * 微信 openId
     */
    @ApiModelProperty(name = "openId",value = "微信 openId",example = "微信【openId】")
    private String openId;

    /**
     * 名称
     */
    @ApiModelProperty(name = "screenName",value = "名称",example = "微信【名称】")
    private String screenName;

    /**
     * 男女
     */
    @ApiModelProperty(name = "gender",value = "男女",example = "微信【男、女】")
    private String gender;

    /**
     * 微信头像地址
     */
    @ApiModelProperty(name = "profileImageUrl",value = "微信头像地址",example = "微信")
    private String profileImageUrl;

    public String getScreenName() {
        return StringUtils.isBlank(screenName) ? screenName : EmojiParser.parseToHtmlDecimal(screenName);
    }
}
