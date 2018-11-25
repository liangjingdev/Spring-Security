/**
 * 
 */
package cn.jing.security.core.properties;

/**
 * function:QQ登录的相关配置项
 * 
 * 
 * @author liangjing
 *
 */
public class QQProperties {

	/**
	 * 第三方id，用来决定发起第三方登录的url，默认是qq。
	 */
	private String providerId = "qq";

	/**
	 * Application id.
	 */
	private String appId;

	/**
	 * Application secret.
	 */
	private String appSecret;

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppSecret() {
		return this.appSecret;
	}

	public void setAppSecret(String appSecret) {
		this.appSecret = appSecret;
	}

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

}
