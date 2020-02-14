package com.rohitsalecha.springular.devops.config;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import com.google.gson.JsonObject;

//http://www.programmergirl.com/spring-http-request-logging/ Only working Solution
public class RequestResponseLoggingFilter implements Filter {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static int MAX_PAYLAOD_LENGTH = 10000;
    
    public void doFilter(ServletRequest request, ServletResponse response,
    	      FilterChain chain) throws IOException, ServletException {
    	        if((request instanceof HttpServletRequest) 
    	          && !(request instanceof ContentCachingRequestWrapper)) {
    	            request = new ContentCachingRequestWrapper((HttpServletRequest) request);
    	        }
    	        try {
    	            chain.doFilter(request, response);
    	        } finally {
    	            if(request instanceof HttpServletRequest) {
    	            	logRequestData((HttpServletRequest)request,(HttpServletResponse)response);
    	            }
    	        }
    	    }
    	 
//    	    public void performRequestAudit(HttpServletRequest httpRequest) {
//    	        ContentCachingRequestWrapper wrapper =
//    	          WebUtils.getNativeRequest(httpRequest, ContentCachingRequestWrapper.class);
//    	        String payload = "";
//    	        if (wrapper != null) {
//    	            byte[] requestBuffer = wrapper.getContentAsByteArray();
//    	            if (requestBuffer.length > 0) {
//    	                int length = Math.min(requestBuffer.length, MAX_PAYLAOD_LENGTH);
//    	                try {
//    	                    payload = new String(requestBuffer,
//    	                      0, length, wrapper.getCharacterEncoding());
//    	                }
//    	                catch (UnsupportedEncodingException unex) {
//    	                    payload = "[Unsupported-Encoding]";
//    	                }
//    	            }
//    	        }
//	    		HttpHeaders headers = new ServletServerHttpRequest(httpRequest).getHeaders();
//	    		logger.info("{}|{}", payload, headers);
//    	    }

    
    		public void logRequestData(HttpServletRequest httpRequest,HttpServletResponse httpResponse) {
    	        ContentCachingRequestWrapper wrapper =
    	        WebUtils.getNativeRequest(httpRequest, ContentCachingRequestWrapper.class);    			
    		   	JsonObject jsonObject = new JsonObject(); 
    	        int status = httpResponse.getStatus();
    	        String httpMethod = wrapper.getMethod();
    	        jsonObject.addProperty("httpStatus",status);
    	        jsonObject.addProperty("path", wrapper.getRequestURI());
    	        jsonObject.addProperty("httpMethod",httpMethod);
    	        jsonObject.addProperty("clientIP", wrapper.getRemoteAddr());
    	        jsonObject.addProperty("javaMethod", wrapper.toString());
    	        jsonObject.addProperty("queryString", wrapper.getQueryString());
    	        
    	        Enumeration<String> headerNames = wrapper.getHeaderNames();
    	        while (headerNames.hasMoreElements()) {
    	            String key = (String) headerNames.nextElement();
    	            String value = wrapper.getHeader(key);
    	            //Filter any sensitive information from being sent to logs
    	            if(key.equalsIgnoreCase("JSESSIONID")) {
    	            	jsonObject.addProperty(key,"");
    	            }
    	            else {
    	                jsonObject.addProperty(key,value);
    	            }
    	        }
    	        
    	        Enumeration<String> parameterNames = wrapper.getParameterNames();
    	        while (parameterNames.hasMoreElements()) {
    	            String key = (String) parameterNames.nextElement();
    	            String value = wrapper.getParameter(key);
    	            jsonObject.addProperty(key,value);
    	        }
    	        if (!httpMethod.equalsIgnoreCase("GET")) {
    	        	String payload = "";
					if (wrapper != null) {
					    byte[] requestBuffer = wrapper.getContentAsByteArray();
					    if (requestBuffer.length > 0) {
					        int length = Math.min(requestBuffer.length, MAX_PAYLAOD_LENGTH);
					        try {
					            payload = new String(requestBuffer,
					              0, length, wrapper.getCharacterEncoding());
					        }
					        catch (UnsupportedEncodingException unex) {
					            payload = "[Unsupported-Encoding]";
					        }
					    }
					}    	        		
					jsonObject. addProperty("data",payload);
    	        }
    	        logger.info(jsonObject.toString());

    			
    		}
}
