package com.fable.outer.security;

import java.util.Collection;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

/**
 * 访问决策器：用来决定用户是否拥有访问权限的关键类，其接口为AccessDecisionManager
 * 
 * @author 汪朝  2013-9-30
 * 
 */
@Service("fableAccessDecisionManager")
public class FableAccessDecisionManager implements AccessDecisionManager {

	/**
	 * 最重要的就是decide方法 ，它负责去决策你是否有权限去访问你访问的资源。
	 * 第一个参数Authentication 是你登陆的角色所具有的权限列表。
	 * 第二个参数是你访问的url。
	 * 第三个参数是访问这个url所需要的权限列表。
	 */
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes) throws AccessDeniedException,
			InsufficientAuthenticationException {
		// 所请求的资源拥有的权限(一个资源对多个权限)
		for(ConfigAttribute configAttribute :configAttributes){
			// 用户所拥有的权限authentication
			for (GrantedAuthority ga : authentication.getAuthorities()) {
				// 访问所请求资源所需要的权限与用户的权限角色是否相同
				if (configAttribute.getAttribute().equals(ga.getAuthority())) {
					//表示认证成功
					return;
				}
			}
		}
		//如果这个URL没有配置相关的权限时或者没有一个角色和访问URL的角色匹配时
		throw new AccessDeniedException(" 没有权限访问！");
	}

	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}
	/**
	 * AccessdecisionManager在Spring security中是很重要的。
	 * 
	 * 在验证部分简略提过了，所有的Authentication实现需要保存在一个GrantedAuthority对象数组中。 这就是赋予给主体的权限。
	 * GrantedAuthority对象通过AuthenticationManager 保存到
	 * Authentication对象里，然后从AccessDecisionManager读出来，进行授权判断。
	 * 
	 * Spring Security提供了一些拦截器，来控制对安全对象的访问权限，例如方法调用或web请求。
	 * 一个是否允许执行调用的预调用决定，是由AccessDecisionManager实现的。 这个 AccessDecisionManager
	 * 被AbstractSecurityInterceptor调用， 它用来作最终访问控制的决定。
	 * 这个AccessDecisionManager接口包含三个方法：
	 * 
	 * void decide(Authentication authentication, Object secureObject,
	 * List<ConfigAttributeDefinition> config) throws AccessDeniedException;
	 * boolean supports(ConfigAttribute attribute); boolean supports(Class
	 * clazz);
	 * 
	 * 从第一个方法可以看出来，AccessDecisionManager使用方法参数传递所有信息，这好像在认证评估时进行决定。
	 * 特别是，在真实的安全方法期望调用的时候，传递安全Object启用那些参数。 比如，让我们假设安全对象是一个MethodInvocation。
	 * 很容易为任何Customer参数查询MethodInvocation，
	 * 然后在AccessDecisionManager里实现一些有序的安全逻辑，来确认主体是否允许在那个客户上操作。
	 * 如果访问被拒绝，实现将抛出一个AccessDeniedException异常。
	 * 
	 * 这个 supports(ConfigAttribute) 方法在启动的时候被
	 * AbstractSecurityInterceptor调用，来决定AccessDecisionManager
	 * 是否可以执行传递ConfigAttribute。 supports(Class)方法被安全拦截器实现调用，
	 * 包含安全拦截器将显示的AccessDecisionManager支持安全对象的类型。
	 */
}
