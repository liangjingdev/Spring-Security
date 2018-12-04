/**
 * 
 */
package cn.jing.security.core.validate.code.sms;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import cn.jing.security.core.properties.SecurityProperties;
import cn.jing.security.core.validate.code.ValidateCode;
import cn.jing.security.core.validate.code.ValidateCodeGenerator;

/**
 * function:短信验证码生成器
 * 
 * 短信验证码生成器可直接使用@Component注解将其声明为Spring的Bean即可，因为它不像图片验证码有多种生成方式，所以不必要将其像图片验证码生成器一样做成可配置化的形式(即用户可自定义)。
 * 
 * @author liangjing
 *
 */
@Component("smsValidateCodeGenerator")
public class SmsCodeGenerator implements ValidateCodeGenerator {

	@Autowired
	private SecurityProperties securityProperties;

	@Override
	public ValidateCode generate(ServletWebRequest request) {
		String code = RandomStringUtils.randomNumeric(securityProperties.getCode().getSms().getLength());
		return new ValidateCode(code, securityProperties.getCode().getSms().getExpireIn());
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

}
