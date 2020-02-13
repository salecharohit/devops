package com.rohitsalecha.springular.devops.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rohitsalecha.springular.devops.interceptor.RequestResponseLoggingFilter;

@Configuration
public class WebConfig implements WebMvcConfigurer  {

//    @Bean
//    public ServletRegistrationBean dispatcherRegistration() {
//        return new ServletRegistrationBean(dispatcherServlet());
//    }
//
//    @Bean(name = DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_BEAN_NAME)
//    public DispatcherServlet dispatcherServlet() {
//        return new LoggableDispatcherServlet();
//    }
	
	
	@Bean
	public FilterRegistrationBean<RequestResponseLoggingFilter> loggingFilter(){
	    FilterRegistrationBean<RequestResponseLoggingFilter> registrationBean 
	      = new FilterRegistrationBean<>();
	    registrationBean.setFilter(new RequestResponseLoggingFilter());
	    registrationBean.addUrlPatterns("/**");
	    return registrationBean;    
	}
}
