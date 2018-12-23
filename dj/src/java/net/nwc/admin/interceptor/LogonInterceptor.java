package net.nwc.admin.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import net.nwc.admin.service.LogonService;

public class LogonInterceptor implements HandlerInterceptor {
	private static Logger logger = Logger.getLogger(LogonInterceptor.class);

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView model)
			throws Exception {
	}

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			return preHandlerLogin(request, response, handlerMethod);
		}
		return true;
	}

	boolean checkHandlerLogin(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler)
			throws IOException {
		HttpSession session = request.getSession(true);
		String uri = request.getRequestURI();
		Object obj = LogonService.getSessionUser(session);
		if (uri.indexOf("/logon") > -1) {
			if (obj != null) {// 已登录访问登录相关，自动跳转到/main
				response.sendRedirect("/main");
				return false;
			}
		} else {
			if (obj == null) {// 访问其他页面，无登录用户，跳转转到登录界面
				return true;
			}
		}
		return false;
	}

	private boolean preHandlerLogin(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler)
			throws IOException {
		if (checkHandlerLogin(request, response, handler)) {
			logger.info("拦截登录请求： 需先登录");
			String resultUrl = request.getRequestURL().toString();
			String param = request.getQueryString();
			if (param != null) {
				resultUrl += "?" + param;
			}
			// post参数
			@SuppressWarnings("rawtypes")
			Enumeration enu = request.getParameterNames();
			StringBuffer paramBuf = new StringBuffer(128);
			int index = 0;
			while (enu.hasMoreElements()) {
				String paraName = (String) enu.nextElement();
				if (index > 0) {
					paramBuf.append("&");
				}
				paramBuf.append(paraName).append("=").append(request.getParameter(paraName));
				index++;
			}
			if (paramBuf.length() > 0) {
				if (resultUrl.indexOf("?") > -1) {
					resultUrl += "&" + paramBuf.toString();
				} else {
					resultUrl += "?" + paramBuf.toString();
				}
			}
			try {
				resultUrl = URLEncoder.encode(resultUrl, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 请求的路径
			String contextPath = request.getContextPath();
			String url = contextPath + "/logon?redirect=" + resultUrl;
			if (param != null) {
				url += "&" + param;
			}
			response.sendRedirect(url);
			return false;// 终止拦截，处理结束
		}
		return true;
	}

}