package dev.matheuslf.desafio.inscritos.security;

import com.auth0.jwt.JWT;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        var token = this.recoverToken(request);
        log.info("DEBUG: Token recovered: {}", token != null ? "TOKEN_EXISTS" : "NO_TOKEN");

        if (token != null) {
            var login = tokenService.validateToken(token);
            log.info("DEBUG: Login from token: {}", login);

            if (login != null) {
                User user = userRepository.findById(UUID.fromString(login))
                        .orElseThrow(() -> new NotFoundException("User not found with ID: " + login));

                assert token != null;
                String role = JWT.decode(token).getClaim("role").asString();

                log.info("DEBUG: User from DB - Email: {}, Role: {}", user.getEmail(), user.getRole());
                log.info("DEBUG: Role from token: {}", role);

                var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()));
                log.info("DEBUG: Authorities created: {}", authorities);

                var authentication = new UsernamePasswordAuthenticationToken(user.getId().toString(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                log.info("DEBUG: Authentication set with ID as principal: {}", user.getId());
            }
        }

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            return null;
        }
        return authHeader.replace("Bearer ", "");
    }
}