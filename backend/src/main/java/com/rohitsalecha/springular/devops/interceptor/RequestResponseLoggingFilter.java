package com.rohitsalecha.springular.devops.interceptor;

import java.io.IOException;
import java.util.Enumeration;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import com.google.gson.JsonObject;

@Component
@Order(1)
public class RequestResponseLoggingFilter implements Filter {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;		
        HttpServletRequest cachedRequest = new ContentCachingRequestWrapper(req);
		
    	JsonObject jsonObject = new JsonObject(); 
        int status = res.getStatus();
        String httpMethod = cachedRequest.getMethod();
        jsonObject.addProperty("httpStatus",status);
        jsonObject.addProperty("path", cachedRequest.getRequestURI());
        jsonObject.addProperty("httpMethod",httpMethod);
        jsonObject.addProperty("clientIP", cachedRequest.getRemoteAddr());
        jsonObject.addProperty("javaMethod", cachedRequest.toString());
        jsonObject.addProperty("queryString", cachedRequest.getQueryString());
        
        Enumeration<String> headerNames = cachedRequest.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = cachedRequest.getHeader(key);
            //Filter any sensitive information from being sent to logs
            if(key.equalsIgnoreCase("JSESSIONID")) {
            	jsonObject.addProperty(key,"");
            }
            else {
                jsonObject.addProperty(key,value);
            }
        }
        
        Enumeration<String> parameterNames = cachedRequest.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = cachedRequest.getParameter(key);
            jsonObject.addProperty(key,value);
        }
        if (!httpMethod.equalsIgnoreCase("GET")) {
        	try {
        		String  requestBody = cachedRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	        	jsonObject. addProperty("data",requestBody);
			} catch (IOException e) {
				logger.error(e.getStackTrace().toString());
			}
        }
        logger.info(jsonObject.toString());
        
        chain.doFilter(request, response);
		
	}

}
