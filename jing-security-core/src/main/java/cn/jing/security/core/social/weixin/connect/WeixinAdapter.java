/**
 * 
 */
package cn.jing.security.core.social.weixin.connect;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import cn.jing.security.core.social.weixin.api.Weixin;
import cn.jing.security.core.social.weixin.api.WeixinUserInfo;

/**
 * function:微信api适配器，将微信api的数据模型转为spring social的标准模型。
 * 与QQ不一样的是：QQ的openId是放在QQImpl类里面的，而微信的openId是放在WeixinAdapter类里面。
 * 
 * @author liangjing
 *
 */
public class WeixinAdapter implements ApiAdapter<Weixin> {

	private String openId;

	public WeixinAdapter() {
	}

	public WeixinAdapter(String openId) {
		this.openId = openId;
	}

	/**
	 * @param api
	 * @return
	 */
	@Override
	public boolean test(Weixin api) {
		return true;
	}

	/**
	 * @param api
	 * @param values
	 */
	@Override
	public void setConnectionValues(Weixin api, ConnectionValues values) {
		WeixinUserInfo profile = api.getUserInfo(openId);
		values.setProviderUserId(profile.getOpenid());
		values.setDisplayName(profile.getNickname());
		values.setImageUrl(profile.getHeadimgurl());
	}

	/**
	 * @param api
	 * @return
	 */
	@Override
	public UserProfile fetchUserProfile(Weixin api) {
		return null;
	}

	/**
	 * @param api
	 * @param message
	 */
	@Override
	public void updateStatus(Weixin api, String message) {
		// do nothing
	}

}
