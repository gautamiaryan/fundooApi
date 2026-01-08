package com.bridgelabz.fundoo.utility;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoo.dto.MailObject;

@Component
public class JMSProvider {

    private final String fromEmail = System.getenv("email");
    private final String password = System.getenv("password");

    private Session createSession() {
	Properties prop = new Properties();
	prop.put("mail.smtp.auth", "true");
	prop.put("mail.smtp.starttls.enable", "true");
	prop.put("mail.smtp.host", "smtp.gmail.com");
	prop.put("mail.smtp.port", "587");

	return Session.getInstance(prop, new Authenticator() {
	    @Override
	    protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(fromEmail, password);
	    }
	});
    }

    public void sendEmail(String toEmail, String subject, String body) {
	try {
	    Session session = createSession();
	    MimeMessage message = new MimeMessage(session);
	    message.setFrom(new InternetAddress(fromEmail, "Gautamkumar"));
	    message.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
	    message.setSubject(subject);
	    message.setText(body);
	    Transport.send(message);
	    System.out.println("Email sent successfully to " + toEmail);
	} catch (MessagingException | java.io.UnsupportedEncodingException e) {
	    e.printStackTrace();
	    System.err.println("Exception occurred while sending email: " + e.getMessage());
	}
    }

    @RabbitListener(queues = "rmq.rube.queue")
    public void receiveMessage(MailObject user) {
	sendEmail(user.getEmail(), user.getSubject(), user.getMessage());
	System.out.println("Received Message From RabbitMQ: " + user);
    }
}
