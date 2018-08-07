package cn.jing.service.impl;

import org.springframework.stereotype.Service;

import cn.jing.service.HelloService;

/**
 * 
 * @author liangjing
 *
 */
@Service
public class HelloServiceImpl implements HelloService {

	@Override
	public String greeting(String name) {
		System.out.println("greeting");
		return "hello" + name;
	}

}
