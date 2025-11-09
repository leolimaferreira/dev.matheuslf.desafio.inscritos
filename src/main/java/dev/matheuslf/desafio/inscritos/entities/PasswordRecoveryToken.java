package dev.matheuslf.desafio.inscritos.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "password_recovery_tokens")
@Getter
@Setter
public class PasswordRecoveryToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private LocalDateTime expirationDate = LocalDateTime.now().plusMinutes(15);

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
