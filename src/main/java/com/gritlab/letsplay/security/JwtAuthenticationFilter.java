package com.gritlab.letsplay.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final String jwtSecret;

    public JwtAuthenticationFilter(String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        System.out.println("[DEBUG] JwtAuthenticationFilter: Filtering " + request.getRequestURI());
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required. Please log in.");
        }
        String token = header.substring(7);
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            String role = (String) claims.get("role");
            List<GrantedAuthority> authorities = new ArrayList<>();
            if (username != null) {
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toLowerCase()));
                }
                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("[DEBUG] Setting authorities: " + authorities);
            }
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("Authentication required. Please log in.");
        }
        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        String path = request.getRequestURI();
        boolean skip = false;

        // Both POST and GET for login/register are public
        if ((method.equals("POST") || method.equals("GET")) &&
                (path.equals("/users/login") || path.equals("/users/register"))) {
            skip = true;
        }
        // All GET /products* EXCEPT /products/me is public
        if (method.equals("GET")
                && path.startsWith("/products")
                && !path.equals("/products/me")) {
            skip = true;
        }

        System.out.println("[DEBUG][FILTER] method=" + method + ", path=" + path + ", skip=" + skip);
        return skip;
    }
}
