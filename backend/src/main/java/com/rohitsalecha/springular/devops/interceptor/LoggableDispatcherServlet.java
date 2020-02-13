package com.rohitsalecha.springular.devops.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import com.google.gson.JsonObject;

//https://stackoverflow.com/a/39207422
public class LoggableDispatcherServlet extends DispatcherServlet {

	private static final long serialVersionUID = -1814726843560189697L;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (!(request instanceof ContentCachingRequestWrapper)) {
            request = new ContentCachingRequestWrapper(request);
        }
        if (!(response instanceof ContentCachingResponseWrapper)) {
            response = new ContentCachingResponseWrapper(response);
        }
        HandlerExecutionChain handler = getHandler(request);

        try {
            super.doDispatch(request, response);
        } finally {
            log(request, response, handler);
            updateResponse(response);
        }
    }
    
    private void log(HttpServletRequest requestToCache, HttpServletResponse responseToCache, HandlerExecutionChain handler) {
        JsonObject jsonObject = new JsonObject();
        int status = responseToCache.getStatus();
        jsonObject.addProperty("httpStatus",status);
        jsonObject.addProperty("path", requestToCache.getRequestURI());
        jsonObject.addProperty("httpMethod", requestToCache.getMethod());
        jsonObject.addProperty("clientIP", requestToCache.getRemoteAddr());
        jsonObject.addProperty("javaMethod", handler.toString());
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

    private String getResponsePayload(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {

            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                int length = Math.min(buf.length, 5120);
                try {
                    return new String(buf, 0, length, wrapper.getCharacterEncoding());
                }
                catch (UnsupportedEncodingException ex) {
                    // NOOP
                }
            }
        }
        return "[unknown]";
    }

    private void updateResponse(HttpServletResponse response) throws IOException {
        ContentCachingResponseWrapper responseWrapper =
            WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        responseWrapper.copyBodyToResponse();
    }    
    
}
