package root.demo.services.camunda;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.enums.UserType;
import root.demo.model.users.User;
import root.demo.repositories.UserRepository;

@Service
public class PromoteReviewer implements JavaDelegate {

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	UserRepository userRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		@SuppressWarnings("unchecked")
		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("formSubmission");
		User user = userRepository.findByUsername((String) execution.getVariable("username"));
		for (FormSubmissionDto dtoLoop : dto) {
			if (dtoLoop.getFieldId().equals("approve")) {
				if (Boolean.parseBoolean(dtoLoop.getFieldValue())) {
					user.setUserType(UserType.reviewer);
					System.out.println("User updated, Reviewer role set");				
				}
			}
		}

		userRepository.save(user);
		

	}

}
