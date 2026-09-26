package com.bridgelabz.fundoo.notes.security;

import com.bridgelabz.fundoo.notes.entity.User;
import com.bridgelabz.fundoo.notes.redis.TokenCacheService;
import com.bridgelabz.fundoo.notes.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final TokenCacheService tokenCacheService;

    public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository, TokenCacheService tokenCacheService) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.tokenCacheService = tokenCacheService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try{
            String authorizationHeader = request.getHeader("Authorization");

            if(authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }

            String token = authorizationHeader.substring(7);

            if (!tokenCacheService.isTokenValid(token)) {
                SecurityContextHolder.clearContext();

                filterChain.doFilter(request, response);
                return;
            }

            String email = jwtService.extractEmail(token);

            User user = userRepository.findByEmail(email).orElse(null);

            if(user!=null && jwtService.validateToken(token, email)){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, null, null);

                authenticationToken.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            log.warn("Invalid or expired authentication token");
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);        }
    }
}
