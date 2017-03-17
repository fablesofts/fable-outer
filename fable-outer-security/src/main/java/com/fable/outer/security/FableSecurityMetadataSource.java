package com.fable.outer.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import com.fable.outer.service.system.resource.intf.ResourceInfoService;

/**
 * 加载资源与权限的对应关系－－ 资源权限获取器：用来取得访问某个URL或者方法所需要的权限，接口为SecurityMetadataSource
 * SecurityMetadataSource包括MethodSecurityMetadataSource和FilterInvocationSecurityMetadataSource
 * ，分别对应方法和URL资源。
 * 
 * @author 汪朝 2013-9-30
 * 
 */
@Service("fableSecurityMetadataSource")
public class FableSecurityMetadataSource implements
		FilterInvocationSecurityMetadataSource {

	@Resource(name = "resourceInfoService")
	private ResourceInfoService resourceInfoService;

	/** URL与权限对应 */
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	/**
	 * 加载所有资源与权限的关系
	 */
	@PostConstruct
	private void loadResourceDefine() {
		if (resourceMap == null) {
			resourceMap = new LinkedHashMap<String, Collection<ConfigAttribute>>();
			// map中是所有资源的url及可以访问对应资源的角色名集合
			Map<String, Object> map = this.resourceInfoService.getResourceMap();
			// 遍历map，map中的key为资源的url，value为可以访问该url的角色名组成的集合
			for (String key : map.keySet()) {
				// 临时权限列表
				Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
				@SuppressWarnings("unchecked")
				List<Object> list = (List<Object>) map.get(key);
				for (Object obj : list) {
					configAttributes.add(new SecurityConfig(obj.toString()));
				}
				// 生成<URL,ROLE>对应的MAP
				resourceMap.put(key, configAttributes);
			}
		}
	}

	/**
	 * 返回所请求资源所需要的权限
	 * 
	 * @param urlObject
	 *            此参数封装了访问的URL(请求的资源)
	 */
	public Collection<ConfigAttribute> getAttributes(Object urlObject)
			throws IllegalArgumentException {

		String requestUrl = ((FilterInvocation) urlObject).getRequestUrl();
		// 处理一下,把?后的去掉
		if (requestUrl.indexOf("?") != -1)
			requestUrl = requestUrl.substring(0, requestUrl.indexOf("?"));
		if (resourceMap == null) {
			loadResourceDefine();
		}
		// 返回的所有权限资源
		Collection<ConfigAttribute> all = new ArrayList<ConfigAttribute>();
		PathMatcher matcher = new AntPathMatcher();

		for (String url : resourceMap.keySet()) {
			// 如果有匹配的URL时
			if (matcher.match(url, requestUrl)) {
				all.addAll(resourceMap.get(url));
			}
		}
		// 如果这个URL没有配置相关的权限时，即all中没有元素时，抛出异常，否则返回all
		if(all.isEmpty())
			throw new AccessDeniedException(" 没有权限访问！");
		return all;
	}

	public static Map<String, Collection<ConfigAttribute>> getResourceMap() {
		return resourceMap;
	}

	public static void setResourceMap(
			Map<String, Collection<ConfigAttribute>> resourceMap) {
		FableSecurityMetadataSource.resourceMap = resourceMap;
	}

}
