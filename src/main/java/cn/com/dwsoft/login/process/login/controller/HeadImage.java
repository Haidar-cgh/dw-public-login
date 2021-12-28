package cn.com.dwsoft.login.process.login.controller;

import cn.com.dwsoft.authority.exception.LoginOutException;
import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.authority.util.FileUtil;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import cn.com.dwsoft.login.config.LoginVariableProperties;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.login.pojo.UmsImagePath;
import cn.com.dwsoft.login.process.login.pojo.UmsImagePathHis;
import cn.com.dwsoft.login.process.login.service.UmsImagePathHisImpl;
import cn.com.dwsoft.login.process.login.service.UmsImagePathImpl;
import cn.com.dwsoft.login.process.zxtapp.util.AppUserUtil;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import cn.com.dwsoft.login.util.SnowFlake;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/head")
@Api(tags = "用户头像")
@Log4j2
public class HeadImage extends DwsoftControllerSupport {
    @Autowired
    private UmsImagePathImpl umsImagePath;

    @Autowired
    private UmsImagePathHisImpl umsImagePathHis;
    @Autowired
    private LoginVariableProperties properties;

    /**
     * <p> 保存用户头像
     * @author Haidar
     * @date 2020/12/10 18:36
     **/
    @PostMapping("/saveHeadImage")
    @ResponseBody
    @ApiOperation(value = "保存图片")
    @ApiParam(name = "file",value = "图片")
    public Result saveHeadImage(MultipartFile file){
        User user = getUser();
        try {
            String id = user.getId();
            String toPath = properties.getHeadImagePath() + File.separator;
/*            File filePath=new File(headImagePath);
            if(!filePath.exists()){
                filePath.mkdirs();
            }*/
            String imageUrl = FileUtil.saveFileAndGetUrl(file, toPath, id); // 保存图片路径
            String onlyCode = AppUserUtil.getOnlyCode(user);
            List<UmsImagePath> list = umsImagePath.query().eq("CODE", onlyCode).list();
            if (!list.isEmpty()){//第二次进来后保存图片
                int size = umsImagePathHis.query().eq("CODE", onlyCode).list().size();
                UmsImagePath pathData = list.get(0);// 老的数据
                UmsImagePathHis imagePathHis = new UmsImagePathHis();
                imagePathHis.setImagePath(pathData.getImagePath());
                imagePathHis.setId(pathData.getId());
                imagePathHis.setCode(pathData.getCode());
                imagePathHis.setCreateTime(pathData.getCreateTime());
                imagePathHis.setVersion(size+1);
                umsImagePathHis.save(imagePathHis);//保存历史数据
                umsImagePath.removeById(pathData.getId());
            }
            UmsImagePath imagePath = new UmsImagePath();
            imagePath.setId(SnowFlake.nextId(""));
            imagePath.setCode(onlyCode);
            imagePath.setImagePath(imageUrl);
            imagePath.setCreateTime(new Date());
            umsImagePath.save(imagePath);
            HashMap<String, String> map = new HashMap<>();
            map.put("imagePath",properties.getDwPublic()+"/head/getHeadImage?imagePath="+imageUrl);
            return Result.success(map);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failed("异常请联系管理员");
        }
    }

    @ResponseBody
    @RequestMapping("/getHeadImage")
    @ApiOperation(value = "获取图片")
    @ApiParam(name = "imagePath",value = "图片路径")
    public void getHeadImage(String imagePath,HttpServletResponse response){
        imagePath = properties.getHeadImagePath() + File.separator+imagePath;
        if (StringUtils.isNotBlank(imagePath)){
            FileUtil.getAndResizeImage(response,imagePath,400,400);
        }else {
            User user = getUser();
            try {
                if (StringUtils.isNotBlank(imagePath)){
                    FileUtil.getImage(response,imagePath,null);
                }else {
                    String onlyCode = AppUserUtil.getOnlyCode(user);
                    List<UmsImagePath> image = umsImagePath.query().eq("CODE", onlyCode).list();
                    if (null != image && !image.isEmpty()){
                        imagePath = image.get(0).getImagePath();
                        FileUtil.getImage(response,imagePath,null);
                    }else {
                        int sex = user.getSex();
                        if (sex == 1){
                            FileUtil.getAndResizeImage(response, ResourceUtils.getURL("classpath:").getPath() + File.separator + "image" + File.separator + LoginProcessCondition.MAN_IMAGE,400,400);
                        }else {
                            FileUtil.getAndResizeImage(response, ResourceUtils.getURL("classpath:").getPath() + File.separator + "image" + File.separator + LoginProcessCondition.WO_MAN_IMAGE,400,400);
                        }
                    }
                }
            } catch (Exception e) {
                log.error("不能正常处理逻辑,请查看 RestHeadImage.getHeadImage() 获取头像异常");
                try {
                    FileUtil.getImage(response, ResourceUtils.getURL("classpath:").getPath() + File.separator + "image" + File.separator + LoginProcessCondition.MAN_IMAGE,null);
                } catch (FileNotFoundException fileNotFoundException) {

                }
            }
        }
    }

    @ResponseBody
    @RequestMapping("/getBaseHeadImage")
    @ApiOperation(value = "获取基础图片")
    @ApiParam(name = "num",value = "图片数值【1-8】")
    public void getBaseHeadImage(HttpServletResponse response, String num ){
        try {
            FileUtil.getImage(response, ResourceUtils.getURL("classpath:").getPath() + File.separator + "image" + File.separator + num +".jpg",null);
        } catch (FileNotFoundException fileNotFoundException) {
            try {
                FileUtil.getImage(response, ResourceUtils.getURL("classpath:").getPath() + File.separator + "image" + File.separator  +"1.jpg",null);
            } catch (FileNotFoundException e) {

            }
        }
    }
}
