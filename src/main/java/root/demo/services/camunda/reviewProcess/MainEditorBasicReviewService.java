package root.demo.services.camunda.reviewProcess;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;

@Service
public class MainEditorBasicReviewService implements JavaDelegate {
	
	@Autowired
	RuntimeService runtimeService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("basicReviewDecision");
		
		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().equals("approve")) {
				if(d.getFieldValue().equals("true")) {
					runtimeService.setVariable(execution.getId(), "approve", true);
				} else {
					runtimeService.setVariable(execution.getId(), "approve", false);
				}
				
			}
		}

	}

}
