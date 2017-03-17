package com.fable.outer.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.AbstractSecurityInterceptor;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;

/**
 * URL过滤器或方法拦截器：用来拦截URL或者方法资源对其进行验证，其抽象基类为AbstractSecurityInterceptor
 * 
 * 访问控制说明：
 * 
 * 由于我们配置了访问控制（授权）的默认拦截器org.springframework.security.web.access.intercept.FilterSecurityInterceptor。
 * 其主要业务方法是InterceptorStatusToken beforeInvocation(Object object)
 * 
 * 该方法会将URL传给SecurityMetadataSource获取匹配该URL所有参数ConfigAttribute（拥有权限的角色）的集合。
 * 
 * 如果该用户尚未认证（登录），或拦截器配置了“始终认证”，则拦截器会将该用户的登录信息（未登录则跳转到登陆页面）重新认证，并加载角色信息。
 * 
 * 随后将用户认证信息（Authentication），用户请求访问的URL，以及配置集合（Collection<ConfigAttribute>）
 * 交由accessDecisionManager的decide方法。通过则方法继续，否则抛出AccessDeniedException。
 * 
 * 调用runAsManager尝试转换认证信息，这是为了方便适应两层访问控制架构。runAsManager就可以将外部公开的认证，转换为内部认证，
 * 继续之后的访问。
 * 
 * 之后返回包含访问拦截信息的对象InterceptorStatusToken。以便afterInvocation(InterceptorStatusToken, Object)方法运行。
 * 
 * @author 汪朝  2013-9-30
 * 
 */
public class FableSecurityFilter extends AbstractSecurityInterceptor implements Filter {
	/**
	 * 与applicationContext-security.xml里的认证过滤器Filter的属性securityMetadataSource对应，
	 * 其他的两个组件，已经在AbstractSecurityInterceptor定义
	 * 
	 */
	private FilterInvocationSecurityMetadataSource securityMetadataSource;

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.securityMetadataSource;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		FilterInvocation fi = new FilterInvocation(request, response, chain);
		invoke(fi);
	}

	private void invoke(FilterInvocation fi) throws IOException, ServletException {
		
		// 解决输入路径为/时，直接跳转到login.jsp
		if ("/".equalsIgnoreCase(fi.getRequestUrl())) {
			fi.getHttpResponse().sendRedirect(fi.getRequest().getContextPath()+"/login.jsp");
			return;
		}
		/* object为FilterInvocation对象
		 1.获取请求资源的权限
		 执行Collection<ConfigAttribute> attributes =
		 SecurityMetadataSource.getAttributes(object);
		 2.是否拥有权限
		 获取安全主体，可以强制转换为UserDetails的实例
		 1) UserDetails
		 Authentication authenticated = authenticateIfRequired();
		 this.accessDecisionManager.decide(authenticated, object, attributes);
		 用户拥有的权限
		 2) GrantedAuthority
		 Collection<GrantedAuthority> authenticated.getAuthorities()*/
		InterceptorStatusToken token = super.beforeInvocation(fi);
		try {
			fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
		} finally {
			super.afterInvocation(token, null);
		}
	}

	public FilterInvocationSecurityMetadataSource getSecurityMetadataSource() {
		return securityMetadataSource;
	}

	public void setSecurityMetadataSource(FilterInvocationSecurityMetadataSource securityMetadataSource) {
		this.securityMetadataSource = securityMetadataSource;
	}

	public void init(FilterConfig arg0) throws ServletException {
		
	}

	public void destroy() {

	}

	@Override
	public Class<? extends Object> getSecureObjectClass() {
		// 下面的MyAccessDecisionManager的supports方面必须放回true,否则会提醒类型错误
		return FilterInvocation.class;
	}
}
