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

	public ValidateCodeException(String msg) {
		super(msg);
	}

}
