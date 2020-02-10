package root.demo.services.camunda;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.users.User;
import root.demo.repositories.UserRepository;

@Service
public class ActivateUserService implements JavaDelegate {

	@Autowired
	TaskService taskService;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	UserRepository userRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		User user = userRepository.findByUsername((String) execution.getVariable("username"));
		String processInstance = execution.getProcessInstanceId();
		execution.setVariable("processInstancee", processInstance);
		execution.setVariable("adminUsername", "test");
		System.out.println(user.getUsername() + "'s email confirmed.");

	}

}
