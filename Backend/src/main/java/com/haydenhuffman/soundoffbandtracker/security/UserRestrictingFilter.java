package com.haydenhuffman.soundoffbandtracker.security;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

public class UserRestrictingFilter extends OncePerRequestFilter {
    private AntPathRequestMatcher matcher = new AntPathRequestMatcher("/users/{userId}/**");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !matcher.matches(request);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        if (matcher.matches(request)) {
            String pathUserId = request.getParameter("userId");
            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (!userDetails.getUsername().equals(pathUserId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access Denied");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }



}
