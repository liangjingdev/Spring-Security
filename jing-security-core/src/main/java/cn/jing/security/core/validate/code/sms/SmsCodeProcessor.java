/**
 * 
 */
package cn.jing.security.core.validate.code.sms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import cn.jing.security.core.properties.SecurityConstants;
import cn.jing.security.core.validate.code.ValidateCode;
import cn.jing.security.core.validate.code.impl.AbstractValidateCodeProcessor;

/**
 * function:短信验证码处理器
 * 
 * @author liangjing
 *
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCode> {

	/**
	 * 短信验证码发送器
	 */
	@Autowired
	private SmsCodeSender smsCodeSender;

	/**
	 * function:发送短信验证码，调用短信验证码发送器进行发送
	 */
	@Override
	protected void send(ServletWebRequest request, ValidateCode validateCode) throws Exception {
		String paramName = SecurityConstants.DEFAULT_PARAMETER_NAME_MOBILE;
		String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), paramName);
		smsCodeSender.send(mobile, validateCode.getCode());
	}

}
