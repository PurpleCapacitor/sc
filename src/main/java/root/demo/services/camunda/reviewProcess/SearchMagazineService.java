package root.demo.services.camunda.reviewProcess;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.Magazine;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.UserRepository;

@Service
public class SearchMagazineService implements JavaDelegate {
	
	@Autowired
	RuntimeService runtimeService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MagazineRepository magazineRepository;

	@Override
	@SuppressWarnings("unchecked")
	public void execute(DelegateExecution execution) throws Exception {
		
		List<FormSubmissionDto> selectedMag = (List<FormSubmissionDto>) execution.getVariable("selMagazineDetails");
		
		for (FormSubmissionDto form : selectedMag) {
			if(form.getFieldId().equals("username")) {
				runtimeService.setVariable(execution.getId(), "authorUsername", form.getFieldValue());
			} else {
				Magazine m = magazineRepository.findByName(form.getFieldValue());
				String magType = m.getType().toString();
				runtimeService.setVariable(execution.getId(), "magazineType", magType);
				runtimeService.setVariable(execution.getId(), "mainEditorUsername", m.getMainEditor().getUsername());
			}
			

		}
		
	}

}
