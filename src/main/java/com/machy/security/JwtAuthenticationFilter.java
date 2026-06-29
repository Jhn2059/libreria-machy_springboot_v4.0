package com.machy.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final TokenBlacklistService blacklistService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, TokenBlacklistService blacklistService) {
        this.jwtUtil = jwtUtil;
        this.blacklistService = blacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (jwtUtil.validateToken(token) && !blacklistService.isInvalidated(token)) {
                UUID userId = jwtUtil.getUserId(token);
                String username = jwtUtil.getUsername(token);
                String rol = jwtUtil.getRol(token);

                var authorities = List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));
                var authentication = new UsernamePasswordAuthenticationToken(
                        new UserPrincipal(userId, username, rol), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    public record UserPrincipal(UUID id, String username, String rol) {}
}
