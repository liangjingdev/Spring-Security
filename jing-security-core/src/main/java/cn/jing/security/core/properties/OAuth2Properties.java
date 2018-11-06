/**
 * 
 */
package cn.jing.security.core.properties;

/**
 * function:认证服务器的相关配置项
 * 
 * @author liangjing
 *
 */
public class OAuth2Properties {

	/**
	 * 使用jwt时为token签名的秘钥（验令牌的时候也会使用它来进行验签，所以该密钥一定要保存好，
	 * 因为别人一旦知道这个密钥，就可以使用该密钥去签发你的jwt令牌，因为jwt令牌唯一的安全性就
	 * 是这个密钥，如果你的密钥丢了，别人用你的密钥去签发令牌的话，就可以随意进到你的系统里）
	 */
	private String jwtSigningKey = "jing";
	/**
	 * 客户端配置
	 */
	private OAuth2ClientProperties[] clients = {};

	public OAuth2ClientProperties[] getClients() {
		return clients;
	}

	public void setClients(OAuth2ClientProperties[] clients) {
		this.clients = clients;
	}

	public String getJwtSigningKey() {
		return jwtSigningKey;
	}

	public void setJwtSigningKey(String jwtSigningKey) {
		this.jwtSigningKey = jwtSigningKey;
	}

}
