package cn.com.dwsoft.login.process.login.controller;

import cn.com.dwsoft.authority.pojo.User;
import cn.com.dwsoft.authority.util.FileUtil;
import cn.com.dwsoft.common.utils.DwBeanUtil;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import cn.com.dwsoft.login.config.LoginVariableProperties;
import cn.com.dwsoft.login.process.common.controller.DwsoftControllerSupport;
import cn.com.dwsoft.login.process.login.pojo.UmsImagePath;
import cn.com.dwsoft.login.process.login.pojo.UmsImagePathHis;
import cn.com.dwsoft.login.process.login.pojo.UmsUserExtend;
import cn.com.dwsoft.login.process.login.pojo.UmsUserExtendImpl;
import cn.com.dwsoft.login.process.login.service.UmsImagePathHisImpl;
import cn.com.dwsoft.login.process.login.service.UmsImagePathImpl;
import cn.com.dwsoft.login.process.zxtapp.util.AppUserUtil;
import cn.com.dwsoft.login.process.zxtapp.util.Result;
import cn.com.dwsoft.login.util.SaveFileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
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
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping(value = "/head")
@Api(tags = "用户头像")
@Slf4j
public class HeadImage extends DwsoftControllerSupport {
    @Autowired
    private UmsImagePathImpl umsImagePath;

    @Autowired
    private UmsImagePathHisImpl umsImagePathHis;
    @Autowired
    private LoginVariableProperties properties;
    @Autowired
    private SaveFileUtil saveFileUtil;
    @Autowired
    private UmsUserExtendImpl userExtendImpl;

    public String parserHeadImage(String image){
        return image.replaceAll("\\$\\{dw-public}",properties.getDwPublic());
    }

    /**
     * <p> 保存用户头像
     * @author Haidar
     * @date 2020/12/10 18:36
     **/
    @PostMapping("/saveHeadImage")
    @ResponseBody
    @ApiOperation(value = "保存图片")
    @ApiParam(name = "file",value = "图片")
    public Result saveHeadImage(MultipartFile[] file){
        User user = getUser();
        try {
            String onlyCode = AppUserUtil.getOnlyCode(user);
            List<UmsImagePath> list = umsImagePath.query().eq("CODE", onlyCode).list();
            List<UmsImagePath> save = saveFileUtil.save(file, false);
            List<UmsUserExtend> extendList = userExtendImpl.query().eq("USER_ID", onlyCode).list();
            if (extendList != null && !extendList.isEmpty()){
                // TODO
            }
            if (!list.isEmpty()){//第二次进来后保存图片
                int size = umsImagePathHis.query().eq("CODE", onlyCode).list().size();
                UmsImagePath data = list.get(0);// 老的数据
                UmsImagePathHis toData = new UmsImagePathHis();
                try {
                    DwBeanUtil.copyBean2Bean(data,toData);
                    toData.setVersion(size+1);
                    umsImagePathHis.save(toData);//保存历史数据
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                umsImagePath.removeById(data.getId());
            }
            for (UmsImagePath imagePath : save) {
                imagePath.setCode(onlyCode);
                imagePath.setDocumentPath(LoginProcessCondition.HEAD_IMAGE_BEFORE+imagePath.getDocumentPath());
            }
            umsImagePath.saveBatch(save);
            HashMap<String, String> map = new HashMap<>();
            map.put("imagePath",parserHeadImage(save.get(0).getDocumentPath()));
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
        if (StringUtils.isNotBlank(imagePath)){
            saveFileUtil.readImage(response,imagePath,400,400);
        }else {
            User user = getUser();
            try {
                String onlyCode = AppUserUtil.getOnlyCode(user);
                List<UmsImagePath> image = umsImagePath.query().eq("CODE", onlyCode).list();
                if (null != image && !image.isEmpty()){
                    imagePath = image.get(0).getDocumentPath();
                    imagePath.replaceAll("\\$\\{dw-public\\}/head/getHeadImage\\?imagePath=","");
                    saveFileUtil.readImage(response,imagePath,400,400);
                }else {
                    int sex = user.getSex();
                    if (sex == 1){
                        getBaseHeadImage(response,"3");
                    }else {
                        getBaseHeadImage(response,"4");
                    }
                }
            } catch (Exception e) {
                log.error("不能正常处理逻辑,请查看 RestHeadImage.getHeadImage() 获取头像异常");
                try {
                    getBaseHeadImage(response,"3");
                } catch (Exception fileNotFoundException) {

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
