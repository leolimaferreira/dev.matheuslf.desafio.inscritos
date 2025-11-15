package dev.matheuslf.desafio.inscritos.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    private static final String FRONTEND_URL = "https://gestaoprojetosfatecfront.azurewebsites.net";

    public void sendPasswordRecoveryEmail(String recipientEmail, UUID tokenId) {
        String recoveryLink = FRONTEND_URL + "/reset-password?token=" + tokenId;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("Recuperação de Senha");
        message.setText(
                "Olá,\n\n" +
                        "Recebemos uma solicitação para recuperar sua senha.\n\n" +
                        "Clique no link abaixo para criar uma nova senha:\n" +
                        recoveryLink + "\n\n" +
                        "Este link expirará em 15 minutos.\n\n" +
                        "Se você não solicitou esta recuperação, ignore este email.\n\n" +
                        "Atenciosamente,\n" +
                        "Equipe de Suporte"
        );

        mailSender.send(message);
        log.info("Password recovery email sent to {}", recipientEmail);
    }
}
