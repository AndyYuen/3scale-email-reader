package app.threescale;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Date;

// To use this as a normal Java Class, do the following:
// 1. remove @Component
// 2. remove 'implements CommandLineRunner'
// 3. Comment out the 'run' method and its annotation
//@Component
public class Mail {
	final String SEPARATOR = "******************************************";
	
	final String APIADMIN = "ayuen@redhat.com";
	final String REPLYTO = APIADMIN;
	
	final String SUBJECT = "Subject: ADHA Service Update Announcement for - %s";
	
	final String MSG = "Hi Service Administrator,\n" + 
			"\n" + 
			"The following service has been updated, for more details please see below:\n\n" + 
			"Name: %s\n" + 
			"Date: %S\n" + 
//			"Link: %s\n" + 
			"\n" + 
			"Regards,\n" + 
			"API Team";
	

    private JavaMailSender javaMailSender;

	public Mail(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}
    /**
    * Sends an email. 
    * <p>
    * This method sends an email. 
    *
    * @param  to          the recipient
    * @param  serviceNmae the service that has been updated
    * @param  LINK        the URL to access more info
    * @return             nothing
    */
    public void sendNotificationEmail(String to, String serviceName /*, String link*/) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setFrom(APIADMIN);
        msg.setReplyTo(REPLYTO);
        msg.setSubject(String.format(SUBJECT, serviceName));
        msg.setText(String.format(MSG, serviceName, new Date().toString() /*, link */) );

        System.out.println(SEPARATOR);
        System.out.println(msg.toString());
        javaMailSender.send(msg);

    }
 /*   
    @Override
    public void run(String... args) {

        System.out.println("Sending Email...");

        sendNotificationEmail("andyyuen105@gmail.com", "API Service", "http://mrdreambot.ddns.net");

        System.out.println("Done");

    }
*/
}
