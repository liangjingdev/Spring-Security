/**
 * 
 */
package cn.jing.security.core.properties;

/**
 * @author liangjing
 *
 */
public class ImageCodeProperties {

	/**
	 * 验证码宽度
	 */
	private int width = 67;
	/**
	 * 验证码高度
	 */
	private int height = 23;
	/**
	 * 验证码长度
	 */
	private int length = 4;
	/**
	 * 验证码过期时间
	 */
	private int expireIn = 60;

	/**
	 * 需要验证码的url字符串，用英文逗号隔开
	 */
	private String url;

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getExpireIn() {
		return expireIn;
	}

	public void setExpireIn(int expireIn) {
		this.expireIn = expireIn;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
