package root.demo.services.camunda;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.enums.UserType;
import root.demo.model.users.UserDetails;
import root.demo.repositories.UserRepository;

@Service
public class RegistrationService implements JavaDelegate {
	
	@Autowired
	IdentityService identityService;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	RuntimeService runtimeService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> registration = (List<FormSubmissionDto>) execution.getVariable("registration");
		
		User userCamunda = identityService.newUser(""); // camunda 
		for(FormSubmissionDto formSubmission : registration) {
			if(formSubmission.getFieldId().equals("username")) {
				userCamunda.setId(formSubmission.getFieldValue());
			}
			if(formSubmission.getFieldId().equals("password")) {
				userCamunda.setPassword(formSubmission.getFieldValue());
			}
		}
		
		
		
		identityService.saveUser(userCamunda);
		
		root.demo.model.users.User user = new root.demo.model.users.User(); // profil usera
		UserDetails userDetails = new UserDetails();
		user.setUsername(userCamunda.getId());
		user.setPassword(userCamunda.getPassword());
		for(FormSubmissionDto formSubmission : registration) {
			if(formSubmission.getFieldId().equals("email")) {
				userDetails.setEmail(formSubmission.getFieldValue());
			}
			if(formSubmission.getFieldId().equals("firstName")) {
				userDetails.setFirstName(formSubmission.getFieldValue());
			}
			if(formSubmission.getFieldId().equals("lastName")) {
				userDetails.setLastName(formSubmission.getFieldValue());
			}
			if(formSubmission.getFieldId().equals("city")) {
				userDetails.setCity(formSubmission.getFieldValue());
			}
			if(formSubmission.getFieldId().equals("country")) {
				userDetails.setCountry(formSubmission.getFieldValue());
			}
			if(formSubmission.getFieldId().equals("reviewer")) {
				if(formSubmission.getFieldValue().isEmpty()) {
					user.setWantsReviewer(false);
				} else {
					user.setWantsReviewer(true);
				}
				
			}
		}
		
		user.setUserType(UserType.author);
		user.setUserDetails(userDetails);
		userRepository.saveAndFlush(user);
		runtimeService.setVariable(execution.getId(), "userEmail", user.getUserDetails().getEmail());
		runtimeService.setVariable(execution.getId(), "username", user.getUsername());
		
	}

}
