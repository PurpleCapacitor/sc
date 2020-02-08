package root.demo.services.camunda;

import java.util.UUID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import root.demo.model.users.VerificationToken;
import root.demo.repositories.VerificationTokenRepository;

@Service
public class EmailService implements JavaDelegate {

	@SuppressWarnings("unused")
	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		send((String) execution.getVariable("userEmail"), execution.getId());
	}

	@Async
	public void send(String email, String exId) throws MailException, InterruptedException {
		
		String token = UUID.randomUUID().toString();
		String confirmationUrl = "/users/registrationConfirm/" + token;
		VerificationToken t = new VerificationToken();
		t.setToken(token);
		t.setEmail(email);
		verificationTokenRepository.save(t);

		SimpleMailMessage mail = new SimpleMailMessage();
		mail.setTo(email);
		mail.setFrom("upp@sbb.rs");
		mail.setSubject("Conformation email CAMUNDA");
		mail.setText("Aktivacioni link je " + "http://localhost:8080" + confirmationUrl);
		//javaMailSender.send(mail);
		System.out.println("********** Mail SENT");
		System.out.println("****" + mail.getText());

	}

}
