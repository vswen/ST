package action;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import service.LoginService;
import util.NumericLetterCode;
import util.QuestionAnswerCode;
import util.RandomCode;
import util.SysConstants;
import util.Tools;

@Controller
public class LoginAction {
	
	@Resource
	private LoginService ls;
	
	/**
	 * 初始化登录
	 * @param httpSession
	 * @param map
	 * @return
	 */
	@RequestMapping("/l_init")
	public String init(HttpSession httpSession,ModelMap map){
		String salt = Tools.getRandomStr(6,1);
		httpSession.setAttribute(SysConstants.LOGIN_SALT,salt);
		int loginErrorNumber = Tools.obj2Int(httpSession.getAttribute(SysConstants.LOGIN_ERROR_NUMBER));
		int loginErrorVerufy = SysConstants.LOGIN_ERROR_VERUFY;
		map.put("salt", salt);
		map.put("loginErrorNumber", loginErrorNumber);
		map.put("loginErrorVerufy", loginErrorVerufy);
		return "login";
	}
	
	/**
	 * 登录操作
	 * @param httpSession
	 * @param userName
	 * @param passWord
	 * @param captcha
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/l_login")
	public JSONObject login(HttpSession httpSession,String userName,String passWord,String captcha){
		JSONObject json = new JSONObject();
		int loginErrorNumber = Tools.obj2Int(httpSession.getAttribute(SysConstants.LOGIN_ERROR_NUMBER));
		if(loginErrorNumber > SysConstants.LOGIN_ERROR_VERUFY){
			String memLoginValidcode = Tools.obj2Str(httpSession.getAttribute(SysConstants.MEM_LOGIN_VALIDCODE));
			if(!memLoginValidcode.equals(captcha)){
				json.put("msg", "验证码错误！");
				json.put("success", false);
				return json;
			}
		}
		String salt = Tools.obj2Str(httpSession.getAttribute(SysConstants.LOGIN_SALT));
		boolean b = ls.login(userName, passWord, salt);
		if(!b){
			if(loginErrorNumber == SysConstants.LOGIN_ERROR_VERUFY){
				json.put("loadCaptcha", true);
			}
			json.put("msg", "账户或密码错误！");
			json.put("success", false);
			httpSession.setAttribute(SysConstants.LOGIN_ERROR_NUMBER,loginErrorNumber+1);
			return json;
		}
		SysConstants.IMMISSION_LOGIN.put(userName, httpSession.getId());
		httpSession.setAttribute(SysConstants.LOGIN_STATE,true);
		httpSession.setAttribute(SysConstants.LOGIN_NAME,userName);
		json.put("success", true);
		return json;
	}
	
	/**
	 * 登录3次失败时要求输入验证码
	 * @return
	 */
	@RequestMapping("/l_loginrand")
	public void loginrand(HttpSession httpSession,HttpServletResponse response) {
		Random rand = new Random();
		int num = rand.nextInt(2);
		RandomCode random = null;
		if (num == 0) {
			random = new QuestionAnswerCode(120, 42);
		} else {
			random = new NumericLetterCode(120, 42);
		}

		byte[] bs = random.getImage();

		//将验证码放入session中
		httpSession.setAttribute(SysConstants.MEM_LOGIN_VALIDCODE, random.getCode());
        
		try {
			OutputStream stream = response.getOutputStream();
			stream.write(bs);
	        stream.close();
		} catch (IOException e) {
			
		}
	}
	
	/**
	 * 退出
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/l_esc")
	public void esc(HttpSession httpSession,HttpServletResponse response) throws IOException{
		httpSession.setAttribute(SysConstants.LOGIN_STATE,false);
		response.sendRedirect("login.jsp");
	}
	
	/**
	 * 修改密码
	 * @param httpSession
	 * @param oldPassWord
	 * @param newPassWord
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/l_upPwd")
	public JSONObject upPassWord(HttpSession httpSession,String oldPassWord,String newPassWord){
		JSONObject json = new JSONObject();
		String salt = Tools.obj2Str(httpSession.getAttribute(SysConstants.LOGIN_SALT));
		boolean b = false;
		try {
			b = ls.upPassWord(oldPassWord, newPassWord, salt);
			if(b){
				json.put("errcode", 0);
				json.put("errmsg", "ok");
			}else{
				json.put("errcode", 401);
				json.put("errmsg", "文件写入错误！");
			}
		} catch (Exception e) {
			json.put("errcode", 5);
			json.put("errmsg", e.getMessage());
		}
		return json;
	}
}
