/**
 * 
 */
package cn.jing.security.core.validate.code.impl;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import cn.jing.security.core.validate.code.ValidateCode;
import cn.jing.security.core.validate.code.ValidateCodeException;
import cn.jing.security.core.validate.code.ValidateCodeGenerator;
import cn.jing.security.core.validate.code.ValidateCodeProcessor;
import cn.jing.security.core.validate.code.ValidateCodeRepository;
import cn.jing.security.core.validate.code.ValidateCodeType;

/**
 * function:抽象的验证码处理器
 * 
 * 由于这两种验证码在创建的时候的主干逻辑都是相同的，都是三步，分别是：验证码的生成，验证码的保存，验证码的发送。
 * 就像这种主干逻辑是相同的，但是其中某一步的实现步骤可能是不一样的，比如发送那一步，图形验证码是写到输出流里面，而短信验证码是使用一个发送器将其发送出去的，这种代码可以使用模版方法模式将其抽象出来。
 * 
 * @author liangjing
 *
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCode> implements ValidateCodeProcessor {

	/**
	 * 收集系统中所有的 {@link ValidateCodeGenerator} 接口的实现。
	 * 这是Spring的一个特性（依赖查找），就是在项目启动的时候会自动去收集系统中 {ValidateCodeGenerator}
	 * 接口所有的实现类对象，找到以后会以它的Bean的名字作为key放入到Map集合中，
	 * 以当前系统来说，这个Map集合中有两个数据元素，key分别是smsCodeGenerator和imageCodeGenerator。
	 * 这是Spring开发中常见的一个技巧，叫做依赖搜索，定向去搜索某一个接口的实现。
	 */
	@Autowired
	private Map<String, ValidateCodeGenerator> validateCodeGenerators;

	@Autowired
	private ValidateCodeRepository validateCodeRepository;

	/**
	 * function:创建验证码
	 */
	@Override
	public void create(ServletWebRequest request) throws Exception {
		// 主干逻辑第一步：验证码的生成
		C validateCode = generate(request);
		// 主干逻辑第二步：验证码的保存
		save(request, validateCode);
		// 主干逻辑第三步：验证码的发送
		send(request, validateCode);
	}

	/**
	 * function:生成验证码
	 * 
	 * @param request
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private C generate(ServletWebRequest request) {
		String type = getValidateCodeType(request).toString().toLowerCase();
		// 由验证码的类型去获取相对应的验证码生成器接口实现类
		String generatorName = type + ValidateCodeGenerator.class.getSimpleName();
		ValidateCodeGenerator validateCodeGenerator = validateCodeGenerators.get(generatorName);
		if (validateCodeGenerator == null) {
			throw new ValidateCodeException("验证码生成器" + generatorName + "不存在");
		}
		return (C) validateCodeGenerator.generate(request);
	}

	/**
	 * function:保存验证码
	 * 
	 * @param request
	 * @param validateCode
	 */
	private void save(ServletWebRequest request, C validateCode) {
		// 后面浏览器端的session是统一存放在redis中的，而浏览器端的验证码信息又是存放在session中的，所以验证码信息也就是存放在redis中的，
		// 由于存放在redis中的对象一定要是可序列化的且对象里面的属性也要是可序列化的，但是ImageCode对象中的BufferedImage属性是
		// java本身就有的对象，而且并没有实现序列化接口，所以ImageCode对象是不能够放到session中的，又由于校验的时候只需要校验码那
		// 一串字符串即可，并不需要图片信息，所以干脆只需要将校证码字符串存入session中即可。
		ValidateCode code = new ValidateCode(validateCode.getCode(), validateCode.getExpireTime());
		validateCodeRepository.save(request, code, getValidateCodeType(request));
	}

	/**
	 * function:发送校验码，由子类实现
	 * 
	 * @param request
	 * @param validateCode
	 * @throws Exception
	 */
	protected abstract void send(ServletWebRequest request, C validateCode) throws Exception;

	/**
	 * function:根据请求的url所初始化的ValidateCodeProcessor接口实现类来获取验证码的类型
	 * 
	 * ValidateCodeController->ValidateCodeProcessorHolder->ValidateCodeProcessor
	 * 
	 * @param request
	 * @return
	 */
	private ValidateCodeType getValidateCodeType(ServletWebRequest request) {
		String type = StringUtils.substringBefore(getClass().getSimpleName(), "CodeProcessor");
		return ValidateCodeType.valueOf(type.toUpperCase());
	}

	/**
	 * function:校验验证码
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void validate(ServletWebRequest request) {

		ValidateCodeType codeType = getValidateCodeType(request);

		C codeInSession = (C) validateCodeRepository.get(request, codeType);

		String codeInRequest;
		try {
			codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
					codeType.getParamNameOnValidate());
		} catch (ServletRequestBindingException e) {
			throw new ValidateCodeException("获取验证码的值失败");
		}

		if (StringUtils.isBlank(codeInRequest)) {
			throw new ValidateCodeException(codeType + "验证码的值不能为空");
		}

		if (codeInSession == null) {
			throw new ValidateCodeException(codeType + "验证码不存在");
		}

		if (codeInSession.isExpried()) {
			validateCodeRepository.remove(request, codeType);
			throw new ValidateCodeException(codeType + "验证码已过期");
		}

		if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
			throw new ValidateCodeException(codeType + "验证码不匹配");
		}

		// 移除验证码
		validateCodeRepository.remove(request, codeType);

	}

}
