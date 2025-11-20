package dev.matheuslf.desafio.inscritos.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.matheuslf.desafio.inscritos.dto.user.UpdateUserDTO;
import dev.matheuslf.desafio.inscritos.dto.user.UserRequestDTO;
import dev.matheuslf.desafio.inscritos.dto.user.UserResponseDTO;
import dev.matheuslf.desafio.inscritos.entities.User;
import dev.matheuslf.desafio.inscritos.exception.NotFoundException;
import dev.matheuslf.desafio.inscritos.exception.UnauthorizedException;
import dev.matheuslf.desafio.inscritos.mapper.UserMapper;
import dev.matheuslf.desafio.inscritos.repository.UserRepository;
import dev.matheuslf.desafio.inscritos.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder encoder;
    private final UserValidator userValidator;

    public UserResponseDTO saveUser(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);
        user.setPassword(encoder.encode(user.getPassword()));
        userValidator.validateUserEmail(user);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }

    public void updateUser(String token, UUID id, UpdateUserDTO dto) {
        User user = userRepository.findById(id).orElseThrow( () -> new NotFoundException("User not found"));

        DecodedJWT decodedToken = JWT.decode(token);

        if (!decodedToken.getSubject().equals(user.getId().toString()) || !decodedToken.getClaim("role").asString().equals("ADMIN")) {
            throw new UnauthorizedException("You are not allowed to update this user.");
        }
        userValidator.validateUserEmail(user);
        userMapper.updateEntity(user, dto);
        userRepository.save(user);
    }

    public List<String> findAllEmails() {
        return userRepository.findAllEmails();
    }
}
