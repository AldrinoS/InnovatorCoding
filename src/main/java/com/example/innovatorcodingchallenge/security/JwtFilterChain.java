package com.example.innovatorcodingchallenge.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Component
public class JwtFilterChain extends OncePerRequestFilter {


    @Autowired
    private JwtGenerator jwtGenerator;
    @Autowired
    private CustomUserDetailsImpl customUserDetailsImpl;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = getTokenFromRequest(httpServletRequest);

        if(null!=token && jwtGenerator.isTokenValid(token)) {
            String username = jwtGenerator.getUsernameFromToken(token);
            UserDetails userDetails = customUserDetailsImpl.loadUserByUsername(username);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if(null!=auth && auth.contains("Bearer ")) {
            return auth.substring(7);
        }

        return null;
    }
}
