/**
 * 
 */
package cn.jing.web.async;

import java.util.concurrent.Callable;

import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * function:异步处理的控制器
 * 
 * @author liangjing
 *
 */
@RestController
public class AsyncController {

	@Autowired
	private MockQueue mockQueue;
	@Autowired
	private DeferredResultHolder deferredResultHolder;

	private Logger logger = LoggerFactory.getLogger(AsyncController.class);

	/**
	 * function:标准的同步处理的方式
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/order1")
	public String order1() throws Exception {
		logger.info("主线程开始");
		// 让当前线程睡眠1秒钟，此处可以理解为下单的逻辑
		Thread.sleep(1000);
		logger.info("主线程返回");
		return "success";
	}

	/**
	 * function:使用Callable异步处理Rest服务
	 * 
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/order2")
	public Callable<String> order2() throws Exception {
		logger.info("主线程开始");
		Callable<String> result = new Callable<String>() {
			@Override
			public String call() throws Exception {
				logger.info("副线程开始");
				// 让当前线程睡眠1秒钟，此处可以理解为下单的逻辑
				Thread.sleep(1000);
				logger.info("副线程返回");
				return "success";
			}
		};
		logger.info("主线程返回");
		return result;
	}

	/**
	 * function:使用DeferredResult异步处理Rest服务
	 * 
	 * @return DeferredResult<String> 订单处理结果
	 * @throws Exception
	 */
	@GetMapping("/order3")
	public DeferredResult<String> order3() throws Exception {
		logger.info("主线程开始");

		// 生成随机订单号
		String orderNumber = RandomStringUtils.randomNumeric(8);
		// 将订单号放到消息队列中
		mockQueue.setPlaceOrder(orderNumber);

		DeferredResult<String> result = new DeferredResult<>();
		deferredResultHolder.getMap().put(orderNumber, result);
		logger.info("主线程返回");
		return result;
	}
}
