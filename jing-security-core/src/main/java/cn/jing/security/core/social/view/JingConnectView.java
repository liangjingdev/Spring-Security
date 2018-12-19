/**
 * 
 */
package cn.jing.security.core.social.view;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.AbstractView;

/**
 * function:绑定/解绑成功视图
 * 
 * @author liangjing
 *
 */
// 需要注意的是，该视图在这里不能够这样配：@Component("connect/weixinConnected")，因为这个视图在后边还想给多个视图来重用，
// 又因为这是一个绑定结果的视图，所以像QQ或者其他社交网站绑定成功后都想用这个视图，但是针对不同的社交网站，视图的名字又是不一样的，
// 所以不能够声明这样的@component注解。
public class JingConnectView extends AbstractView {

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		response.setContentType("text/html;charset=UTF-8");
		// model中如果有connections信息，说明是在做绑定处理，若没有connections信息，说明是在做解绑处理
		if (model.get("connections") == null) {
			response.getWriter().write("<h3>解绑成功</h3>");
		} else {
			response.getWriter().write("<h3>绑定成功</h3>");
		}

	}

}
