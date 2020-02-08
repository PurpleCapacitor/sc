package root.demo.services.camunda.reviewProcess;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.users.UserDetails;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.ScientificAreaRepository;
import root.demo.repositories.UserDetailsRepository;
import root.demo.repositories.UserRepository;

@Service
public class AddCoAuthorsService implements JavaDelegate {

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MagazineRepository magazineRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) runtimeService.getVariable(execution.getId(),
				"coAuthors");

		UserDetails ud = new UserDetails();
		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().equals("email")) {
				ud.setEmail(d.getFieldValue());
			}
			if (d.getFieldId().equals("firstName")) {
				ud.setFirstName(d.getFieldValue());
			}
			if (d.getFieldId().equals("lastName")) {
				ud.setLastName(d.getFieldValue());
			}
			if (d.getFieldId().equals("city")) {
				ud.setCity(d.getFieldValue());
			}
			if (d.getFieldId().equals("country")) {
				ud.setCountry(d.getFieldValue());
			}

		}
		
		userDetailsRepository.saveAndFlush(ud);
		System.out.println("coAuthor added.");
	}

}
