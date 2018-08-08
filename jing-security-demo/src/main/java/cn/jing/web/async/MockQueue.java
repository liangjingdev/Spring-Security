/**
 * 
 */
package cn.jing.web.async;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * function:模拟消息队列的对象
 * 
 * @author liangjing
 *
 */
@Component
public class MockQueue {

	private Logger logger = LoggerFactory.getLogger(MockQueue.class);
	// 代表下单的消息(当placeOrder有值的时候，表示接收到下单的消息)
	private String placeOrder;
	// 代表订单完成的消息(当completeOrder有值的时候，表示接收到订单完成的消息)
	private String completeOrder;
	public String getPlaceOrder() {
		return placeOrder;
	}
	public void setPlaceOrder(String placeOrder) throws Exception {
		new Thread(() -> {
			logger.info("接到下单请求" + placeOrder);
			// 模拟下单逻辑处理的过程
			try {
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
			this.completeOrder = placeOrder;
			logger.info("下单请求处理完毕");
		}).start();
	}
	public String getCompleteOrder() {
		return completeOrder;
	}
	public void setCompleteOrder(String completeOrder) {
		this.completeOrder = completeOrder;
	}
}
