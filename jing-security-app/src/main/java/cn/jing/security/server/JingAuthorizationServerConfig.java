/**
 * 
 */
package cn.jing.security.server;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import cn.jing.security.core.properties.OAuth2ClientProperties;
import cn.jing.security.core.properties.SecurityProperties;

/**
 * function:认证服务器配置，通过继承AuthorizationServerConfigurerAdapter这个类并重写configure方法去添加一些自定义的配置
 * 
 * @author liangjing
 *
 */
@Configuration
@EnableAuthorizationServer
public class JingAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

	// 自个的UserDetailsService的实现
	@Autowired
	private UserDetailsService userDetailsService;

	// 去做认证的时候的AuthenticationManager
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private TokenStore tokenStore;

	@Autowired(required = false)
	private JwtAccessTokenConverter jwtAccessTokenConverter;

	@Autowired(required = false)
	private TokenEnhancer jwtTokenEnhancer;

	@Autowired
	private SecurityProperties securityProperties;

	/**
	 * function:配置授权服务器端点，如令牌存储，令牌自定义，用户批准和授权类型，不包括端点安全配置
	 */
	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		// 默认情况下，令牌是存储在服务器的内存中的，一旦重启服务器，那么令牌信息就会被清空。我们应当将其存储到持久化的数据源里(redis或者数据库)
		// 由于令牌是个访问比较频繁的东西，所以将其存在redis里
		endpoints.tokenStore(tokenStore).authenticationManager(authenticationManager)
				.userDetailsService(userDetailsService);

		if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
			// 增强器链（在DefaultTokenServices类里面的createAccessToken方法是一个私有方法，在该方法里面new了一个DefaultOAuth2AccessToken
			// 由于没有接口的封装，意味着这是不可变的，所以说这块代码是没法改变的，只能够通过一些增强器去改变这个token里面的内容，比如把UUID转成JWT、
			// 把一些自定义内容加入到token中，这些都是通过增强器来完成的）
			TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
			// 用增强器链把以下两个增强器连起来
			List<TokenEnhancer> enhancers = new ArrayList<>();
			enhancers.add(jwtTokenEnhancer);
			enhancers.add(jwtAccessTokenConverter);
			enhancerChain.setTokenEnhancers(enhancers);
			endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
		}

	}

	/**
	 * function:配置授权服务器端点的安全
	 */
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		// tokenKey的访问权限表达式配置
		security.tokenKeyAccess("permitAll()");
	}

	/**
	 * function:客户端配置（会有哪些应用来访问我们的系统，认证服务器会给哪些应用发令牌）
	 * 当覆盖了这个方法以后，那么之前在application.properties中指定的clientId以及clientSecret
	 * 就不再起作用了，它会根据这个方法里的配置来决定能给哪些应用发令牌，然后这些应用的配置是什么
	 */
	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		// clients.inMemory()表示将这些信息存在内存里，clients.jdbc(datasource)表示存在数据库里
		InMemoryClientDetailsServiceBuilder builder = clients.inMemory();
		if (ArrayUtils.isNotEmpty(securityProperties.getOauth2().getClients())) {
			for (OAuth2ClientProperties client : securityProperties.getOauth2().getClients()) {
				builder.withClient(client.getClientId()).secret(client.getClientSecret())
						.authorizedGrantTypes("refresh_token", "authorization_code", "password") // 针对当前客户端应用，你所能支持的授权模式是哪些
						.accessTokenValiditySeconds(client.getAccessTokenValidateSeconds()) // 发出去的令牌的有效时间
						.refreshTokenValiditySeconds(2592000).scopes("all"); // 指的是当前客户端应用能发出去的权限是哪些，all、read、write...
			}
		}
	}

}
