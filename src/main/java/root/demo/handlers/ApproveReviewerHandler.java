package root.demo.handlers;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.ProcessInstanceData;
import root.demo.repositories.ProcessInstanceDataRepository;

@Service
public class ApproveReviewerHandler implements TaskListener {
	
	@Autowired
	ProcessInstanceDataRepository processInstanceDataRepository;

	@Override
	public void notify(DelegateTask delegateTask) {
		String processInstanceId = delegateTask.getExecution().getProcessInstanceId();
		ProcessInstanceData pid = new ProcessInstanceData();
		pid.setProcessInstanceId(processInstanceId);
		processInstanceDataRepository.saveAndFlush(pid);
		
	}

}
