package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import org.springframework.stereotype.Service;
import util.MD5;

@Service
public class LoginService {

	public boolean login(String userName,String passWord,String salt){
		Properties properties = new Properties();
		try {
			InputStream in = new FileInputStream(LoginService.class.getResource("../login.properties").getFile());
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			properties.clone();
		}
		String username2 = properties.getProperty("login.username");
		String password2 = properties.getProperty("login.password");
		String md5password2 = MD5.getMD5Str(salt + password2);
		if(username2.equals(userName)&&md5password2.equals(passWord)){
			return true;
		}
		return false;
	}
	
	/**
	 * 修改登录密码
	 * @param oldPassWord
	 * @param newPassWord
	 * @return
	 */
	public boolean upPassWord(String oldPassWord,String newPassWord,String salt)throws Exception{
		URL fileUrl = LoginService.class.getResource("../login.properties");
		InputStream in = new FileInputStream(fileUrl.getFile());
		Properties properties = new Properties();
		properties.load(in);
		String password2 = properties.getProperty("login.password");
		String md5password2 = MD5.getMD5Str(salt + password2);
		if(!md5password2.equals(oldPassWord)){
			throw new Exception("原密码错误！");
		}
		properties.setProperty("login.password", newPassWord);
		FileOutputStream oFile = new FileOutputStream(new File(fileUrl.toURI()));
		properties.store(oFile,null);
		oFile.close();
		properties.clone();
		return true;
	}
}
