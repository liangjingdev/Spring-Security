/**
 * 
 */
package cn.jing.security.core.validate.code;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import cn.jing.security.core.properties.SecurityProperties;

/**
 * @author liangjing
 *
 */
public class ImageCodeGenerator implements ValidateCodeGenerator {

	private static final String IMAGE_WIDTH_NAME = "width";
	private static final String IMAGE_HEIGHT_NAME = "height";
	private static final Integer MAX_COLOR_VALUE = 255;

	@Autowired
	private SecurityProperties securityProperties;

	/*
	 * (non-Javadoc)
	 * 
	 * @see cn.jing.security.core.validate.code.ValidateCodeGenerator#generate(org.
	 * springframework.web.context.request.ServletWebRequest)
	 */
	@Override
	public ImageCode generate(ServletWebRequest request) {

		// 请求级配置（最后一个参数是defaultValue，即如果在请求里获取不到值的话，那么就取最一个参数的值来进行赋值）
		int width = ServletRequestUtils.getIntParameter(request.getRequest(), IMAGE_WIDTH_NAME,
				securityProperties.getCode().getImage().getWidth());
		int height = ServletRequestUtils.getIntParameter(request.getRequest(), IMAGE_HEIGHT_NAME,
				securityProperties.getCode().getImage().getHeight());
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics g = image.getGraphics();

		Random random = new Random();

		// 生成画布
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);
		g.setFont(new Font("Times New Roman", Font.ITALIC, 20));
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}

		// 生成数字验证码
		StringBuilder sRand = new StringBuilder();
		for (int i = 0; i < securityProperties.getCode().getImage().getLength(); i++) {
			String rand = String.valueOf(random.nextInt(10));
			sRand.append(rand);
			g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
			g.drawString(rand, 13 * i + 6, 16);
		}

		g.dispose();

		return new ImageCode(image, sRand.toString(), securityProperties.getCode().getImage().getExpireIn());
	}

	/**
	 * 生成随机背景条纹
	 *
	 * @param fc
	 *            前景色
	 * @param bc
	 *            背景色
	 * @return RGB颜色
	 */
	private Color getRandColor(int fc, int bc) {
		Random random = new Random();
		if (fc > MAX_COLOR_VALUE) {
			fc = MAX_COLOR_VALUE;
		}
		if (bc > MAX_COLOR_VALUE) {
			bc = MAX_COLOR_VALUE;
		}
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}

	public SecurityProperties getSecurityProperties() {
		return securityProperties;
	}

	public void setSecurityProperties(SecurityProperties securityProperties) {
		this.securityProperties = securityProperties;
	}

}
