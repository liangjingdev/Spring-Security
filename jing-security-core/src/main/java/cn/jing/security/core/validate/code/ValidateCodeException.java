/**
 * 
 */
package cn.jing.security.core.validate.code;

import org.springframework.security.core.AuthenticationException;

/**
 * @author liangjing
 *
 */
public class ValidateCodeException extends AuthenticationException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6296526574183458021L;

	public ValidateCodeException(String msg) {
		super(msg);
	}

}
