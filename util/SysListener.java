package util;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SysListener implements HttpSessionListener{

	public void sessionCreated(HttpSessionEvent arg0) {

	}

	public void sessionDestroyed(HttpSessionEvent arg0) {
		HttpSession httpSession = arg0.getSession();
		String loginName = Tools.obj2Str(httpSession.getAttribute(SysConstants.LOGIN_NAME));
		if(httpSession.getId().equals(SysConstants.IMMISSION_LOGIN.get(loginName))){
			SysConstants.IMMISSION_LOGIN.remove(loginName);
		}
	}

}
