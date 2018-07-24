/**
 * 
 */
package cn.jing.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * @author liangjing
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserControllerTest {

	// 注入一个web应用环境(容器)
	@Autowired
	private WebApplicationContext wac;

	// MVC环境测试对象
	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
	}

	@Test
	public void whenQuerySuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/user").param("username", "liangjing").param("age", "18")
				.param("ageTo", "60").param("xxx", "yyy")
				// 查询第3页的数据，每页返回15条数据，然后根据年龄降序排序
				//.param("size", "15").param("page", "3").param("sort", "age,desc")
				.contentType(MediaType.APPLICATION_JSON_UTF8)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(3));
	}
}
