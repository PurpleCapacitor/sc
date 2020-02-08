package root.demo.services.camunda;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.ScientificArea;
import root.demo.model.users.User;
import root.demo.repositories.ScientificAreaRepository;
import root.demo.repositories.UserRepository;

@Service
public class SCAreaService implements JavaDelegate {

	@Autowired
	UserRepository userRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> scAreas = (List<FormSubmissionDto>) execution.getVariable("scAreas");
		List<FormSubmissionDto> registration = (List<FormSubmissionDto>) execution.getVariable("registration");

		for (FormSubmissionDto dto : scAreas) {
			if (dto.getFieldValue().equals("true")) {
				ScientificArea sc = scientificAreaRepository.findByName(dto.getFieldId());
				User user = null;
				for (FormSubmissionDto formSubmission : registration) {
					if (formSubmission.getFieldId().equals("username")) {
						user = userRepository.findByUsername(formSubmission.getFieldValue());
						user.getScientificAreas().add(sc);
						sc.getUsers().add(user);
						userRepository.saveAndFlush(user);
					}
				}
			}

		}

	}

}
