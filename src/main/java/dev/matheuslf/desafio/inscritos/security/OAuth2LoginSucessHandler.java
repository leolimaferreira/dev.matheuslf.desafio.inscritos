package dev.matheuslf.desafio.inscritos.security;

import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.entities.enums.Role;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSucessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        log.info("OAuth2 login successful for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(email, name));

        String token = tokenService.generateToken(user);

        String targetUrl = String.format("https://gestaoprojetosfatecfront.azurewebsites.net/auth/callback?token=%s&name=%s",
                token, user.getName());
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private User createNewUser(String email, String name) {
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setRole(Role.USER);

        return userRepository.save(user);
    }
}
