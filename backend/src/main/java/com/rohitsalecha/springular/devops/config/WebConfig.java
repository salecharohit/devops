package com.rohitsalecha.springular.devops.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.rohitsalecha.springular.devops.interceptor.RequestInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer  {
	@Autowired
	private RequestInterceptor requestInterceptor;
	
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(requestInterceptor).addPathPatterns("/**");
    }
}
