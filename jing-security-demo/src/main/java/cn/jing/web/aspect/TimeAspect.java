/**
 * 
 */
package cn.jing.web.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * function:拦截REST服务--使用AOP进行拦截
 * 
 * @author liangjing
 *
 */
@Aspect
@Component
public class TimeAspect {

	@Around("execution(* cn.jing.web.controller.UserController.*(..))")
	public Object handleTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		System.out.println("time aspect is start.");
		// 获取前端访问API的时候所携带的参数
		for (Object object : proceedingJoinPoint.getArgs()) {
			System.out.println(object);
		}
		long startTime = System.currentTimeMillis();
		// 继续执行被拦截的方法
		Object obj = proceedingJoinPoint.proceed();
		System.out.println("time aspect 耗时：" + (System.currentTimeMillis() - startTime));
		System.out.println("time aspect finish.");
		return obj;
	}
}
