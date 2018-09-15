/**
 * 
 */
package cn.jing.security.app.validate.code.impl;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import cn.jing.security.core.validate.code.ValidateCode;
import cn.jing.security.core.validate.code.ValidateCodeException;
import cn.jing.security.core.validate.code.ValidateCodeRepository;
import cn.jing.security.core.validate.code.ValidateCodeType;

/**
 * function:基于redis的验证码存取器，避免由于没有session导致无法存取验证码的问题
 * 
 * @author liangjing
 *
 */
@Component
public class RedisValidateCodeRepository implements ValidateCodeRepository {

	@Autowired
	private RedisTemplate<Object, Object> redisTemplate;

	@Override
	public void save(ServletWebRequest request, ValidateCode code, ValidateCodeType validateCodeType) {
		redisTemplate.opsForValue().set(buildKey(request, validateCodeType), code, 30, TimeUnit.SECONDS);
	}

	@Override
	public ValidateCode get(ServletWebRequest request, ValidateCodeType validateCodeType) {
		Object value = redisTemplate.opsForValue().get(buildKey(request, validateCodeType));
		if (value == null) {
			return null;
		}
		return (ValidateCode) value;
	}

	@Override
	public void remove(ServletWebRequest request, ValidateCodeType validateCodeType) {
		redisTemplate.delete(buildKey(request, validateCodeType));
	}

	/**
	 * function:构建验证码放入redis时的key
	 * 
	 * @param request
	 * @param validateCodeType
	 * @return
	 */
	private String buildKey(ServletWebRequest request, ValidateCodeType validateCodeType) {
		String deviceId = request.getHeader("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new ValidateCodeException("请在请求头中携带deviceId参数");
		}
		return "code:" + validateCodeType.toString().toLowerCase() + ":" + deviceId;
	}

}
