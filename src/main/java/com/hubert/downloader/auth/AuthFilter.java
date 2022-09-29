package com.hubert.downloader.auth;

import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.AccessCodeDTO;
import com.hubert.downloader.services.TokenService;
import com.hubert.downloader.services.UserService;
import com.hubert.downloader.utils.CachedBodyHttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Order(-1)
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private final TokenService tokenService;
    private final UserService userService;

    private Boolean isValidRequest(final HttpServletRequest request) {
        return  request.getHeader("Authorization") != null &&
                request.getHeader("Authorization").startsWith("Bearer");
    }

    private void unauthorized(final ServletResponse servletResponse) throws IOException {
        servletResponse.getOutputStream().write("{\"message\": \"Unauthorized\"}".getBytes(StandardCharsets.UTF_8));
        ((HttpServletResponse) servletResponse).setHeader("Content-Type", "application/json");
        ((HttpServletResponse) servletResponse).setStatus(403);
    }

    private Boolean isValidTokenRequest(final HttpServletRequest request) {
        String tokenRawData = request.getHeader("Authorization").replace("Bearer ", "");
        Token token = new Token(tokenRawData);
        AccessCodeDTO accessCodeDTO = tokenService.decode(token);

        return userService.findByAccessCode(accessCodeDTO) != null;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) servletRequest);
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (cachedRequest.getMethod().equals("OPTIONS")) {
            response.setStatus(200);
            filterChain.doFilter(cachedRequest, response);
            return;
        }

        try {
            if (!isValidRequest(cachedRequest) || !isValidTokenRequest(cachedRequest)) {
                unauthorized(response);
                return;
            }

            filterChain.doFilter(cachedRequest, response);
        } catch (Exception ignored) {
            System.out.println(ignored.getMessage());
            unauthorized(response);
        }
    }
}
