package com.rohitsalecha.springular.devops.interceptor;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.google.gson.JsonObject;

@Component
public class RequestInterceptor implements HandlerInterceptor{
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean preHandle(HttpServletRequest request,HttpServletResponse  response){
    	log(request,response);
        return true;
    }
    
    private void log(HttpServletRequest requestToCache, HttpServletResponse responseToCache) {
    	System.out.println("Inside Interceptor");
    	logger.info("Inside Interceptor");
        int status = responseToCache.getStatus();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("httpStatus", status);
        jsonObject.addProperty("path", requestToCache.getRequestURI());
        jsonObject.addProperty("httpMethod", requestToCache.getMethod());
        jsonObject.addProperty("clientIP", requestToCache.getRemoteAddr());
        if (status > 299) {
            String requestBody = null;
            try {
                requestBody = requestToCache.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            jsonObject.addProperty("requestBody", requestBody);
            jsonObject.addProperty("requestParams", requestToCache.getQueryString());
        }
        logger.info(jsonObject.toString());
    }
}
