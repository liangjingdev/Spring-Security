/**
 * 
 */
package cn.jing.security;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;

/**
 * function:ConnectionSignUp接口实现类(实现"偷偷注册"这个功能)
 * 
 * @author liangjing
 *
 */
//@Component
public class DemoConnectionSignUp implements ConnectionSignUp {

	@Override
	public String execute(Connection<?> connection) {
		// 根据社交用户信息默认创建用户并返回用户唯一标识
		// 此处拿DisplayName作为唯一标识返回回去，只是个事例，真实情况不是这样的，真实的逻辑要按照业务来做
		return connection.getDisplayName();
	}

}
