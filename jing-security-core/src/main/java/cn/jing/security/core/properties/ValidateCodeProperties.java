/**
 * 
 */
package cn.jing.security.core.properties;

/**
 * function:验证码的相关配置项
 * 
 * @author liangjing
 *
 */
public class ValidateCodeProperties {

	/**
	 * 图形验证码配置
	 */
	private ImageCodeProperties image = new ImageCodeProperties();
	/**
	 * 短信验证码配置
	 */
	private SmsCodeProperties sms = new SmsCodeProperties();

	public ImageCodeProperties getImage() {
		return image;
	}

	public void setImage(ImageCodeProperties image) {
		this.image = image;
	}

	public SmsCodeProperties getSms() {
		return sms;
	}

	public void setSms(SmsCodeProperties sms) {
		this.sms = sms;
	}

}
