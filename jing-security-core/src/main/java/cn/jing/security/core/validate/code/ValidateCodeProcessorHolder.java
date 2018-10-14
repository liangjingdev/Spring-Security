/**
 * 
 */
package cn.jing.security.core.validate.code;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * function:验验码处理器的管理器
 * 
 * @author liangjing
 *
 */
@Component
public class ValidateCodeProcessorHolder {

	@Autowired
	private Map<String, ValidateCodeProcessor> validateCodeProcessors;

	/**
	 * function:根据验证码的类型去获取对应的验证码处理器
	 * 
	 * @param type
	 * @return
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(ValidateCodeType type) {
		return findValidateCodeProcessor(type.toString().toLowerCase());
	}

	/**
	 * function:根据验证码的类型去获取对应的验证码处理器
	 * 
	 * @param type
	 * @return
	 */
	public ValidateCodeProcessor findValidateCodeProcessor(String type) {
		String name = type.toLowerCase() + ValidateCodeProcessor.class.getSimpleName();
		ValidateCodeProcessor processor = validateCodeProcessors.get(name);
		if (processor == null) {
			throw new ValidateCodeException("验证码处理器" + name + "不存在");
		}
		return processor;
	}

}
