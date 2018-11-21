package cn.jing.security.core.social;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.security.SpringSocialConfigurer;

import cn.jing.security.core.properties.SecurityProperties;
import cn.jing.security.core.social.support.JingSpringSocialConfigurer;
import cn.jing.security.core.social.support.SocialAuthenticationFilterPostProcessor;

/**
 * function:社交登录安全配置主类
 * 
 * @author liangjing
 *
 */
@Configuration
@EnableSocial // 启用Spring Social的相关功能
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private SecurityProperties securityProperties;

	@Autowired(required = false)
	private ConnectionSignUp connectionSignUp;

	@Autowired(required = false)
	private SocialAuthenticationFilterPostProcessor socialAuthenticationFilterPostProcessor;

	/*
	 * function：配置JdbcUsersConnectionRepository
	 */
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		// ConnectionFactoryLocator这个类的作用就是根据条件去查找当前需要用到的对应的ConnectionFactory来构建Connection数据，因为在系统里可能会有多个ConnectionFactory，比如有QQ的ConnectionFactory、微信的ConnectionFactory。
		// Encryptors的作用是帮你把插到数据库里的数据做加解密，因为我们插到数据库里的是一些比较敏感的数据，比如用户的accessToken。(Encryptors.noOpText()表示不做任何操作，意思就是把原样的数据给存储进数据库里，为了后面能看清楚一点，在真正开发的时候不能够这样做)。
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource,
				connectionFactoryLocator, Encryptors.noOpText());
		// 需要注意的是：UserConnection这张表的表名是不能够变的，但是你可以给它加上前缀，如：jing_UserConnection，接着还需要去做配置：repository.setTablePrefix("jing_”)
		repository.setTablePrefix("jing_");

		// 如果该属性不为空，那么用户第一次使用社交账号登录时，它就会去执行ConnectionSignUp接口的execute方法，这个方法会返回一个新的用户Id，如果这个新的用户Id确实返回回来了，
		// 那么就会将这个新的用户Id与当前的connection信息一起插入到数据库的UserConnection表中，建一条新的记录。也就是说默认的它会根据你的社交账户的用户信息去自动的帮你新创建一个业务系统的用户，然后直接给你登录上去（自个偷偷的给你注册了）。
		if (connectionSignUp != null) {
			repository.setConnectionSignUp(connectionSignUp);
		}
		return repository;
	}

	/**
	 * function:SpringSocialConfigurer类中进行了SocialAuthenticationFilter过滤器的相关配置，所以如果需要使用社交登录功能的话，那么就需要注册一个SpringSocialConfigurer类型的Bean，
	 * 然后供浏览器或app模块引入社交登录配置用(.apply(springSocialConfigurer))。
	 * 
	 * @return
	 */
	@Bean
	public SpringSocialConfigurer jingSocialSecurityConfig() {
		String filterProcessesUrl = securityProperties.getSocial().getFilterProcessesUrl();
		JingSpringSocialConfigurer configurer = new JingSpringSocialConfigurer(filterProcessesUrl);
		// 社交登录时，如果需要用户进行注册，以下配置的就是即将跳转的url
		configurer.signupUrl(securityProperties.getBrowser().getSignUpUrl());
		configurer.setSocialAuthenticationFilterPostProcessor(socialAuthenticationFilterPostProcessor);
		return configurer;
	}

	/**
	 * function:SpringSocial提供的一个用来处理注册/绑定流程的工具类
	 * 
	 * 该工具类解决的两个问题：1、在注册/绑定过程中如何拿到SpringSocial中的第三方用户的基本信息
	 * 2、注册/绑定完成了，如何把业务系统里的对应的用户Id(用户唯一标识即可)再传给SpringSocial
	 * 
	 * @param connectionFactoryLocator
	 * @return
	 */
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator) {
		return new ProviderSignInUtils(connectionFactoryLocator,
				getUsersConnectionRepository(connectionFactoryLocator)) {
		};
	}
}
