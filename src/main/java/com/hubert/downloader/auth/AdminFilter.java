package com.hubert.downloader.auth;

import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.User;
import com.hubert.downloader.domain.models.user.UserRole;
import com.hubert.downloader.services.UserService;
import com.hubert.downloader.utils.CachedBodyHttpServletRequest;
import lombok.RequiredArgsConstructor;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class AdminFilter implements Filter {

    private final UserService userService;

    @Override
    public void doFilter(
            ServletRequest servletRequest,
            ServletResponse servletResponse,
            FilterChain filterChain
    ) throws IOException, ServletException {

        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) servletRequest);
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (cachedRequest.getRequestURI().contains("logged")) {
            filterChain.doFilter(cachedRequest, response);
            return;
        }

        if (cachedRequest.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            filterChain.doFilter(cachedRequest, response);
            return;
        }

        Token token = new Token(cachedRequest.getHeader("Authorization").replace("Bearer ", ""));
        User user = userService.findByToken(token);

        if (!user.getRole().equals(UserRole.ADMIN)) {
            response.getOutputStream().write("{\"message\": \"Unauthorized\"}".getBytes(StandardCharsets.UTF_8));
            response.setHeader("Content-Type", "application/json");
            response.setStatus(403);
            return;
        }

        filterChain.doFilter(cachedRequest, response);
    }
}
