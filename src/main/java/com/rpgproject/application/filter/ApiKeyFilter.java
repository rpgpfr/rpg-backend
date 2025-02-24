package com.rpgproject.application.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApiKeyFilter implements Filter {

	@Value("${BACKEND_API_KEY}")
	private String apiKey;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		System.out.println("Hello world");

		HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
		String requestApiKey = httpRequest.getHeader("backend-api-key");
		System.out.println("request API key: " + requestApiKey);
		if (!StringUtils.equals(requestApiKey, apiKey)) {
			((HttpServletResponse) servletResponse).sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid API key");
			throw new ServletException("Missing or invalid API key");
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

}
