/**
 * 
 */
package cn.jing.security.app.authentication;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.TokenRequest;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * function：app环境下登录成功的处理器
 * 
 * @author liangjing
 *
 */
@Component("jingAuthenticationSuccessHandler")
public class JingAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private ClientDetailsService clientDetailsService;

	@Autowired
	private AuthorizationServerTokenServices authorizationServerTokenServices;

	@SuppressWarnings("unchecked")
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		logger.info("登录成功");

		// 首先要做的事情就是要从请求头里拿到Authorization首部字段对应的值，也就是那个字符串，然后再从这个字符串中解析出clientId。实际上是一个Basic登录的过程。
		// 从BasicAuthenticationFilter过滤器中拷出一些代码即可。
		String header = request.getHeader("Authorization");

		// 如果这个值为空或者不是以Basic开头的那就不对
		if (header == null || !header.startsWith("Basic ")) {
			throw new UnapprovedClientAuthenticationException("请求头中无client信息");
		}

		// 抽取并且解码那个对应的字符串
		String[] tokens = extractAndDecodeHeader(header, request);
		assert tokens.length == 2;

		// 取出clientId字段对应的值与clientSecret对应的值
		String clientId = tokens[0];
		String clientSecret = tokens[1];

		// 生成ClientDetails对象
		ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

		if (clientDetails == null) {
			throw new UnapprovedClientAuthenticationException("clientId对应的配置信息不存在:" + clientId);
		} else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
			throw new UnapprovedClientAuthenticationException("clientSecret不匹配:" + clientId);
		}

		// 生成TokenRequest对象，需要传递四个参数，分别是：requestParameters、clientId、scope、grantType
		// 第一个参数实际上是一个Map集合，里面放着每一个授权模式下它特有的请求参数，然后SpringSecurity的OAuth标准流程就会根据这些参数
		// 去组建Authentication，也就是去验用户信息，但是在我们的这个流程里，我们不需要去构建Authentication，因为Authentication
		// 已经由成功处理器的参数传进来了，所以这里传一个空Map集合进去即可。
		// 第三个参数的值是第三方应用里配置的，也就说配了这个应用能有什么权限，那么我直接就给它什么权限，不再走SpringSecurityOAuth的标准逻辑。
		// 不再去做与scope相关的校验，因为我们只是在我们自个的app、前端之间去做这个事，所以我直接就把所有的权限都给你。然后你就能调到所有东西了那就可以了。
		// 第四个参数我们填自定义的值。
		TokenRequest tokenRequest = new TokenRequest(MapUtils.EMPTY_MAP, clientId, clientDetails.getScope(), "custom");

		// 生成OAuth2Request对象
		OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);

		OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
		// 最终生成OAuth2AccessToken对象
		OAuth2AccessToken token = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
		// 将令牌信息以json字符串形式写到响应中去
		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(token));

	}

	// 抽取并且解码那个对应的字符串
	private String[] extractAndDecodeHeader(String header, HttpServletRequest request) throws IOException {

		byte[] base64Token = header.substring(6).getBytes("UTF-8");
		byte[] decoded;
		try {
			decoded = Base64.decode(base64Token);
		} catch (IllegalArgumentException e) {
			throw new BadCredentialsException("Failed to decode basic authentication token");
		}

		String token = new String(decoded, "UTF-8");

		int delim = token.indexOf(":");

		if (delim == -1) {
			throw new BadCredentialsException("Invalid basic authentication token");
		}
		return new String[] { token.substring(0, delim), token.substring(delim + 1) };
	}

}
