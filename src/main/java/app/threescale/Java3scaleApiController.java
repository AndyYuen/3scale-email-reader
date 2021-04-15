package app.threescale;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class Java3scaleApiController {

	private String ACCESS_TOKEN;
	private String ACCOUNT_ID;
	private String THREESCALE_HOST;
	private Mail mail = null;

    @Autowired
    private JavaMailSender javaMailSender;
	
	List<Object> providers = null;

    @RequestMapping(value="/notify", method=RequestMethod.GET) 
    public @ResponseBody List <String> migrate(HttpServletRequest request, 
            HttpServletResponse response
			, @RequestParam("accesstoken") String accesstoken
			, @RequestParam("accountid") String accountid
			, @RequestParam("threscalehost") String threscalehost
			, @RequestParam("servicename") String servicename
			) throws Exception {
        

				ACCESS_TOKEN=accesstoken;
				ACCOUNT_ID=accountid;
				THREESCALE_HOST=threscalehost;
  	  	
    	//String jason = getFile("_applications-json.txt");
		List <String> jason = getUsersJson(servicename);
	
       
        
    	return jason;
    }
    
    private List <String> getUsersJson(String servicename) throws Exception{
    	String URL = THREESCALE_HOST+"/admin/api/accounts/"+ACCOUNT_ID+"/users.json?access_token="+ACCESS_TOKEN;

		providers = new ArrayList<Object>();
		providers.add(new JacksonJsonProvider());

    	//WebClient client = WebClient.create(URL, providers);
    	WebClient client = WebClient.create(URL);
    	client = client.accept("application/json").type("application/json");
    	
    	Response r = client.get();
    	
		String sUsers = r.readEntity(String.class);

		ObjectMapper objectMapper = new ObjectMapper();



		List <String> emails = new ArrayList<String>();

		Users users = objectMapper.readValue(sUsers, Users.class);

		for (User user: users.getUsers()){
			String email = (String) user.getUser().get("email");
			sendEmail (email, servicename);
			emails.add(email);
		}

		///Users users = r.readEntity(Users.class);

    	return emails;
    }
    
	private String sendEmail(String email, String servicename){
		if (mail == null) {
			mail = new Mail(javaMailSender);
		}
		String s = "Sending email to "+email+", with service name "+servicename;
		System.out.println(s);
		mail.sendNotificationEmail(email, servicename /*, "http://mrdreambot.ddns.net"*/);
		return s;
	}

}