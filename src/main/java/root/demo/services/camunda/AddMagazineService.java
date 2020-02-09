package root.demo.services.camunda;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.Magazine;
import root.demo.model.ScientificArea;
import root.demo.model.enums.MagazineType;
import root.demo.model.enums.UserType;
import root.demo.model.users.User;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.ScientificAreaRepository;
import root.demo.repositories.UserRepository;

@Service
public class AddMagazineService implements JavaDelegate {

	@Autowired
	UserRepository userRepository;

	@Autowired
	MagazineRepository magazineRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> list = (List<FormSubmissionDto>) execution.getVariable("magazineDetails");
		Magazine m = new Magazine();
		ScientificArea sc = null;
		for (FormSubmissionDto dto : list) {
			if (dto.getFieldId().equals("name"))
				m.setName(dto.getFieldValue());
			if (dto.getFieldId().equals("issn"))
				m.setIssn(Long.valueOf(dto.getFieldValue()));
			if (dto.getFieldId().equals("scArea")) {
				sc = scientificAreaRepository.findByName(dto.getFieldValue());
				m.getScientificAreas().add(sc);
			}
			if (dto.getFieldId().equals("type")) {
				if (dto.getFieldValue().equals("openAccess")) {
					m.setType(MagazineType.openAccess);
				} else {
					m.setType(MagazineType.paidAccess);
				}
			}

		}

		User mainEditor = userRepository.findByUsername((String) execution.getVariable("mainEditor"));
		m.setMainEditor(mainEditor);
		magazineRepository.saveAndFlush(m);		
		List<User> compatibleUsers = sc.getUsers();
		List<String> compatibleReviewers = new ArrayList<>();
		for (User u : compatibleUsers) {
			if(u.getUserType().equals(UserType.editor)){
				execution.setVariable("editorValue", u.getUsername());
			} else if (u.getUserType().equals(UserType.reviewer)) {
				compatibleReviewers.add(u.getUsername());				
			}
		}
		
		execution.setVariable("reviewerList", compatibleReviewers); 

	}

}
