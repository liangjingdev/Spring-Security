/**
 * 
 */
package cn.jing.web.async;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * function:消息队列的监听器
 * 
 * @author liangjing
 *
 */
@Component
public class QueueListener implements ApplicationListener<ContextRefreshedEvent> {
	@Autowired
	private MockQueue mockQueue;
	@Autowired
	private DeferredResultHolder deferredResultHolder;
	private Logger logger = LoggerFactory.getLogger(QueueListener.class);

	/**
	 * function：ContextRefreshedEvent事件是整个Spring容器初始化完毕的一个事件，监听该事件就相当于当整个系统提起来之后要做的一些事情
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		new Thread(() -> {
			// 监听消息队列
			while (true) {
				if (StringUtils.isNotBlank(mockQueue.getCompleteOrder())) {
					String orderNumber = mockQueue.getCompleteOrder();
					logger.info("返回订单处理结果：" + orderNumber);
					deferredResultHolder.getMap().get(orderNumber).setResult("place order success");
					mockQueue.setCompleteOrder(null);
				} else {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
}
