package cn.jing.security.core.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * 
 * function:默认的UserDetailsService接口的实现类
 * 
 * 不做任何处理，只在控制台打印一句日志，然后抛出异常，提醒业务系统自己配置UserDetailsService。
 * 
 * @author liangjing
 *
 */
public class DefaultUserDetailsService implements UserDetailsService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		logger.warn("请配置 UserDetailsService 接口的实现");
		throw new UsernameNotFoundException(username);
	}

}
