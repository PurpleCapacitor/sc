package root.demo.services.camunda;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.model.Magazine;
import root.demo.repositories.MagazineRepository;

@Service
public class ApproveMagazineService implements JavaDelegate {
	
	@Autowired
	RuntimeService runtimeService;

	@Autowired
	MagazineRepository magazineRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Magazine m = magazineRepository
				.findByName((String) runtimeService.getVariable(execution.getId(), "magazineName"));
		boolean decision = (boolean) runtimeService.getVariable(execution.getId(), "decision");
		m.setActivated(decision);
		magazineRepository.saveAndFlush(m);
		System.out.println("Magazine activated.");
		
	}
	

}
