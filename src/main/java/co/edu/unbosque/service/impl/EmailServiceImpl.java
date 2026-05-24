package co.edu.unbosque.service.impl;

import co.edu.unbosque.entity.Usuario;
import co.edu.unbosque.service.api.EmailServiceAPI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Slf4j
@Service
public class EmailServiceImpl implements EmailServiceAPI {

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public void enviarCodigoVerificacion(String destinatario, String codigo) {
        try {
            log.info("Enviando código de verificación a: {}", destinatario);
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(destinatario);
            helper.setSubject("Código de verificación - Mundial 2026");

            String html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">"
                    + "<div style=\"max-width: 500px; margin: 0 auto; background: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);\">"
                    + "<h2 style=\"color: #333; text-align: center;\">Verificación de correo electrónico</h2>"
                    + "<p style=\"color: #555; font-size: 16px; text-align: center;\">Tu código de verificación es:</p>"
                    + "<div style=\"text-align: center; margin: 25px 0;\">"
                    + "<span style=\"font-size: 36px; font-weight: bold; letter-spacing: 8px; color: #2c3e50; background: #ecf0f1; padding: 15px 25px; border-radius: 6px;\">" + codigo + "</span></div>"
                    + "<p style=\"color: #888; font-size: 14px; text-align: center;\">Este código expira en <b>3 minutos</b>.</p>"
                    + "<hr style=\"border: none; border-top: 1px solid #eee; margin: 20px 0;\">"
                    + "<p style=\"color: #aaa; font-size: 12px; text-align: center;\">Si no solicitaste este código, ignora este mensaje.</p>"
                    + "</div></body></html>";

            helper.setText(html, true);
            mailSender.send(mensaje);

        } catch (Exception e) {
            log.error("Error al enviar código de verificación a {}: {}", destinatario, e.getMessage());
            throw new RuntimeException("Error al enviar código de verificación a " + destinatario, e);
        }
    }

    @Override
    public void enviarCredenciales(Usuario usuario, String passwordOriginal) {
        try {
            log.info("Enviando credenciales a: {}", usuario.getCorreo());
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

            helper.setTo(usuario.getCorreo());
            helper.setSubject("Registro exitoso - Mundial 2026");

            String passwordOfuscado = ofuscarPassword(passwordOriginal);

            String html = "<!DOCTYPE html><html><head><meta charset=\"UTF-8\"></head><body style=\"font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;\">"
                    + "<div style=\"max-width: 500px; margin: 0 auto; background: white; border-radius: 8px; padding: 30px; box-shadow: 0 2px 10px rgba(0,0,0,0.1);\">"
                    + "<h2 style=\"color: #333; text-align: center;\">¡Registro exitoso!</h2>"
                    + "<p style=\"color: #555; font-size: 16px; text-align: center;\">Tus credenciales registradas son:</p>"
                    + "<div style=\"background: #f9f9f9; border-radius: 6px; padding: 20px; margin: 20px 0;\">"
                    + "<p style=\"margin: 8px 0; font-size: 15px; color: #333;\"><b>Username:</b> " + usuario.getUsername() + "</p>"
                    + "<p style=\"margin: 8px 0; font-size: 15px; color: #333;\"><b>Nombre:</b> " + usuario.getNombreApellido() + "</p>"
                    + "<p style=\"margin: 8px 0; font-size: 15px; color: #333;\"><b>Password:</b> " + passwordOfuscado + "</p>"
                    + "</div>"
                    + "<p style=\"color: #888; font-size: 13px; text-align: center;\">Recomendamos guardar tus credenciales en un lugar seguro.</p>"
                    + "</div></body></html>";

            helper.setText(html, true);
            mailSender.send(mensaje);

        } catch (Exception e) {
            log.error("Error al enviar credenciales a {}: {}", usuario.getCorreo(), e.getMessage());
            throw new RuntimeException("Error al enviar credenciales a " + usuario.getCorreo(), e);
        }
    }

    private String ofuscarPassword(String password) {
        log.debug("Ofuscando password");
        if (password == null || password.isEmpty()) return "";
        if (password.length() <= 2) return password;
        if (password.length() <= 4) {
            return password.charAt(0) + "**" + password.charAt(password.length() - 1);
        }
        return password.substring(0, 2)
                + "*".repeat(password.length() - 4)
                + password.substring(password.length() - 2);
    }

}
