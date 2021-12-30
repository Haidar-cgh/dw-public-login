package cn.com.dwsoft.login.util.ftp;

import cn.com.dwsoft.authority.exception.ServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FtpUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FtpUtil.class);
	
	private FTPClient ftpClient;

	private String server ;

	private int port ;

	private String user ;

	private String password ;

	private String ftp_home_path ;

	private String data_encoder = "UTF-8";
       
	private int bufferSize = 1024;
       
	/**
    * 根路径为"/",如果需要链接服务器之后跳转到路径，则在path中定义
    * @param ftpConfig
    * @throws SocketException
    * @throws IOException
    * @throws Exception
    */
	public boolean connectServer(FtpConfig ftpConfig) throws SocketException, IOException, Exception {

		server = ftpConfig.getServer();

		port = ftpConfig.getPort();

		user = ftpConfig.getUsername();

		password = ftpConfig.getPassword();

//		ftp_home_path = this.dropPathSuffix(this.addPathPrefix(ftpConfig.getFtp_home_path()));
		ftp_home_path = ftpConfig.getFtp_home_path();

		data_encoder = ftpConfig.getData_encoder();
		bufferSize  = ftpConfig.getBufferSize();
              
		return this.connectServer(server, port, user, password, ftp_home_path,data_encoder);
	}

	/**
    * 连接ftp服务器
    * @param server 服务器ip
    * @param port 端口，通常为21
    * @param user 用户名
    * @param password 密码
    * @param ftp_home_path 进入服务器之后的默认路径
    * @return 连接成功返回true，否则返回false
    * @throws SocketException
    * @throws IOException
    * @throws Exception 
    */
	public boolean connectServer(String server, int port, String user,
                     String password, String ftp_home_path,String data_encoder) 
                    		 throws SocketException, IOException, Exception {

		ftpClient = new FTPClient();

		ftpClient.connect(server, port);

		ftpClient.setControlEncoding(data_encoder);

		logger.info("Connected to " + server + ".");

		logger.info("FTP server reply code:" + ftpClient.getReplyCode());

		if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {

         	if (ftpClient.login(user, password)) {

                if (ftp_home_path.length() != 0) {

                	boolean retVal = ftpClient.changeWorkingDirectory(ftp_home_path);

                	if(!retVal){
                		//创建
                		logger.info("--连接--ftp目录["+ftp_home_path+"]不存在.重建[The remote directory is not exist.Build.]:");
                  	  
          				// 建目录
          				logger.info("准备在ftp创建目录[To mkdir under the ftp home directory]:"+ftp_home_path);
          				ftpClient.makeDirectory(ftp_home_path);
          				
          				// 建好后,再测
          				retVal = ftpClient.changeWorkingDirectory(ftp_home_path); 
                        	  
      					if(!retVal){
      						createDirecrotyDir(ftp_home_path, ftpClient);
      						retVal = ftpClient.changeWorkingDirectory(ftp_home_path); 
      					}
          				
          				if(!retVal){
          					throw new Exception("ftp目录["+ftp_home_path+"]不存在.且建目录失败.[The remote directory is not exist and build failed.]");
          				}
//                		throw new Exception("ftp user's home directory failed.");
                	}else {
                		logger.info("--连接--已存在目录["+ftp_home_path+"]");
                	}
                } else {
                	disconnect();
                	throw new Exception("ftp user's home directory failed.");
                }
                return true;
         	}else {
         		disconnect();
         		throw new Exception("ftp login failed.");
         	}
		}else {
			disconnect();
			throw new Exception("The ftp mode is not in Positive.");
		}
	}

	
	
	 public void createDirecrotyDir(String remote, FTPClient ftpClient) throws IOException {
	        String directory = remote + "/";
//	        String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
	        
	        remote = remote.replace("dfs://", "/");
	        directory = directory.replace("dfs://", "/");
	        
	        logger.info("[directory-dfs://]："+directory);
	        // 如果远程目录不存在，则递归创建远程服务器目录
	        if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(new String(directory))) {
	            int start = 0;
	            int end = 0;
	            if (directory.startsWith("/")) {
	                start = 1;
	            } else {
	                start = 0;
	            }
	            end = directory.indexOf("/", start);
//	            String path = "";
	            String path = "dfs:/";
	            String paths = "";
	            while (true) {
	                String subDirectory = new String(remote.substring(start, end));
	                path = path + "/" + subDirectory;
	                logger.info("创建路径[path]："+path);
	                if (!existFile(path)) {
	                    if (ftpClient.makeDirectory(subDirectory)) {
	                    	ftpClient.changeWorkingDirectory(subDirectory);
	                    } else {
	                        logger.debug("创建目录[" + subDirectory + "]失败");
	                        ftpClient.changeWorkingDirectory(subDirectory);
	                    }
	                } else {
	                	ftpClient.changeWorkingDirectory(subDirectory);
	                }

	                paths = paths + "/" + subDirectory;
	                start = end + 1;
	                end = directory.indexOf("/", start);
	                logger.info("创建路径[end]："+end);
	                // 检查所有目录是否创建完毕
	                if (end <= start) {
	                    break;
	                }
	            }
	        }
	    }
	
	 public boolean existFile(String path) throws IOException {
	        boolean flag = false;
	        FTPFile[] ftpFileArr = ftpClient.listFiles(path);
	        if (ftpFileArr.length > 0) {
	            flag = true;
	        }
	        return flag;
	    }

	/**
    * 断开与远程服务器的连接
    * @throws IOException
    */
	public void disconnect() throws IOException {
		if (ftpClient.isConnected()) {
			ftpClient.disconnect();
			logger.info("ftp disconnect!");
		}
	}

    public void download(HttpServletResponse response, String documentPath, String documentName) throws IOException {
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String(documentName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));

        // 开始下载
        ftpClient.setBufferSize(this.getBufferSize());

        ftpClient.setControlEncoding(this.getData_encoder());

        // 设置PassiveMode传输
        ftpClient.enterLocalPassiveMode();

        //设置文件类型（二进制）
        ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

        logger.info("开始下载start to download:["+documentPath+"]");

        ftpClient.retrieveFile(documentPath, response.getOutputStream());
    }

	public boolean changeDirectory(String path) throws IOException {
		return ftpClient.changeWorkingDirectory(path);
	}

	public boolean createDirectory(String pathName) throws IOException {
		return ftpClient.makeDirectory(pathName);
  	}

	private boolean removeDirectory(String path) throws IOException {
		return ftpClient.removeDirectory(path);
	}

	public boolean removeDir(String path, boolean isAll) throws IOException, Exception{
		logger.info("准备删目录.");
		path = this.ftp_home_path+this.addPathPrefix(path);
		logger.info("删path:"+path);
		return removeDirectory(path, isAll);
	}
	
	private boolean removeDirectory(String path, boolean isAll) throws IOException, Exception {
		if (!isAll) {
			return removeDirectory(path);
		}
		
		FTPFile[] ftpFileArr = ftpClient.listFiles(path);

		if (ftpFileArr == null || ftpFileArr.length == 0) {
			return removeDirectory(path);
		}

		for (FTPFile ftpFile : ftpFileArr) {

			String name = ftpFile.getName();

			if (ftpFile.isDirectory()) {

				logger.info("* [sD]Delete subPath [" + path + "/" + name + "]");

                if (!ftpFile.getName().equals(".") && (!ftpFile.getName().equals(".."))) {
                	removeDirectory(path + "/" + name, true);
                }
			} else if (ftpFile.isFile()) {

				logger.info("* [sF]Delete file [" + path + "/" + name + "]");

				deleteFile(path + "/" + name);

			} else if (ftpFile.isSymbolicLink()) {
	
			} else if (ftpFile.isUnknown()) {

			}
		}
		return ftpClient.removeDirectory(path);
	}

	/**
    * 查看目录是否存在
    * @param path
    * @return
    * @throws IOException
    */
  	public boolean isDirectoryExists(String path) throws IOException {
  		boolean flag = false;

  		FTPFile[] ftpFileArr = ftpClient.listFiles(path);

  		for (FTPFile ftpFile : ftpFileArr) {

  			if (ftpFile.isDirectory() && ftpFile.getName().equalsIgnoreCase(path)) {
  				flag = true;
  				break;
  			}
     	}
     	return flag;
  	}

  	/**
    * 得到某个目录下的文件名列表
    * @param path
    * @return
    * @throws IOException
    */
  	public List<String> getFileList(String path) throws IOException {
  		FTPFile[] ftpFiles = ftpClient.listFiles(path);
  		List<String> retList = new ArrayList<String>();

  		if (ftpFiles == null || ftpFiles.length == 0) {
  			return retList;
  		}

  		for (FTPFile ftpFile : ftpFiles) {

  			if (ftpFile.isFile()) {
  				retList.add(ftpFile.getName());

  			}
  		}
  		return retList;
  	}

  	public void deleteFile(String remoteFileName) throws Exception, IOException {
  		logger.info("准备删除文件[remote file path]:"+remoteFileName);
    	   
  		// 从文件名获取远端目录
  		String remotePath = getPath(remoteFileName);
  		String remoteRawPath = "";
           
  		// 进入远端目录
  		// 如果远端目录不为空,就进入,且目录不存在,就抛异常
  		if(StringUtils.isNotBlank(remotePath)){
  			remotePath = this.addPathPrefix(remotePath);
  			remoteRawPath = this.ftp_home_path+remotePath;
  			logger.info("The ftp raw directory is "+remoteRawPath);
  			
  			//进入远端目录 
  			boolean pathExist = ftpClient.changeWorkingDirectory(remoteRawPath); 
  			if(!pathExist){
  				throw new Exception("ftp目录["+remoteRawPath+"]不存在.[The remote directory is not exist.]");
  			}
  		} else {
         	  // 如果远端目录为空,则远端目录就是当前ftp用户的home目录
  		}
           
  		// 在当前目录下,检查远程是否存在文件
  		FTPFile[] files = ftpClient.listFiles();
  		logger.info("files.length:"+files.length);
  		
  		boolean found = false;
  		for(FTPFile _file: files ){
  			if(getFileName(remoteFileName).equals(_file.getName())){
  				found = true;
  			}
  		}
  		if(!found){
  			throw new Exception("ftp目录["+remoteRawPath+"]下不存在名为["+getFileName(remoteFileName)+"]的文件.[The remote file is not exist.]");
  		}
    	   
  		boolean retVal = ftpClient.deleteFile(this.ftp_home_path+this.addPathPrefix(remoteFileName));
  		if(!retVal){
  			throw new Exception("删除远端文件失败.[delete failed.].");
  		}
  	}

  	public boolean upload(String remoteFileName,InputStream fis) throws IOException, Exception {
  		logger.info("准备上传文件[remote file path]:"+remoteFileName);
              
  		// 从文件名获取上传目录
  		String remotePath = getPath(remoteFileName);
              
  		// 进入上传目录
  		// 如果上传目录不为空,就进入,且目录不存在就递归建目录
  		if(StringUtils.isNotBlank(remotePath)){
  			
  			remotePath = this.addPathPrefix(remotePath);
  			String remoteRawPath = this.ftp_home_path+remotePath;
  			logger.info("The ftp raw directory is "+remoteRawPath);
            	  
  			//进入上传目录 
  			boolean pathExist = ftpClient.changeWorkingDirectory(remoteRawPath); 
  			if(!pathExist){//屏蔽上传创建目录，连接测试时已创建
  				logger.info("--upload--ftp目录["+remoteRawPath+"]不存在.");
  				
  				/*logger.info("ftp目录["+remoteRawPath+"]不存在.重建[The remote directory is not exist.Build.]:");
                	  
  				// 建目录
  				logger.info("准备在ftp目录["+this.ftp_home_path+"]下,建子目录[To mkdir under the ftp home directory]:"+remotePath);
  				// 注意传入的是不带ftp_home_path的remotePath
  				createDirecroty(remotePath, ftpClient); 
                	  
  				// 建好后,再测
  				pathExist = ftpClient.changeWorkingDirectory(remoteRawPath); 
                	  
  				if(!pathExist){
  					throw new Exception("ftp目录["+remoteRawPath+"]不存在.且建目录失败.[The remote directory is not exist and build failed.]");
  				}*/
  			}
  		} else {
  			// 如果上传目录为空,则上传目录就是当前ftp用户的home目录
  			logger.info("上传目录："+this.ftp_home_path);
  		}
              
  		// 在当前目录下,检查远程是否存在同名文件
  		ftpClient.enterLocalPassiveMode();
  		logger.info("上传ftpClient-printWorkingDirectory目录："+ftpClient.printWorkingDirectory());
  		FTPFile[] files = ftpClient.listFiles();
  		logger.info("files.length-"+files.length);
  		for(FTPFile _file: files ){
  			if(getFileName(remoteFileName).equals(_file.getName())){
  				logger.info("上传文件["+_file.getName()+"]已经存在.[The file with same name is exist.]");
  				//throw new Exception("上传文件["+files[0].getName()+"]已经存在.");
  				logger.info("覆盖上传.[overwrite.]");
  			}
  		}
              
  		// 开始上传
  		ftpClient.setBufferSize(this.getBufferSize()); 
  		ftpClient.setControlEncoding(this.getData_encoder()); 
              
  		// 设置PassiveMode传输
  		ftpClient.enterLocalPassiveMode();
              
  		//设置文件类型（二进制） 
  		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE); 
              
  		logger.info("开始上传start to upload:["+this.ftp_home_path+this.addPathPrefix(remoteFileName)+"]");
              
  		Long startTime = System.currentTimeMillis();
              
  		// 上传
  		boolean retVal = ftpClient.storeFile(this.ftp_home_path+this.addPathPrefix(remoteFileName), fis);
  		if(retVal){
  			logger.info("上传结束 upload end.");
  			Long endTime = System.currentTimeMillis();
  			Long consumeTime = endTime-startTime;
          		
  			long day=consumeTime/(24*60*60*1000);   
  			long hour=(consumeTime/(60*60*1000)-day*24);   
  			long min=((consumeTime/(60*1000))-day*24*60-hour*60);   
  			long s=(consumeTime/1000-day*24*60*60-hour*60*60-min*60);
          		
			String consumeTimeStr = ""+day+"days "+hour+"hr:"+min+"min:"+s+"sec"; 
            	
			logger.info("耗时[time consume]:"+consumeTimeStr);
  		}else {
  			logger.info("上传失败. upload failed.");
  		}
  		return retVal;
   }

   /**
   	 * 获取文件路径(不带文件名)
   	 */
   	public String getPath(String fullFileName){
   		if(StringUtils.isBlank(fullFileName))
   			return "";
   		fullFileName = fullFileName.replace("\\","/");
   		if(fullFileName.contains("/")){
   			return fullFileName.substring(0,fullFileName.lastIndexOf("/")+1);				
   		}else{
   			return "";				
   		}
   	}
   	
   	/**
   	 * 获取文件文件名(不带路径)
   	 */
   	public  String getFileName(String fullFileName){
   		if(StringUtils.isBlank(fullFileName))
   			return "";
   		fullFileName = fullFileName.replace("\\","/");
   		if(fullFileName.contains("/")){
   			return fullFileName.substring(fullFileName.lastIndexOf("/")+1,fullFileName.length());				
   		}else{
   			return fullFileName;				
   		}
   	}
   	
   	public String addPathPrefix(String path){
		if(StringUtils.isBlank(path))
			return "";
		path = path.replace("\\","/");
		if(!path.startsWith("/")){
			return "/" + path;
		}else
			return path;			
	}
   	
   	/**
	 * 在路径尾部去掉/
	 * @author guanxiaoyu
	 * @param path
	 * @return
	 */
	public String dropPathSuffix(String path){
		if(StringUtils.isBlank(path))
			return "";
		path = path.replace("\\","/");
		if(path.endsWith("/")){
			return StringUtils.substring(path, 0,path.length()-1);
		}else
			return path;			
	}

	/**
    * 递归创建远程服务器目录
    * @param remote 远程服务器文件绝对路径
    * @param ftpClient FTPClient对象
    * @return 目录创建是否成功
    * @throws IOException
    */
	public void createDirecroty(String remote, FTPClient ftpClient) throws IOException {

		String directory = remote.substring(0, remote.lastIndexOf("/") + 1);
              
		if (!directory.equalsIgnoreCase("/") && !ftpClient.changeWorkingDirectory(directory)) {
			// 如果远程目录不存在，则递归创建远程服务器目录
			int start = 0;
			int end = 0;

			if (directory.startsWith("/")) {
				start = 1;
			} else {
				start = 0;
			}

			end = directory.indexOf("/", start);

			while (true) {
				String subDirectory = remote.substring(start, end);

				if (!ftpClient.changeWorkingDirectory(subDirectory)) {

					if (ftpClient.makeDirectory(subDirectory)) {
						ftpClient.changeWorkingDirectory(subDirectory);
					} else {
						logger.info("创建目录失败");
						return ;
					}
				}

            	start = end + 1;
            	end = directory.indexOf("/", start);

            	// 检查所有目录是否创建完毕
            	if (end <= start) {
            		break;
            	}
			}

		}
		return ;
	}

    public InputStream getFileInPutStream(String remoteFileName) throws IOException {
        logger.info("准备下载文件[remote file path]:"+remoteFileName);
        // 从文件名获取下载目录
        String remotePath = getPath(remoteFileName);

        String remoteRawPath = "";
        // 进入下载目录
        // 如果下载目录不为空,就进入,且目录不存在,就抛异常
        if(StringUtils.isNotBlank(remotePath)){
            logger.info("下载目录不为空");
            remotePath = this.addPathPrefix(remotePath);
            remoteRawPath = this.ftp_home_path+remotePath;
            logger.info("The ftp raw directory is "+remoteRawPath);

            //进入下载目录
            boolean pathExist = ftpClient.changeWorkingDirectory(remotePath);
            if(!pathExist){
                throw new ServiceException("ftp目录["+this.ftp_home_path+"]不存在.[The remote directory is not exist.]");
            }
        } else {
            logger.info("查看目录："+this.ftp_home_path);
        }
        // 在当前目录下,检查远程是否存在文件
        ftpClient.enterLocalPassiveMode();
        FTPFile[] files = ftpClient.listFiles();
        logger.info("下载files="+files.length);
        boolean found = false;
        for(FTPFile _file: files ){
            if(getFileName(remoteFileName).equals(_file.getName())){
                found = true;
            }
        }
        if(!found){
            throw new ServiceException("ftp目录["+this.ftp_home_path+"]下不存在名为["+getFileName(remoteFileName)+"]的文件.[The remote file is not exist.]");
        }
        return ftpClient.retrieveFileStream(remoteFileName);
    }

	public FTPClient getFtpClient() {
		return ftpClient;
	}

	public void setFtpClient(FTPClient ftpClient) {
		this.ftpClient = ftpClient;
	}

	
	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFtp_home_path() {
		return ftp_home_path;
	}

	public void setFtp_home_path(String ftp_home_path) {
		this.ftp_home_path = ftp_home_path;
	}

	public String getData_encoder() {
		return data_encoder;
	}

	public void setData_encoder(String data_encoder) {
		this.data_encoder = data_encoder;
	}

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
}
