/**
 * 
 */
package cn.jing.security.browser;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cn.jing.security.core.properties.SecurityProperties;
import cn.jing.security.core.support.SimpleResponse;

/**
 * function:浏览器环境下与安全相关的服务
 * 
 * @author liangjing
 *
 */
@RestController
public class BrowserSecurityController {

	// Spring Security缓存请求
	private RequestCache requestCache = new HttpSessionRequestCache();

	// 重定向工具类
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	private Logger logger = LoggerFactory.getLogger(getClass());

	private static final String HTML = ".html";

	private final SecurityProperties securityProperties;

	@Autowired
	public BrowserSecurityController(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

	/**
	 * function：当需要进行身份认证的时候跳转到此方法
	 *
	 * @param request  请求
	 * @param response 响应
	 * @return 将信息以JSON形式返回给前端
	 */
	@RequestMapping("/authentication/require")
	@ResponseStatus(code = HttpStatus.UNAUTHORIZED)
	public SimpleResponse requireAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		// 从session缓存中获取引发跳转的请求
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if (null != savedRequest) {
			// 获取到引发跳转的请求的url
			String redirectUrl = savedRequest.getRedirectUrl();
			logger.info("引发跳转的请求的url是：{}", redirectUrl);
			if (StringUtils.endsWithIgnoreCase(redirectUrl, HTML)) {
				// 如果是HTML请求，那么就直接跳转到HTML页面(登录页面)，不再执行后面的代码（即不会返回401状态码和错误信息）
				redirectStrategy.sendRedirect(request, response, securityProperties.getBrowser().getLoginPage());
			}
		}
		return new SimpleResponse("访问的服务需要身份认证，请引导用户到登录页面");
	}

}
