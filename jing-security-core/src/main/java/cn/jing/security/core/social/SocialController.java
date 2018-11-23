package cn.jing.security.core.social;

import org.springframework.social.connect.Connection;

import cn.jing.security.core.social.support.SocialUserInfo;

/**
 * function:与第三方登录相关的服务
 * 
 * @author liangjing
 *
 */
public abstract class SocialController {

	/**
	 * function:根据Connection信息构建SocialUserInfo
	 * 
	 * @param connection
	 * @return
	 */
	protected SocialUserInfo buildSocialUserInfo(Connection<?> connection) {
		SocialUserInfo userInfo = new SocialUserInfo();
		userInfo.setProviderId(connection.getKey().getProviderId());
		userInfo.setProviderUserId(connection.getKey().getProviderUserId());
		userInfo.setNickname(connection.getDisplayName());
		userInfo.setHeadimg(connection.getImageUrl());
		return userInfo;
	}

}
