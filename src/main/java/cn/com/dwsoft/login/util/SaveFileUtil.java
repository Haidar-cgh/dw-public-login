package cn.com.dwsoft.login.util;

import cn.com.dwsoft.authority.exception.ServiceException;
import cn.com.dwsoft.authority.util.FileUtil;
import cn.com.dwsoft.login.config.LoginProcessCondition;
import cn.com.dwsoft.login.config.LoginVariableProperties;
import cn.com.dwsoft.login.process.login.pojo.UmsImagePath;
import cn.com.dwsoft.login.util.ftp.FtpConfig;
import cn.com.dwsoft.login.util.ftp.FtpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author haider
 * @date 2021年12月22日 16:38
 */
@Configuration
@Slf4j
public class SaveFileUtil {
    @Autowired
    private LoginVariableProperties properties;

    @PostConstruct
    public void p(){
        log.info("properties {}",properties.getEnableFTP());
    }

    public List<UmsImagePath> save(MultipartFile[] file,Boolean isDocument) {
        List<UmsImagePath> documentExtends = new ArrayList<>();
        if (properties.getEnableFTP()){
            if (file != null) {
                FtpConfig config = new FtpConfig();
                config.setServer(properties.getFtpHost());
                config.setPort(properties.getFtpPort());
                config.setUsername(properties.getFtpUserName());
                config.setPassword(properties.getFtpPwd());
                config.setFtp_home_path(properties.getFtpServerPath()+File.separator +  LoginProcessCondition.BASE_URL + File.separator);
                FtpUtil ftpUtil = new FtpUtil();
                boolean connectSuccess = false;
                try {
                    connectSuccess = ftpUtil.connectServer(config);
                    for (int i = 0; i < file.length; i++) {
                        MultipartFile multipartFile = file[i];
                        UmsImagePath imagePath = new UmsImagePath();
                        imagePath.setId(SnowFlake.nextId(""));
                        imagePath.setDocumentName(multipartFile.getOriginalFilename());
                        imagePath.setIsDocument(String.valueOf(isDocument));
                        imagePath.setCreateTime(new Date());
                        String end = "." + imagePath.getDocumentName().split(FileUtil.FILE_NAME_SEPARATOR)[imagePath.getDocumentName().split(FileUtil.FILE_NAME_SEPARATOR).length - 1];
                        String reName = File.separator + imagePath.getId() + end;
                        ftpUtil.upload(reName, multipartFile.getInputStream());
                        imagePath.setIsImage(String.valueOf(FileUtil.isImage(multipartFile.getInputStream())));
                        imagePath.setDocumentPath(config.getFtp_home_path()+reName);
                        documentExtends.add(imagePath);
                    }
                } catch (Exception e) {
                    throw new ServiceException(e.getMessage());
                }finally {
                    if (connectSuccess){
                        try {
                            ftpUtil.disconnect();
                        } catch (IOException e) {
                            log.error("关闭 ftp 异常 {}",e.getMessage());
                        }
                    }
                }

            }
        }else {
            if (file != null) {
                for (int i = 0; i < file.length; i++) {
                    MultipartFile multipartFile = file[i];
                    UmsImagePath imagePath = new UmsImagePath();
                    imagePath.setId(SnowFlake.nextId(""));
                    imagePath.setDocumentName(multipartFile.getOriginalFilename());
                    imagePath.setIsDocument(String.valueOf(isDocument));
                    imagePath.setCreateTime(new Date());
                    String path = properties.getFilePath().replaceAll(FileUtil.REG_END_SEPARATOR, "") + File.separator;
                    try {
                        String saveUrl = FileUtil.saveFileAndGetUrl(multipartFile, path,imagePath.getId());
                        imagePath.setIsImage(String.valueOf(FileUtil.isImage(saveUrl)));
                        imagePath.setDocumentPath(saveUrl);
                        documentExtends.add(imagePath);
                    } catch (Exception e) {
                        throw new ServiceException("保存文件异常 " + e.getMessage());
                    }
                }
            }
        }
        return documentExtends;
    }

    public void down(HttpServletResponse response, String documentPath, String documentName) {
        if (properties.getEnableFTP()){
            FtpConfig config = new FtpConfig();
            config.setServer(properties.getFtpHost());
            config.setPort(properties.getFtpPort());
            config.setUsername(properties.getFtpUserName());
            config.setPassword(properties.getFtpPwd());
            config.setFtp_home_path(properties.getFtpServerPath()+File.separator + LoginProcessCondition.BASE_URL + File.separator);
            FtpUtil ftpUtil = new FtpUtil();
            boolean connectSuccess = false;
            try {
                connectSuccess = ftpUtil.connectServer(config);
                ftpUtil.download(response,documentPath,documentName);
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }finally {
                if (connectSuccess){
                    try {
                        ftpUtil.disconnect();
                    } catch (IOException e) {
                        log.error("关闭 ftp 异常 {}",e.getMessage());
                    }
                }
            }

        }else {
            FileUtil.download(response,documentPath,documentName);
        }
    }

    public void del(String path) {
        if (properties.getEnableFTP()){
            FtpConfig config = new FtpConfig();
            config.setServer(properties.getFtpHost());
            config.setPort(properties.getFtpPort());
            config.setUsername(properties.getFtpUserName());
            config.setPassword(properties.getFtpPwd());
            config.setFtp_home_path(properties.getFtpServerPath()+ File.separator + LoginProcessCondition.BASE_URL + File.separator);
            FtpUtil ftpUtil = new FtpUtil();
            boolean connectSuccess = false;
            try {
                connectSuccess = ftpUtil.connectServer(config);
                ftpUtil.getFtpClient().dele(path);
                ftpUtil.getFtpClient().deleteFile(path);
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }finally {
                if (connectSuccess){
                    try {
                        ftpUtil.disconnect();
                    } catch (IOException e) {
                        log.error("关闭 ftp 异常 {}",e.getMessage());
                    }
                }
            }

        }else {
            FileUtil.deleteFile(path,false);
        }
    }

    public void readImage(HttpServletResponse response,String path,Integer with,Integer height) {
        if (properties.getEnableFTP()){
            response.setContentType("image/gif");
            FtpConfig config = new FtpConfig();
            config.setServer(properties.getFtpHost());
            config.setPort(properties.getFtpPort());
            config.setUsername(properties.getFtpUserName());
            config.setPassword(properties.getFtpPwd());
            config.setFtp_home_path(properties.getFtpServerPath()+ File.separator + LoginProcessCondition.BASE_URL + File.separator);
            FtpUtil ftpUtil = new FtpUtil();
            boolean connectSuccess = false;
            try {
                connectSuccess = ftpUtil.connectServer(config);
                InputStream fis = ftpUtil.getFileInPutStream(path);
                OutputStream out = response.getOutputStream();
                String end = path.split(FileUtil.FILE_NAME_SEPARATOR)[path.split(FileUtil.FILE_NAME_SEPARATOR).length - 1];
                FileUtil.getAndResizeImage(fis,out,with,height,end);
            } catch (Exception e) {
                throw new ServiceException(e.getMessage());
            }finally {
                if (connectSuccess){
                    try {
                        ftpUtil.disconnect();
                    } catch (IOException e) {
                        log.error("关闭 ftp 异常 {}",e.getMessage());
                    }
                }
            }

        }else {
            FileUtil.getAndResizeImage(response,path,400,400);
        }
    }
}
