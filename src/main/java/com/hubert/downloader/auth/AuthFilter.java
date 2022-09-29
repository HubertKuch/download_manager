package com.hubert.downloader.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubert.downloader.domain.models.tokens.RequestWithTokenDTO;
import com.hubert.downloader.domain.models.tokens.Token;
import com.hubert.downloader.domain.models.user.AccessCodeDTO;
import com.hubert.downloader.services.TokenService;
import com.hubert.downloader.services.UserService;
import com.hubert.downloader.utils.CachedBodyHttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

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
        ((HttpServletResponse) servletResponse).setStatus(401);
    }

    private Boolean isValidTokenRequest(final HttpServletRequest request) throws IOException {
        String tokenRawData = new String(request.getInputStream().readAllBytes());

        RequestWithTokenDTO token = new ObjectMapper().readValue(tokenRawData, RequestWithTokenDTO.class);
        AccessCodeDTO accessCodeDTO = tokenService.decode(token.token());

        return userService.findByAccessCode(accessCodeDTO) != null;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        CachedBodyHttpServletRequest cachedRequest = new CachedBodyHttpServletRequest((HttpServletRequest) servletRequest);
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        try {
            if (!isValidRequest(cachedRequest) || !isValidTokenRequest(cachedRequest)) {
                unauthorized(response);
                return;
            }

            filterChain.doFilter(cachedRequest, response);
        } catch (Exception ignored) {
            System.out.println("exception");
            unauthorized(response);
        }
    }
}
