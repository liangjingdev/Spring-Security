/**
 * 
 */
package cn.jing.security.core.validate.code.sms;

/**
 * function:短信验证码发送器接口
 * 
 * 因为不同的应用它会去使用不同的短信运营商，不同的人去使用你这个模块的时候，他们要把他们需要的那个短信供应商给配置进去，所以这部分逻辑要通过一个接口去将其封装起来，做成可配置化的形式。
 * 
 * @author liangjing
 *
 */
public interface SmsCodeSender {

	/**
	 * function:发送短信验证码到前端
	 * 
	 * @param mobile
	 * @param code
	 */
	void send(String mobile, String code);

}
