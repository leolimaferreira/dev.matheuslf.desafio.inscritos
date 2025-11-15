package dev.matheuslf.desafio.inscritos.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    private static final String FRONTEND_URL = "https://gestaoprojetosfatecfront.azurewebsites.net";

    public void sendPasswordRecoveryEmail(String recipientEmail, UUID tokenId) throws MessagingException {
        String recoveryLink = FRONTEND_URL + "/reset-password?token=" + tokenId;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(recipientEmail);
        helper.setSubject("🔐 Recuperação de Senha - Gestão de Projetos");
        helper.setText(buildEmailHtml(recoveryLink), true);

        mailSender.send(message);
        log.info("Password recovery email sent to {}", recipientEmail);
    }

    private String buildEmailHtml(String recoveryLink) {
        return """
                 <!DOCTYPE html>
                 <html lang="pt-BR">
                 <head>
                     <meta charset="UTF-8">
                     <meta name="viewport" content="width=device-width, initial-scale=1.0">
                     <title>Recuperação de Senha</title>
                 </head>
                 <body style="margin: 0; padding: 0; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #f5f5f5;">
                     <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f5f5f5; padding: 40px 20px;">
                         <tr>
                             <td align="center">
                                 <!-- Container principal -->
                                 <table width="600" cellpadding="0" cellspacing="0" style="background-color: #ffffff; border-radius: 16px; box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); overflow: hidden;">
                                    \s
                                     <tr>
                                         <td style="background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%); padding: 40px 30px; text-align: center;">
                                             <h1 style="margin: 0; color: #ffffff; font-size: 28px; font-weight: 600;">
                                                 🔐 Recuperação de Senha
                                             </h1>
                                             <p style="margin: 10px 0 0 0; color: rgba(255, 255, 255, 0.9); font-size: 16px;">
                                                 Gestão de Projetos
                                             </p>
                                         </td>
                                     </tr>
                                    \s
                                     <tr>
                                         <td style="padding: 40px 30px;">
                                             <p style="margin: 0 0 20px 0; color: #333333; font-size: 16px; line-height: 1.6;">
                                                 Olá! 👋
                                             </p>
                                            \s
                                             <p style="margin: 0 0 20px 0; color: #333333; font-size: 16px; line-height: 1.6;">
                                                 Recebemos uma solicitação para <strong>recuperar sua senha</strong> na plataforma de Gestão de Projetos.
                                             </p>
                                            \s
                                             <p style="margin: 0 0 30px 0; color: #333333; font-size: 16px; line-height: 1.6;">
                                                 Para criar uma nova senha, clique no botão abaixo:
                                             </p>
                                            \s
                                             <table width="100%%" cellpadding="0" cellspacing="0">
                                                 <tr>
                                                     <td align="center" style="padding: 10px 0 30px 0;">
                                                         <a href="%s"\s
                                                            style="display: inline-block;\s
                                                                   background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);\s
                                                                   color: #ffffff;\s
                                                                   text-decoration: none;\s
                                                                   padding: 16px 40px;\s
                                                                   border-radius: 8px;\s
                                                                   font-size: 16px;\s
                                                                   font-weight: 600;
                                                                   box-shadow: 0 4px 6px rgba(102, 126, 234, 0.4);">
                                                             ✨ Redefinir Senha
                                                         </a>
                                                     </td>
                                                 </tr>
                                             </table>
                                            \s
                                             <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #fff8e6; border-left: 4px solid #ffc107; border-radius: 8px; margin: 0 0 20px 0;">
                                                 <tr>
                                                     <td style="padding: 15px 20px;">
                                                         <p style="margin: 0; color: #856404; font-size: 14px; line-height: 1.5;">
                                                             ⏰ <strong>Atenção:</strong> Este link expirará em <strong>15 minutos</strong> por questões de segurança.
                                                         </p>
                                                     </td>
                                                 </tr>
                                             </table>
                                            \s
                                             <p style="margin: 0 0 20px 0; color: #666666; font-size: 14px; line-height: 1.6;">
                                                 Se o botão não funcionar, copie e cole o seguinte link no seu navegador:
                                             </p>
                                            \s
                                             <p style="margin: 0 0 30px 0; padding: 15px; background-color: #f8f9fa; border-radius: 8px; word-break: break-all; font-size: 13px; color: #495057; font-family: monospace;">
                                                 %s
                                             </p>
                                            \s
                                             <table width="100%%" cellpadding="0" cellspacing="0" style="background-color: #f0f4ff; border-left: 4px solid #667eea; border-radius: 8px;">
                                                 <tr>
                                                     <td style="padding: 15px 20px;">
                                                         <p style="margin: 0; color: #495057; font-size: 14px; line-height: 1.5;">
                                                             🛡️ <strong>Não solicitou esta recuperação?</strong><br>
                                                             Se você não pediu para redefinir sua senha, pode ignorar este email com segurança. Sua senha atual permanecerá inalterada.
                                                         </p>
                                                     </td>
                                                 </tr>
                                             </table>
                                         </td>
                                     </tr>
                                    \s
                                     <tr>
                                         <td style="background-color: #f8f9fa; padding: 30px; text-align: center; border-top: 1px solid #e9ecef;">
                                             <p style="margin: 0 0 10px 0; color: #6c757d; font-size: 14px;">
                                                 Atenciosamente,<br>
                                                 <strong style="color: #667eea;">Equipe Gestão de Projetos</strong>
                                             </p>
                                            \s
                                             <p style="margin: 15px 0 0 0; color: #adb5bd; font-size: 12px;">
                                                 © 2025 Gestão de Projetos. Todos os direitos reservados.
                                             </p>
                                         </td>
                                     </tr>
                                    \s
                                 </table>
                             </td>
                         </tr>
                     </table>
                 </body>
                 </html>
                \s""".formatted(recoveryLink, recoveryLink);
    }
}
