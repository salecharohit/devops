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
		
    	JsonObject jsonObject = new JsonObject(); 
        int status = res.getStatus();
        String httpMethod = req.getMethod();
        jsonObject.addProperty("httpStatus",status);
        jsonObject.addProperty("path", req.getRequestURI());
        jsonObject.addProperty("httpMethod",httpMethod);
        jsonObject.addProperty("clientIP", req.getRemoteAddr());
        jsonObject.addProperty("javaMethod", req.toString());
        jsonObject.addProperty("queryString", req.getQueryString());
        
        Enumeration<String> headerNames = req.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = req.getHeader(key);
            //Filter any sensitive information from being sent to logs
            if(key.equalsIgnoreCase("JSESSIONID")) {
            	jsonObject.addProperty(key,"");
            }
            else {
                jsonObject.addProperty(key,value);
            }
        }
        
        Enumeration<String> parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String key = (String) parameterNames.nextElement();
            String value = req.getParameter(key);
            jsonObject.addProperty(key,value);
        }
        if (!httpMethod.equalsIgnoreCase("GET")) {
        	try {
        		
        		String  requestBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
	        	jsonObject. addProperty("postRequest",requestBody);
			} catch (IOException e) {
				logger.error(e.getStackTrace().toString());
			}
        }
        logger.info(jsonObject.toString());

        
        
        chain.doFilter(request, response);
		
	}

}
