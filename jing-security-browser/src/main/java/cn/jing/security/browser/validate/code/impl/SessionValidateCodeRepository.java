/**
 * 
 */
package cn.jing.security.browser.validate.code.impl;

import org.springframework.social.connect.web.HttpSessionSessionStrategy;
import org.springframework.social.connect.web.SessionStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import cn.jing.security.core.validate.code.ValidateCode;
import cn.jing.security.core.validate.code.ValidateCodeRepository;
import cn.jing.security.core.validate.code.ValidateCodeType;

/**
 * function:基于session的校验码存取器
 * 
 * @author liangjing
 *
 */
@Component
public class SessionValidateCodeRepository implements ValidateCodeRepository {

	// 验证码放入session时的前缀
	private static final String SESSION_KEY_PREFIX = "SESSION_KEY_FOR_CODE_";

	// 操作session的工具类
	private SessionStrategy sessionStrategy = new HttpSessionSessionStrategy();

	/**
	 * function:保存验证码到session中
	 * 
	 * @param request
	 * @param code
	 * @param validateCodeType
	 */
	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
		sessionStrategy.setAttribute(request, getSessionKey(validateCodeType), code);
	}

	/**
	 * function:从session中获取验证码
	 * 
	 * @param request
	 * @param validateCodeType
	 * @return
	 */
	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
		return (ValidateCode) sessionStrategy.getAttribute(request, getSessionKey(validateCodeType));
	}

	/**
	 * function:从session中移除验证码
	 * 
	 * @param request
	 * @param validateCodeType
	 */
	@Override
	public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
		sessionStrategy.removeAttribute(request, getSessionKey(validateCodeType));
	}

	/**
	 * function:构建验证码放入session时的key
	 * 
	 * @param validateCodeType
	 * @return
	 */
	private String getSessionKey(ValidateCodeType validateCodeType) {
		return SESSION_KEY_PREFIX + validateCodeType.getParamNameOnValidate().toUpperCase();
	}

}
