package root.demo.services.camunda;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.Magazine;
import root.demo.model.users.User;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.UserRepository;

@Service
public class AddMagazinePersonnel implements JavaDelegate {

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MagazineRepository magazineRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String editorUsername = (String) runtimeService.getVariable(execution.getId(), "chosenEditorUsername");
		@SuppressWarnings("unchecked")
		List<String> reviewersUsernames = (List<String>) runtimeService.getVariable(execution.getId(),
				"chosenReviewersUsernames");
		User editor = userRepository.findByUsername(editorUsername);
		Magazine m = magazineRepository
				.findByName((String) runtimeService.getVariable(execution.getId(), "magazineName"));
		m.getEditors().add(editor);
		editor.getMagazinesEditor().add(m);
		for (String username : reviewersUsernames) {
			User user = userRepository.findByUsername(username);
			m.getReviewers().add(user);
			user.getMagazinesReviewers().add(m);
			magazineRepository.saveAndFlush(m);
		}
		System.out.println("Magazine updated.");

	}

}
