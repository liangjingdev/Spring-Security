package cn.jing.security.browser.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.session.InvalidSessionStrategy;

import cn.jing.security.core.properties.SecurityProperties;

/**
 * function:默认的session失效处理策略
 * 
 * @author liangjing
 *
 */
public class JingInvalidSessionStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {

	public JingInvalidSessionStrategy(SecurityProperties securityProperties) {
		super(securityProperties);
	}

	@Override
	public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		onSessionInvalid(request, response);
	}

}
