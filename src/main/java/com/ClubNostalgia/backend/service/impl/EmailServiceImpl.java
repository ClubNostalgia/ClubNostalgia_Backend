package com.ClubNostalgia.backend.service.impl;

import com.ClubNostalgia.backend.service.interfaces.EmailService;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${EMAIL_FROM}")
    private String emailFrom;

    @Value("${EMAIL_TO}")
    private String emailTo;

    @Override
    public void sendContactNotification(String nombre, String emailUsuario, String proyecto) throws IOException {
        sendEmailToAdmin(nombre, emailUsuario, proyecto);
        sendConfirmationToUser(nombre, emailUsuario);
    }

    private void sendEmailToAdmin(String nombre, String emailUsuario, String proyecto) throws IOException {
        Email from = new Email(emailFrom);
        Email to = new Email(emailTo);
        String subject = "Nuevo mensaje de contacto - Club Nostalgia";
        
        String body = String.format(
            "<h2>Nuevo mensaje de contacto</h2>" +
            "<p><strong>Nombre:</strong> %s</p>" +
            "<p><strong>Email:</strong> %s</p>" +
            "<p><strong>Proyecto:</strong></p>" +
            "<p>%s</p>",
            nombre, emailUsuario, proyecto
        );
        
        Content content = new Content("text/html", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        
        Response response = sg.api(request);
        
        if (response.getStatusCode() >= 400) {
            throw new IOException("Error al enviar email a admin: " + response.getBody());
        }
    }

    private void sendConfirmationToUser(String nombre, String emailUsuario) throws IOException {
        Email from = new Email(emailFrom);
        Email to = new Email(emailUsuario);
        String subject = "Hemos recibido tu mensaje - Club Nostalgia";
        
        String body = String.format(
            "<h2>¡Gracias por contactarnos, %s!</h2>" +
            "<p>Hemos recibido tu mensaje y nos pondremos en contacto contigo pronto.</p>" +
            "<p>Saludos,<br>El equipo de Club Nostalgia</p>",
            nombre
        );
        
        Content content = new Content("text/html", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        
        Response response = sg.api(request);
        
        if (response.getStatusCode() >= 400) {
            throw new IOException("Error al enviar email de confirmación: " + response.getBody());
        }
    }
}
