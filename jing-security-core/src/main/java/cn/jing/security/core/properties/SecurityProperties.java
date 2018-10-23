/**
 * 
 */
package cn.jing.security.core.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * function:整个系统的相关配置项
 * 
 * @ConfigurationProperties(prefix ="jing.security”)
 *                                 ：表示SecurityProperties这个类作为配置读取器来使用，它会去读取配置文件中所有以”jing.security”开头的配置项，
 *                                 接着后面带有browser字段的配置项都会被读取到BrowserProperties对象(browser)中去，比如”jing.security.browser.loginPage”对应的是BrowserProperties对象中的loginPage字段，
 *                                 若配置了”jing.security.browser.loginPage”项的话，那么它的值就会覆盖掉BrowserProperties对象中的loginPage字段的默认值。
 *                                 为了使这个读取配置的类生效，还需要写一个类，并加上注解@Configuration以及@EnableConfigurationProperties(SecurityProperties.class)。
 * 
 * @author liangjing
 *
 */
@ConfigurationProperties(prefix = "jing.security")
public class SecurityProperties {

	/**
	 * 浏览器环境配置
	 */
	private BrowserProperties browser = new BrowserProperties();
	/**
	 * 验证码配置
	 */
	private ValidateCodeProperties code = new ValidateCodeProperties();
	/**
	 * 社交登录配置
	 */
	private SocialProperties social = new SocialProperties();
	/**
	 * OAuth2认证服务器配置
	 */
	private OAuth2Properties oauth2 = new OAuth2Properties();

	public BrowserProperties getBrowser() {
		return browser;
	}

	public void setBrowser(BrowserProperties browser) {
		this.browser = browser;
	}

	public ValidateCodeProperties getCode() {
		return code;
	}

	public void setCode(ValidateCodeProperties code) {
		this.code = code;
	}

	public SocialProperties getSocial() {
		return social;
	}

	public void setSocial(SocialProperties social) {
		this.social = social;
	}

	public OAuth2Properties getOauth2() {
		return oauth2;
	}

	public void setOauth2(OAuth2Properties oauth2) {
		this.oauth2 = oauth2;
	}

}
