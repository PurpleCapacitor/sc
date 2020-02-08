package root.demo.services.camunda.reviewProcess;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
@Service
public class DecisionService implements JavaDelegate {

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("assignedEditorDecision");
		
		for(FormSubmissionDto d : dto) {
			execution.setVariable("finalDecision", d.getFieldValue());
		}
	}

}
