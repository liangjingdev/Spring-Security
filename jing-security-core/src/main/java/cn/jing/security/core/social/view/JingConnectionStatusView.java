/**
 * 
 */
package cn.jing.security.core.social.view;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * function:社交账号绑定状态视图
 * 
 * @author liangjing
 *
 */
@Component("connect/status") // 注意组件的名字要写成"connect/status"
public class JingConnectionStatusView extends AbstractView {

	// 将从model中获取到的数据转化成json格式的字符串渲染到视图上
	@Autowired
	private ObjectMapper objectMapper;

	/*
	 * function:渲染视图
	 */
	@SuppressWarnings("unchecked")
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		Map<String, List<Connection<?>>> connections = (Map<String, List<Connection<?>>>) model.get("connectionMap");

		Map<String, Boolean> result = new HashMap<>();

		for (String key : connections.keySet()) {
			result.put(key, CollectionUtils.isNotEmpty(connections.get(key)));
		}

		response.setContentType("application/json;charset=UTF-8");
		response.getWriter().write(objectMapper.writeValueAsString(result));
	}

}
