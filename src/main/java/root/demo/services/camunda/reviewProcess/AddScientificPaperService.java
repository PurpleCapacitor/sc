package root.demo.services.camunda.reviewProcess;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.Magazine;
import root.demo.model.Paper;
import root.demo.model.ScientificArea;
import root.demo.model.users.User;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.PaperRepository;
import root.demo.repositories.ScientificAreaRepository;
import root.demo.repositories.UserRepository;
import root.demo.services.es.Indexer;
import root.demo.services.storage.StorageService;

@Service
public class AddScientificPaperService implements JavaDelegate {

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	MagazineRepository magazineRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@Autowired
	PaperRepository paperRepository;

	@Autowired
	Indexer indexService;

	@Autowired
	StorageService storageService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("paperDetails");

		Magazine m;
		User user;
		Paper paper = new Paper();
		ScientificArea sc;

		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().equals("scientificArea")) {
				sc = scientificAreaRepository.findByName(d.getFieldValue());
				paper.setScArea(sc);
			}
			if (d.getFieldId().equals("title")) {
				paper.setTitle(d.getFieldValue());
			}
			if (d.getFieldId().equals("keywords")) {
				paper.setKeywords(d.getFieldValue());
			}
			if (d.getFieldId().equals("paperAbstract")) {
				paper.setPaperAbstract(d.getFieldValue());
			}
			if (d.getFieldId().equals("noCoAuthors")) {
				runtimeService.setVariable(execution.getId(), "noCoAuthors", Long.parseLong(d.getFieldValue()));
			}
			if (d.getFieldId().equals("file")) {
				paper.setFile(d.getFieldValue());
			}
			if (d.getFieldId().equals("author")) {
				user = userRepository.findByUsername(d.getFieldValue());
				paper.setAuthor(user);
			}
			if (d.getFieldId().equals("magazine")) {
				m = magazineRepository.findByName(d.getFieldValue());
				paper.setMagazine(m);
			}
		}

		paperRepository.saveAndFlush(paper);
		System.out.println("Paper saved.");
	}

}
