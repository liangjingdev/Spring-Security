/**
 * 
 */
package cn.jing.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import cn.jing.dto.User;
import cn.jing.dto.UserQueryCondition;
import cn.jing.exception.UserNotExistException;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * @author liangjing
 *
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

	@GetMapping
	@JsonView(User.UserSimpleView.class)
	@ApiOperation(value = "用户查询服务")
	public List<User> query(UserQueryCondition condition,
			@PageableDefault(page = 2, size = 17, sort = "username,asc") Pageable pageable) {

		System.out.println(ReflectionToStringBuilder.toString(condition, ToStringStyle.MULTI_LINE_STYLE));

		System.out.println(pageable.getPageSize());
		System.out.println(pageable.getPageNumber());
		System.out.println(pageable.getSort());

		List<User> users = new ArrayList<>();
		users.add(new User());
		users.add(new User());
		users.add(new User());
		return users;
	}

	/**
	 * function:@PathVariable表示映射URL片段到Java方法的参数（在url声明中可以使用正则表达式）
	 * 下面的方法URL片段进行了正则表达式的验证，ID只能是数字。
	 * 
	 * @param id
	 * @return
	 */
	@GetMapping("/{id:\\d+}")
	@JsonView(User.UserDetailView.class)
	@ApiImplicitParam(name = "id", value = "用户id")
	public User getInfo(@PathVariable String id) {

		// 测试自定义异常处理类
		// throw new UserNotExistException(id);

		System.out.println("进入getInfo服务");
		User user = new User();
		user.setUsername("tom");
		return user;
	}

	/**
	 * function:用户创建请求
	 * 
	 * @param user
	 * @param errors
	 * @return
	 */
	@PostMapping
	public User create(@Valid @RequestBody User user, BindingResult errors) {

		// 如果发生检验错误，那么就将所有发生错误的信息逐个打印出来
		if (errors.hasErrors()) {
			errors.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
		}

		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getBirthday());
		user.setId("1");
		return user;
	}

	/**
	 * function:用户修改请求
	 * 
	 * @param user
	 * @param errors
	 * @return
	 */
	@PutMapping("/{id:\\d+}")
	public User update(@Valid @RequestBody User user, BindingResult errors) {

		// 如果发生检验错误，那么就将所有发生错误的信息逐个打印出来
		if (errors.hasErrors()) {
			errors.getAllErrors().forEach(error -> {
				/*
				 * // 在实体对象中自定义错误消息后就不需要这么麻烦了 // 强转为FieldError对象 FieldError fieldError =
				 * (FieldError) error; // 打印出错误域名称以及其错误信息 String message = fieldError.getField()
				 * + " " + error.getDefaultMessage();
				 */
				System.out.println(error.getDefaultMessage());
			});
		}

		System.out.println(user.getId());
		System.out.println(user.getUsername());
		System.out.println(user.getPassword());
		System.out.println(user.getBirthday());
		user.setId("1");
		return user;
	}

	/**
	 * function:用户删除请求
	 * 
	 * @param id
	 */
	@DeleteMapping("/{id:\\d+}")
	public void delete(@PathVariable String id) {
		System.out.println(id);
	}

}
