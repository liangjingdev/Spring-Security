/**
 * 
 */
package cn.jing.security.core.social.weixin.api;

/**
 * function:微信API调用接口
 * 
 * @author liangjing
 *
 */
public interface Weixin {

	/**
	 * function:获取微信用户信息
	 * 
	 * @param openId
	 * @return
	 */
	WeixinUserInfo getUserInfo(String openId);

}
