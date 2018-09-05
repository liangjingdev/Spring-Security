/**
 * 
 */
package cn.jing.security.core;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import cn.jing.security.core.properties.SecurityProperties;

/**
 * function:核心模块配置类
 * 
 * @EnableConfigurationProperties：使得SecurityProperties这个读取application.properties中配置项的类生效
 * @author liangjing
 *
 */
@Configuration
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityCoreConfig {

}
