/**
 * 
 */
package cn.jing.web.async;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * function:使用DeferredResult异步处理Rest服务
 * 
 * @author liangjing
 *
 */
@Component
public class DeferredResultHolder {

	// key可以理解为订单号，每一个订单号对应一个处理结果(DeferredResult<String>，DeferredResult就是去处理，里面放的是处理结果)
	private Map<String, DeferredResult<String>> map = new HashMap<String, DeferredResult<String>>();

	public Map<String, DeferredResult<String>> getMap() {
		return map;
	}

	public void setMap(Map<String, DeferredResult<String>> map) {
		this.map = map;
	}

}
