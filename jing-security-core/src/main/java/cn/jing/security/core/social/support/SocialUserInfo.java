/**
 * 
 */
package cn.jing.security.core.social.support;

/**
 * function:社交网站用户基本信息实体类
 * 
 * @author liangjing
 *
 */
public class SocialUserInfo {

	// 用户在该服务提供商的唯一标识
	private String providerId;

	// 服务提供商Id
	private String providerUserId;

	// 昵称
	private String nickname;

	// 头像
	private String headimg;

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getProviderUserId() {
		return providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		this.providerUserId = providerUserId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimg() {
		return headimg;
	}

	public void setHeadimg(String headimg) {
		this.headimg = headimg;
	}

}
