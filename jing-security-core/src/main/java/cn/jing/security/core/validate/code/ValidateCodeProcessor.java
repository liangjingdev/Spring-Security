/**
 * 
 */
package cn.jing.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * function:校验码处理器接口，封装不同校验码的处理逻辑
 * 
 * @author liangjing
 *
 */
public interface ValidateCodeProcessor {

	/**
	 * function:创建校验码
	 * 
	 * @param request
	 * @throws Exception
	 */
	void create(ServletWebRequest request) throws Exception;

	/**
	 * function:校验验证码
	 * 
	 * @param servletWebRequest
	 * @throws Exception
	 */
	void validate(ServletWebRequest servletWebRequest);

}
