/**
 * 
 */
package cn.jing.security.browser.session;

import java.io.IOException;

import javax.servlet.ServletException;

import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import cn.jing.security.core.properties.SecurityProperties;

/**
 * function:并发登录导致session失效时，默认的处理策略
 * 
 * @author liangjing
 *
 */
public class JingExpiredSessionStrategy extends AbstractSessionStrategy implements SessionInformationExpiredStrategy {

	public JingExpiredSessionStrategy(SecurityProperties securityPropertie) {
		super(securityPropertie);
	}

	/*
	 * function：SessionInformationExpiredEvent是session过期的一个事件，
	 * 在这个事件里面有导致踢用户这个行为产生的请求和相应的响应， 那么在这个请求里你就可以获取到你所需要记录的一些信息来进行保存
	 */
	@Override
	public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws IOException, ServletException {
		onSessionInvalid(event.getRequest(), event.getResponse());
	}

	@Override
	protected boolean isConcurrency() {
		return true;
	}

}