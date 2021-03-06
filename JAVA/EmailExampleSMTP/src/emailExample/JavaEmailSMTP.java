package emailExample;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class JavaEmailSMTP {
	Properties emailProperties;
	Session mailSession;
	MimeMessage emailMessage;
	
	public static void main(String args[])	throws AddressException, MessagingException{
		JavaEmailSMTP javaEmailSMTP = new JavaEmailSMTP();
		
		javaEmailSMTP.setEmailServerProperties();
		javaEmailSMTP.createEmailMessage();
		javaEmailSMTP.sendEmail();
	}
	
	//This function sets email server properties
	public void setEmailServerProperties(){
		String emailPort = "587";		//gmail's SMTP port
		emailProperties = System.getProperties();
		emailProperties.put("mail.smtp.port",emailPort);
		emailProperties.put("mail.smtp.auth","true");
		emailProperties.put("mail.smtp.starttls.enable", "true");
	}
	
	//This function creates email message
	public void createEmailMessage() throws AddressException, MessagingException{
		String[] toEmails = { "bnmali0121@gmail.com", "rohitdeepu17@gmail.com" };
		String emailSubject = "Java Email";
		String emailBody = "This is an email sent by JavaMail api.";
		
		mailSession = Session.getDefaultInstance(emailProperties, null);
		emailMessage = new MimeMessage(mailSession);
		
		for (int i = 0; i < toEmails.length; i++) {
			emailMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmails[i]));
		}
		emailMessage.setSubject(emailSubject);
		//emailMessage.setContent(emailBody, "text/html");//for a html email
		emailMessage.setText(emailBody);// for a text email
	}
	
	//This function sends email
	public void sendEmail() throws AddressException, MessagingException{
		String emailHost = "smtp.gmail.com";
		String fromUser = "rohitdeepu17@gmail.com";//just the id alone without @gmail.com
		String fromUserEmailPassword = "******";

		Transport transport = mailSession.getTransport("smtp");

		transport.connect(emailHost, fromUser, fromUserEmailPassword);
		transport.sendMessage(emailMessage, emailMessage.getAllRecipients());
		transport.close();
		System.out.println("Email sent successfully.");
	}
}

//Reference : http://javapapers.com/core-java/java-email/
