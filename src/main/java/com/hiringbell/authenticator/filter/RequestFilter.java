package com.hiringbell.authenticator.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hiringbell.authenticator.config.RouteValidator;
import com.hiringbell.authenticator.entity.User;
import com.hiringbell.authenticator.model.CurrentSession;
import com.hiringbell.authenticator.model.JwtTokenModel;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@Component
@Order(1)
public class RequestFilter implements Filter {
    @Autowired
    CurrentSession currentSession;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    RouteValidator routeValidator;
    private final static Logger LOGGER = LoggerFactory.getLogger(RequestFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        try {
            HttpServletRequest request = ((HttpServletRequest) servletRequest);
            LOGGER.info("[AUTH SERVICE REQUEST]: " + request.getRequestURI());

            if (routeValidator.isSecured.test(request)) {
                Object headerUserDetail = request.getHeaders("userDetail").nextElement();
                if (headerUserDetail == null || headerUserDetail.toString().isEmpty()) {
                    throw new Exception("Invalid token found. Please contact to admin.");
                }

                var user = objectMapper.readValue(headerUserDetail.toString(), User.class);
                currentSession.setUser(new com.hiringbell.authenticator.model.User(
                        user.getUserId(),
                        user.getMobile(),
                        user.getEmail(),
                        user.getFirstName().concat(" " + user.getLastName()))
                );
            }

        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unauthorized access. Please try with valid token.");
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }
}