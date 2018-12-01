/**
 * 
 */
package cn.jing.security.core.validate.code;

import org.springframework.web.context.request.ServletWebRequest;

/**
 * function:验证码存取器接口
 * 
 * @author liangjing
 *
 */
public interface ValidateCodeRepository {

	/**
	 * function:保存验证码
	 * 
	 * @param request
	 * @param code
	 * @param validateCodeType
	 */
	void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType);

	/**
	 * function:获取验证码
	 * 
	 * @param request
	 * @param validateCodeType
	 * @return
	 */
	ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType);

	/**
	 * function:移除验证码
	 * 
	 * @param request
	 * @param codeType
	 */
	void remove(ServletWebRequest request, ValidateCodeType codeType);

}
