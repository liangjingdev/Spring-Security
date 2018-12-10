/**
 * 
 */
package cn.jing.security.core.social.qq.connet;

import org.springframework.social.connect.ApiAdapter;
import org.springframework.social.connect.ConnectionValues;
import org.springframework.social.connect.UserProfile;

import cn.jing.security.core.social.qq.api.QQ;
import cn.jing.security.core.social.qq.api.QQUserInfo;

/**
 * function:数据适配
 * 
 * @author liangjing
 *
 */
public class QQAdapter implements ApiAdapter<QQ> {

	/**
	 * function:测试当前的Api是否可用，也就是当前QQ的服务是否还可调用
	 */
	@Override
	public boolean test(QQ api) {
		// 此处可以发请求去判断，我们这里就直接返回true了，自个儿认为QQ服务还是通的
		return true;
	}

	/**
	 * function:数据适配 需要注意的是：该方法内部不允许抛出异常
	 */
	@Override
	public void setConnectionValues(QQ api, ConnectionValues values) {
		// 获取QQ用户的基本信息
		QQUserInfo userInfo = api.getUserInfo();

		// 把QQ用户的基本信息的一些值设到ConnectionValues里，ConnectionValues总共有这四个set方法
		values.setDisplayName(userInfo.getNickname()); // QQ用户昵称
		values.setImageUrl(userInfo.getFigureurl_qq_1()); // 头像
		values.setProfileUrl(null); // 这个东西对于QQ来说是没有用的，因为QQ没有所谓的个人主页，微博就有个人主页
		values.setProviderUserId(userInfo.getOpenId()); // QQ用户在服务提供商里的唯一标识
	}

	@Override
	public UserProfile fetchUserProfile(QQ api) {
		return null;
	}

	@Override
	public void updateStatus(QQ api, String message) {
		// do noting
	}

}
