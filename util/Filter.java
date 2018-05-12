package util;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

public class Filter extends OncePerRequestFilter{
	
	@Override
	protected void doFilterInternal(HttpServletRequest arg0,
			HttpServletResponse arg1, FilterChain arg2)
			throws ServletException, IOException {
		
		//不过滤的url集合
		String[] notFilter = new String[]{"css","images","js","l_init","l_login","l_loginrand","im"};
		String uri = arg0.getRequestURI();
		String[] uris = uri.split("/");
		
		boolean b = false;
		for (String string : notFilter) {
			if(uris.length > 2 && string.equals(uris[2])){
				b = true;
				break;
			}
		}
		//获取登录状态
		boolean b2 = Tools.obj2Bool(arg0.getSession().getAttribute(SysConstants.LOGIN_STATE));
		
		//获取登录用户名
		String loginName = Tools.obj2Str(arg0.getSession().getAttribute(SysConstants.LOGIN_NAME));
		
		//将同用户名下前面登录的踢下线
		boolean b3 = false;
		if(arg0.getSession().getId().equals(SysConstants.IMMISSION_LOGIN.get(loginName))){
			b3 = true;
		}
		
		if(!b&&(!b2||!b3)){
			//跳转登录界面
			arg1.sendRedirect(arg0.getContextPath()+"/l_init?anewLogin=true");
		}else{
			arg2.doFilter(arg0, arg1);
		}
		
	}

}
