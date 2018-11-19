/**
 * 
 */
package cn.jing.security.core.validate.code;

import cn.jing.security.core.properties.SecurityConstants;

/**
 * 
 * function:验证码类型
 * 
 * @author liangjing
 *
 */
public enum ValidateCodeType {

	/**
	 * 短信验证码
	 */
	SMS {
		@Override
		public String getParamNameOnValidate() {
			return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_SMS;
		}
	},
	/**
	 * 图片验证码
	 */
	IMAGE {
		@Override
		public String getParamNameOnValidate() {
			return SecurityConstants.DEFAULT_PARAMETER_NAME_CODE_IMAGE;
		}
	};

	/**
	 * function:校验时从请求中获取的参数的名字
	 * 
	 * @return
	 */
	public abstract String getParamNameOnValidate();

}
