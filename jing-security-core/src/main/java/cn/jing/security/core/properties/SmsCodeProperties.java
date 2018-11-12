/**
 * 
 */
package cn.jing.security.core.properties;

/**
 * function:短信验证码的相关配置项
 * 
 * @author liangjing
 *
 */
public class SmsCodeProperties {

	/**
	 * 验证码长度
	 */
	private int length = 6;
	/**
	 * 过期时间
	 */
	private int expireIn = 60;
	/**
	 * 要拦截的url(即访问哪些页面需要进行短信验证码校验)，多个url用逗号隔开，ant pattern
	 */
	private String url;

	public int getLength() {
		return length;
	}

	public void setLength(int lenght) {
		this.length = lenght;
	}

	public int getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
