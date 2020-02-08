package root.demo.services.camunda;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.enums.UserType;
import root.demo.model.users.User;
import root.demo.repositories.UserRepository;

@Service
public class UserService implements JavaDelegate {
	
	@Autowired
	UserRepository userRepository; 

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> list = (List<FormSubmissionDto>) execution.getVariable("magazineLoginDetails");
		
		
		User user = null;
		for (FormSubmissionDto dto : list) {
			if(dto.getFieldId().equals("username")) {
				user = userRepository.findByUsername(dto.getFieldValue());
				if(user == null)
					break;
			}		
			if (dto.getFieldId().equals("password")) {
				if (user.getPassword().equals(dto.getFieldValue())) {
					if(user.getUserType().equals(UserType.editor)) {						
						execution.setVariable("mainEditor", user.getUsername());
						execution.setVariable("loggedIn", true);
					} else {
						execution.setVariable("loggedIn", false);
					}					
				} else {
					execution.setVariable("loggedIn", false);
				}
			}
		}
		
	}

}
