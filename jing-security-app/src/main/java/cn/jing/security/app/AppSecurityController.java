package cn.jing.security.app;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import cn.jing.security.app.social.AppSingUpUtils;
import cn.jing.security.core.properties.SecurityConstants;
import cn.jing.security.core.social.SocialController;
import cn.jing.security.core.social.support.SocialUserInfo;

/**
 * function:App环境下与第三方登录相关的服务
 * 
 * @author liangjing
 *
 */
@RestController
public class AppSecurityController extends SocialController {

	@Autowired
	private ProviderSignInUtils providerSignInUtils;

	@Autowired
	private AppSingUpUtils appSingUpUtils;

	/**
	 * function:需要注册时跳到这里，返回401和用户信息给前端(app),那么这时候app就应该知道它应该显示一个注册的页面给用户，用这些信息去引导用户做一个注册或者绑定
	 * 当用户注册或者绑定完以后，最终会带着一个userId去访问"/user/regist"路径
	 * 
	 * @param request
	 * @return
	 */
	@GetMapping(SecurityConstants.DEFAULT_SOCIAL_USER_INFO_URL)
	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public SocialUserInfo getSocialUserInfo(HttpServletRequest request) {
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(new ServletWebRequest(request));
		appSingUpUtils.saveConnectionData(new ServletWebRequest(request), connection.createData());
		return buildSocialUserInfo(connection);
	}

}
