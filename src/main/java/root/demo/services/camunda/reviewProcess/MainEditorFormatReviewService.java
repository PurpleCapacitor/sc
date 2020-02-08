package root.demo.services.camunda.reviewProcess;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
@Service
public class MainEditorFormatReviewService implements JavaDelegate {

	@Autowired
	RuntimeService runtimeService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("formatReviewDecision");

		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().equals("formatPassed")) {
				if (d.getFieldValue().equals("true")) {
					runtimeService.setVariable(execution.getId(), "formatPassed", true);
				}				
			}
			if (d.getFieldId().equals("formatFailed")) {
				if (d.getFieldValue().equals("true")) {
					runtimeService.setVariable(execution.getId(), "formatFailed", true);
				}
			}
		}

	}
}
