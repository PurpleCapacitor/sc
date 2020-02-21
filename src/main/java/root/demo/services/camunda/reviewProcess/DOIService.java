package root.demo.services.camunda.reviewProcess;

import java.util.List;
import java.util.UUID;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import root.demo.dto.FormSubmissionDto;
import root.demo.model.Paper;
import root.demo.repositories.PaperRepository;
import root.demo.services.es.Indexer;
import root.demo.services.storage.StorageService;
import org.springframework.core.io.Resource;

@Service
public class DOIService implements JavaDelegate {

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

		Paper p = null;
		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().equals("title")) {
				p = paperRepository.findByTitle(d.getFieldValue());
			}
		}

		String doi = UUID.randomUUID().toString();
		p.setDoi(doi);
		
		Resource resource = storageService.loadFile(p.getFile());
		indexService.index(resource.getFile());
		paperRepository.saveAndFlush(p);

	}

}
