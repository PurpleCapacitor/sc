package root.demo.services.camunda.reviewProcess;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.Magazine;
import root.demo.model.ScientificArea;
import root.demo.model.enums.UserType;
import root.demo.model.users.User;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.ScientificAreaRepository;
@Service
public class AssignEditorService implements JavaDelegate {
	
	@Autowired
	RuntimeService runtimeService;
	
	@Autowired
	ScientificAreaRepository scientificAreaRepository;
	
	@Autowired
	MagazineRepository magazineRepository;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> paperDetails = (List<FormSubmissionDto>) execution.getVariable("paperDetails");
		
		ScientificArea sc = new ScientificArea();
		Magazine m = new Magazine();
		for(FormSubmissionDto d : paperDetails) {
			if(d.getFieldId().equals("scientificArea")) {
				sc = scientificAreaRepository.findByName(d.getFieldValue());
			}
			if(d.getFieldId().equals("magazine")) {
				m = magazineRepository.findByName(d.getFieldValue());
			}
		}
		
		List<User> scUsers = sc.getUsers();
		List<User> magazineEditors = m.getEditors();
		List<User> matchedUsers = new ArrayList<User>();
		if(!magazineEditors.isEmpty()) {
			for(User magEditor : magazineEditors) {
				for(User u : scUsers) {
					if(magEditor.getUsername().equals(u.getUsername())) {
						matchedUsers.add(u);
					}
				}
			}
		}
		
		
		
		if(matchedUsers.isEmpty()) {
			User u = m.getMainEditor();
			runtimeService.setVariable(execution.getId(), "assignedEditor", u.getUsername());
			System.out.println("Main editor assigned to pick reviewers");
		} else {
			for(User u : matchedUsers) {
				if(u.getUserType().equals(UserType.editor)) {
					if(u.getMagazinesMainEditor().isEmpty()) {
						runtimeService.setVariable(execution.getId(), "assignedEditor", u.getUsername());
						System.out.println("Editor assigned to pick reviewers");
					}
				}
			}
		}
		
		
	}

}
