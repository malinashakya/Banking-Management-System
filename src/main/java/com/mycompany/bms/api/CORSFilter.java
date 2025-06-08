/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bms.api;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebFilter("/*")
public class CORSFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // Set CORS headers
        httpResponse.setHeader("Access-Control-Allow-Origin", "*");  // Allow all origins
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");  // Allowed HTTP methods
        httpResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");  // Allowed headers
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");  // Allow credentials
        // Handle preflight requests (OPTIONS method)
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        // Continue the filter chain for other requests
        chain.doFilter(request, response);
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization logic
    }
    @Override
    public void destroy() {
        //Filter cleanup logic
    }
}
