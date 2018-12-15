/**
 * 
 */
package cn.jing.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * function:验证码生成器接口
 * 
 * @author liangjing
 *
 */
public interface ValidateCodeGenerator {

	/**
	 * function:生成验验码
	 * 
	 * @param request 这里为什么会传入一个ServletWebRequest类型的参数，是因为ServletWebRequest类中有许多对请求中的参数进行操作的方法，十分方便
	 * @return
	 */
	ValidateCode generate(ServletWebRequest request);

}
