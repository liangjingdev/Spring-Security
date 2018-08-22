/**
 * 
 */
package cn.jing.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author liangjing
 *
 */
public interface ValidateCodeGenerator {

	/**
	 * 生成图片验证码
	 *
	 * @param request
	 *            请求
	 * @return ImageCode实例对象
	 */
	ImageCode generate(ServletWebRequest request);
}
