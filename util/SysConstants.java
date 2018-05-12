package util;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统的常量类
 * @author wilson
 *
 */
public class SysConstants{
	
	/**
	 * 本机地址
	 */
	public static final String LOCAL_DOMAIN = "http://192.168.1.102:8080/";
	
    /**
     * 文件保存路径
     */
    public static final String FILE_PATH = "C:/TTG_st/";
    
    /**
     * 在Session对象中，用来存储用户登陆的验证码
     */
    public static final String MEM_LOGIN_VALIDCODE = "mem_login_validcode";

    /**
     * 在Session对象中，用来存储用户登陆错误数
     */
    public static final String LOGIN_ERROR_NUMBER = "login_error_number";
    
    /**
     * 登录错误数上限数3次
     */
    public static final int LOGIN_ERROR_VERUFY = 3;
    
    /**
     * 登录时密码的盐值
     */
    public static final String LOGIN_SALT = "login_salt";
    
    /**
     * 登录状态
     */
    public static final String LOGIN_STATE = "login_state";
    
    /**
     * 登录用户名
     */
    public static final String LOGIN_NAME = "login_name";
    
    /**
     * 允许登录的用户
     * 将前面登录的同个用户踢下线操作
     */
    public static Map<String,String> IMMISSION_LOGIN = new HashMap<String,String>();
    
    /**
     * 线上服务器地址
     */
    public static final String MAIN_SERVER_ADDRESS = "http://www.i-ttg.com";
    
    /**
     * 线上代理商服务器地址
     */
    public static final String AGENCY_SERVER_ADDRESS = "http://www.i-ttg.com/agent/";
    
    /**
     * 门禁服务器地址
     */
    public static final String ACS_SERVER_ADDRESS = MAIN_SERVER_ADDRESS+"/TTG_acs/";
    
    /**
     * 补丁包文件存放地址
     */
    public static final String PATCHES_FILE_PATH = FILE_PATH + "patches/";
    
    /**
     * 物业广告存放地址
     */
    public static final String ADS_FILE_PATH = FILE_PATH + "ads/";
    
    /**
     * 物业广告web访问地址
     */
    public static final String ADS_FILE_URL = LOCAL_DOMAIN + "ads/";
    
    
    /**
     * 物业通知文件存放地址
     */
    public static final String INFORM_FILE_PATH = FILE_PATH + "inform/";
    
    /**
     * 物业通知文件web访问地址
     */
    public static final String INFORM_FILE_URL = LOCAL_DOMAIN + "inform/";
    
    /**
     * 本机udp端口
     */
    public static final int UDP_HOST = 7861;
}
