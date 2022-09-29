package com.hubert.downloader.auth;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Order(-999)
public class CorsFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Origin", "*");
        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, HEAD");
        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Headers", "Origin, Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers");
        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        ((HttpServletResponse)servletResponse).addHeader("Access-Control-Allow-Credentials", "true");
        ((HttpServletResponse)servletResponse).addIntHeader("Access-Control-Max-Age", 10);

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
