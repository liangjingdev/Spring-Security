/**
 * 
 */
package cn.jing.security.core.validate.code.image;

import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

import cn.jing.security.core.validate.code.ValidateCode;

/**
 * function:图片验证码实体类
 * 
 * @author liangjing
 *
 */
public class ImageCode extends ValidateCode {

	private static final long serialVersionUID = -6020470039852318468L;

	// BufferedImage类型的图片
	private BufferedImage image;

	public ImageCode(BufferedImage image, String code, int expireIn) {
		// 在调用ImageCode实体类的构造函数的同时去调用父类的有参构造函数，把父类中的这两个属性的值给赋上
		super(code, expireIn);
		this.image = image;
	}

	public ImageCode(BufferedImage image, String code, LocalDateTime expireTime) {
		super(code, expireTime);
		this.image = image;
	}

	public BufferedImage getImage() {
		return image;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

}
