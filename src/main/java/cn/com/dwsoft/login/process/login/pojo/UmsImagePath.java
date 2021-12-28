package cn.com.dwsoft.login.process.login.pojo;

import cn.com.dwsoft.login.util.DateUtil;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * <p> UMS_IMAGE_PATH
 * 用户关联表 保存图片
 * @author Haidar
 * @date 2020/12/10 19:06
 **/
@Data
public class UmsImagePath {
    /**
     * 表格id
     */
    @TableId
    private String id;

    /**
     * 唯一标识
     */
    private String code;

    /**
     * 图片地址
     */
    private String imagePath;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = DateUtil.FULL_DATE_TO_THE_SECOND,timezone = "GMT+8")
    private Date createTime;
}
