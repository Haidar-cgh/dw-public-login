package cn.com.dwsoft.login.util.ftp;

/**
 * 对接 FTP 配置类
 * FTP配置类
 * @author ZC
 */
public class FtpConfig {

	private String server;
    private int port;
    private String username;
    private String password;
    private String ftp_home_path;
    private String data_encoder = "UTF-8";
    private int bufferSize = 1024;
    
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
