package com.ovelychko.config;


import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Slf4j
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Already have auth session
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            log.warn("User already authenticated as: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            chain.doFilter(request, response);
            return;
        }

        // Get authorization header and validate
        final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // if Auth token is missing or empty then continue without creating Authentication session
        if (Strings.isBlank(requestTokenHeader) || !requestTokenHeader.startsWith("Bearer ")) {
            log.warn("Auth token is missing: " + requestTokenHeader);
            chain.doFilter(request, response);
            return;
        }

        // Get jwt token and validate
        final String jwtToken = requestTokenHeader.split(" ")[1].trim();
        if (!jwtTokenUtil.validateTokenExpiration(jwtToken)) {
            log.warn("Auth token expired");
            chain.doFilter(request, response);
            return;
        }

        // Get username from jwt token
        String username = null;
        try {
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);
        } catch (Exception e) {
            log.error("Unable to parse JWT Token: " + e);
        }
        // Once we get the username validate it.
        if (Strings.isBlank(username)) {
            log.warn("Username is blank!");
            chain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        } catch (Exception e) {
            log.error("Unable to find user in DB: " + username);
        }
        if (userDetails == null) {
            chain.doFilter(request, response);
            return;
        }

        if (!Objects.equals(username, userDetails.getUsername())) {
            log.warn("token validation failed, username in token : " + username + " are not same as in database " + userDetails.getUsername());
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails, null, userDetails.getAuthorities());

        usernamePasswordAuthenticationToken
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

        chain.doFilter(request, response);
    }
}
