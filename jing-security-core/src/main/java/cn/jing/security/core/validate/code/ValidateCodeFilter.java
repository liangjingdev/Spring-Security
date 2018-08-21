/**
 * 
 */
package cn.jing.security.core.validate.code;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.OncePerRequestFilter;

import cn.jing.security.core.properties.SecurityProperties;

/**
 * @author liangjing
 *
 */
public class ValidateCodeFilter extends OncePerRequestFilter implements InitializingBean {

	private static final String SUBMIT_FORM_DATA_PATH = "/authentication/form";

	// 自定义的登录失败的处理器
	private AuthenticationFailureHandler authenticationFailureHandler;

	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	// 这是个HashSet集合，里面存放的URL是需要进行验证码校验的
	private Set<String> urls = new HashSet<>();

	private SecurityProperties securityProperties;

	private final AntPathMatcher antPathMatcher = new AntPathMatcher();

	@Override
	public void afterPropertiesSet() throws ServletException {
		// 首先还是需要调用一下父类的实现，因为父类已经实现了afterPropertiesSet()方法
		super.afterPropertiesSet();
		String[] configUrls = StringUtils
				.splitByWholeSeparatorPreserveAllTokens(securityProperties.getCode().getImage().getUrl(), ",");
		// 循环这个String类型的数组，逐一的将数组里的数据加入到集合中
		urls.addAll(Arrays.asList(configUrls));
		// 表单登录的链接是必须要进行验证码校验的
		urls.add(SUBMIT_FORM_DATA_PATH);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		boolean action = false;
		for (String url : urls) {
			// 如果实际访问的URL(request.getRequestURI())与用户在YML配置文件中配置的相同(即上面的urls集合里有该实际访问的URL)，并且这是一个post请求，那么就进行验证码校验
			if (antPathMatcher.match(url, request.getRequestURI())) {
				action = true;
			}
		}
		if (action) {
			try {
				validate(new ServletWebRequest(request));
			} catch (ValidateCodeException e) {
				// 捕获到异常的话，则将其交由自定义的登录失败的处理器来将此异常信息返回给前台
				authenticationFailureHandler.onAuthenticationFailure(request, response, e);
				// 如果抛出异常的话，那么在此处就可以直接return掉，这样就不会再去调用后面的过滤器了
				return;
			}
		}
		// 调用后面的过滤器
		filterChain.doFilter(request, response);
	}

	/**
	 * 图形验证码校验逻辑
	 *
	 * @param request
	 *            请求
	 * @throws ServletRequestBindingException
	 *             请求异常
	 */
	private void validate(ServletWebRequest request) throws ServletRequestBindingException {
		// 从session中获取图片验证码
		ImageCode imageCodeInSession = (ImageCode) sessionStrategy.getAttribute(request,
				ValidateCodeController.SESSION_KEY);
		// 从请求中获取用户填写的验证码
		String imageCodeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(), "imageCode");
		if (StringUtils.isBlank(imageCodeInRequest)) {
			throw new ValidateCodeException("验证码不能为空");
		}
		if (null == imageCodeInSession) {
			throw new ValidateCodeException("验证码不存在");
		}
		if (imageCodeInSession.isExpired()) {
			sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
			throw new ValidateCodeException("验证码已过期");
		}
		if (!StringUtils.equalsIgnoreCase(imageCodeInRequest, imageCodeInSession.getCode())) {
			throw new ValidateCodeException("验证码不匹配");
		}
		// 验证成功，删除session中的验证码
		sessionStrategy.removeAttribute(request, ValidateCodeController.SESSION_KEY);
	}

	public AuthenticationFailureHandler getAuthenticationFailureHandler() {
		return authenticationFailureHandler;
	}

	public void setAuthenticationFailureHandler(AuthenticationFailureHandler authenticationFailureHandler) {
		this.authenticationFailureHandler = authenticationFailureHandler;
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

}
