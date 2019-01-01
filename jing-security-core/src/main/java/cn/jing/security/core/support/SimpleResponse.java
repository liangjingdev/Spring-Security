/**
 * 
 */
package cn.jing.security.core.support;

/**
 * function:简单响应的封装类
 * 
 * @author liangjing
 *
 */
public class SimpleResponse {

	private Object content;

	public SimpleResponse(Object content) {
		this.content = content;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

}
