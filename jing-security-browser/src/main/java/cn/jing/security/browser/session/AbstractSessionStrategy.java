package cn.jing.security.browser.session;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import cn.jing.security.core.properties.SecurityProperties;
import cn.jing.security.core.support.SimpleResponse;

/**
 * function:抽象的session失效处理器
 * 
 * @author liangjing
 *
 */
public class AbstractSessionStrategy {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	/**
	 * 最终跳转的url
	 */
	private String destinationUrl;
	/**
	 * 系统配置信息
	 */
	private SecurityProperties securityPropertie;
	/**
	 * 重定向策略
	 */
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	/**
	 * 跳转前是否创建新的session
	 */
	private boolean createNewSession = true;

	private ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * @param invalidSessionUrl
	 * @param invalidSessionHtmlUrl
	 */
	public AbstractSessionStrategy(SecurityProperties securityPropertie) {
		// session失效默认的跳转地址
		String invalidSessionUrl = securityPropertie.getBrowser().getSession().getSessionInvalidUrl();
		Assert.isTrue(UrlUtils.isValidRedirectUrl(invalidSessionUrl), "url must start with '/' or with 'http(s)'");
		Assert.isTrue(StringUtils.endsWithIgnoreCase(invalidSessionUrl, ".html"), "url must end with '.html'");
		this.destinationUrl = invalidSessionUrl;
		this.securityPropertie = securityPropertie;
	}

	/**
	 * function:session失效时回调的方法
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	protected void onSessionInvalid(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.info("session失效");

		if (createNewSession) {
			request.getSession();
		}

		// session失效时，用户正在访问的url
		String sourceUrl = request.getRequestURI();
		// 最终跳转的url
		String targetUrl;

		// 如果session失效时，用户正访问的url时以.html结尾的话，那么最终就跳转到一个html页面上，否则就以json的形式给予响应
		if (StringUtils.endsWithIgnoreCase(sourceUrl, ".html")) {
			// 判断session失效时，用户正在访问的html页面是不是登录页面或者退出成功页面，若是的话，就直接跳转到登录页面或者退出成功页面，如果不是的话，那么就跳转到默认设置好的页面上
			if (StringUtils.equals(sourceUrl, securityPropertie.getBrowser().getSignInPage())
					|| StringUtils.equals(sourceUrl, securityPropertie.getBrowser().getSignOutUrl())) {
				targetUrl = sourceUrl;
			} else {
				targetUrl = destinationUrl;
			}
			logger.info("跳转到:" + targetUrl);
			redirectStrategy.sendRedirect(request, response, targetUrl);
		} else {
			Object result = buildResponseContent(request);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write(objectMapper.writeValueAsString(result));
		}

	}

	/**
	 * function:构建session失效响应信息（json形式）
	 * 
	 * @param request
	 * @return
	 */
	protected Object buildResponseContent(HttpServletRequest request) {
		String message = "session已失效";
		if (isConcurrency()) {
			message = message + "，有可能是并发登录导致的";
		}
		return new SimpleResponse(message);
	}

	/**
	 * function:判断session失效是否是并发登录导致的
	 * 
	 * @return
	 */
	protected boolean isConcurrency() {
		return false;
	}

	/**
	 * Determines whether a new session should be created before redirecting (to
	 * avoid possible looping issues where the same session ID is sent with the
	 * redirected request). Alternatively, ensure that the configured URL does not
	 * pass through the {@code SessionManagementFilter}.
	 *
	 * @param createNewSession defaults to {@code true}.
	 */
	public void setCreateNewSession(boolean createNewSession) {
		this.createNewSession = createNewSession;
	}

}
