package com.hubert.downloader.auth;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class AuthFilter implements Filter {

    private Boolean isValidRequest(final HttpServletRequest request) {
        return  request.getHeader("Authorization") != null &&
                request.getHeader("Authorization").startsWith("Bearer ");
    }

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        if (!isValidRequest(request)) {
            servletResponse.getOutputStream().write("{\"message\": \"Unauthorized\"}".getBytes(StandardCharsets.UTF_8));
            ((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
            ((HttpServletResponse) servletResponse).setStatus(401);

            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}
