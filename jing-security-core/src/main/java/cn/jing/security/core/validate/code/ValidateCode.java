/**
 * 
 */
package cn.jing.security.core.validate.code;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * function:验证码信息实体类(短信验证码实体类)
 * 
 * @author liangjing
 *
 */
public class ValidateCode implements Serializable {

	private static final long serialVersionUID = 1588203828504660915L;

	// String类型的验证码(随机数)
	private String code;

	// LocalDateTime类型的时间参数(验证码的过期时间点)
	private LocalDateTime expireTime;

	// 我们一般指定(图形/短信)验证码的过期时间，不会是直接去指定一个日期这样子的，而是去传递一个秒数（比如说60秒后过期，180秒后过期）
	public ValidateCode(String code, int expireIn) {
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}

	public ValidateCode(String code, LocalDateTime expireTime) {
		this.code = code;
		this.expireTime = expireTime;
	}

	/**
	 * function:判断验证码是否过期
	 * 
	 * @return
	 */
	public boolean isExpried() {
		return LocalDateTime.now().isAfter(expireTime);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(LocalDateTime expireTime) {
		this.expireTime = expireTime;
	}

}
