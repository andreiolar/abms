package com.andreiolar.abms.mail;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.andreiolar.abms.properties.PropertiesReader;

public class MailSender {

	public static void sendMail(String subject, String to, String message, String file) throws AddressException, MessagingException {
		PropertiesReader propertiesReader = new PropertiesReader();
		String username = propertiesReader.readProperty("mail.properties", "email");
		String password = propertiesReader.readProperty("mail.properties", "password");

		// Step1
		Properties mailServerProperties = System.getProperties();
		mailServerProperties.put("mail.smtp.port", "587");
		mailServerProperties.put("mail.smtp.auth", "true");
		mailServerProperties.put("mail.smtp.starttls.enable", "true");

		// Step2
		Session getMailSession = Session.getDefaultInstance(mailServerProperties, null);
		MimeMessage generateMailMessage = new MimeMessage(getMailSession);
		generateMailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		generateMailMessage.setSubject(subject);
		String emailBody = message;
		// generateMailMessage.setContent(emailBody, "text/html");

		BodyPart messageBodyPart = new MimeBodyPart();
		messageBodyPart.setContent(emailBody, "text/html");

		// Create multiPart Message
		Multipart multipart = new MimeMultipart();
		multipart.addBodyPart(messageBodyPart);

		// Part two is attachement
		if (file != null) {
			BodyPart attachBodyPart = new MimeBodyPart();
			String filename = file;
			DataSource source = new FileDataSource(filename);
			attachBodyPart.setDataHandler(new DataHandler(source));
			String fileN = filename.substring(filename.lastIndexOf("\\") + 1, filename.length());
			attachBodyPart.setFileName(fileN);
			multipart.addBodyPart(attachBodyPart);
		}

		generateMailMessage.setContent(multipart);

		// Step3
		Transport transport = getMailSession.getTransport("smtp");

		// Enter your correct gmail UserID and Password
		// if you have 2FA enabled then provide App Specific Password
		transport.connect("smtp.gmail.com", username, password);
		transport.sendMessage(generateMailMessage, generateMailMessage.getAllRecipients());
		transport.close();
	}

}
