/**
 * 
 */
package cn.jing.security.core.validate.code;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

/**
 * @author liangjing
 *
 */
public class ImageCode {

	private BufferedImage image;

	private String code;

	private LocalDateTime expireTime;

	public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
		this.image = image;
		this.code = code;
		this.expireTime = expireTime;
	}

	// 我们一般指定图形验证码的过期时间，不会是直接去指定一个日期这样子的，而是去传递一个秒数（比如说60秒后过期，180秒后过期）
	public ImageCode(BufferedImage image, String code, int expireIn) {
		this.image = image;
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
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

	public boolean isExpired() {
		return LocalDateTime.now().isAfter(expireTime);
	}
}
