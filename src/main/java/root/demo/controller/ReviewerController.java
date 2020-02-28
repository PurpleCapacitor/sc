package root.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import root.demo.dto.FormFieldsDto;
import root.demo.dto.FormSubmissionDto;
import root.demo.dto.MagazineDTO;
import root.demo.dto.PaperDTO;
import root.demo.dto.ReviewDTO;
import root.demo.dto.UnfinishedReviewDTO;
import root.demo.dto.UserDTO;
import root.demo.handlers.DocumentHandler;
import root.demo.model.Magazine;
import root.demo.model.Paper;
import root.demo.model.Review;
import root.demo.model.ScientificArea;
import root.demo.model.es.SimpleQuery;
import root.demo.model.users.User;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.PaperRepository;
import root.demo.repositories.ScientificAreaRepository;
import root.demo.repositories.UserRepository;
import root.demo.services.camunda.reviewProcess.ReviewService;
import root.demo.services.es.Indexer;
import root.demo.services.es.ResultRetriever;
import root.demo.services.storage.StorageService;

@Controller
@RequestMapping(value = "/reviews")
@SuppressWarnings("unchecked")
public class ReviewerController {

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PaperRepository paperRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@Autowired
	MagazineRepository magazineRepository;

	@Autowired
	ReviewService reviewService;

	@Autowired
	ResultRetriever resultRetriever;

	@Autowired
	StorageService storageService;

	@Autowired
	Indexer indexer;

	private DummyController dc = new DummyController();

	@GetMapping(value = "/initProcesses", produces = "application/json")
	public @ResponseBody FormFieldsDto startReviewProcess() {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("review_process");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
	}

	@GetMapping(value = "/initProcesses/magazines", produces = "application/json")
	public @ResponseBody List<MagazineDTO> getMagazines() {
		List<Magazine> magazines = magazineRepository.findAll();
		List<MagazineDTO> dto = new ArrayList<MagazineDTO>();
		for (Magazine m : magazines) {
			MagazineDTO magDto = new MagazineDTO();
			magDto.setName(m.getName());
			dto.add(magDto);
		}

		return dto;
	}

	@PostMapping(value = "/magazineChoices/{taskId}")
	public ResponseEntity<FormSubmissionDto> selectMagazine(@PathVariable String taskId,
			@RequestBody List<FormSubmissionDto> dto) {

		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "selMagazineDetails", dto);
		formService.submitTaskForm(taskId, map);

		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);
	}

	@GetMapping(value = "/{processInstanceId}", produces = "application/json")
	public @ResponseBody FormFieldsDto getFields(@PathVariable String processInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), processInstanceId, properties);
	}

	@PostMapping(value = "/papers/{taskId}")
	public ResponseEntity<FormSubmissionDto> addPaper(@RequestBody List<FormSubmissionDto> dto,
			@PathVariable String taskId) {

		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "paperDetails", dto);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);

	}

	/*
	 * @GetMapping(value = "/coAuthorFields/{processInstanceId}", produces =
	 * "application/json") public @ResponseBody FormFieldsDto
	 * getCoAuthorFields(@PathVariable String processInstanceId) { Task task =
	 * taskService.createTaskQuery().processInstanceId(processInstanceId).list().get
	 * (0); TaskFormData tfd = formService.getTaskFormData(task.getId());
	 * List<FormField> properties = tfd.getFormFields(); return new
	 * FormFieldsDto(task.getId(), processInstanceId, properties); }
	 */

	@PostMapping(value = "/coauthors/{taskId}")
	public ResponseEntity<FormSubmissionDto> addCoAuthors(@RequestBody List<FormSubmissionDto> dto,
			@PathVariable String taskId) {

		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "coAuthors", dto);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);

	}

	@GetMapping(value = "/papers/details/{processInstanceId}", produces = "application/json")
	public @ResponseBody PaperDTO getPaperDetails(@PathVariable String processInstanceId) {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) runtimeService.getVariable(processInstanceId,
				"paperDetails");
		PaperDTO paperDTO = new PaperDTO();

		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().equals("title")) {
				paperDTO.setTitle(d.getFieldValue());
			}
			if (d.getFieldId().equals("keywords")) {
				paperDTO.setKeywords(d.getFieldValue());
			}
			if (d.getFieldId().equals("paperAbstract")) {
				paperDTO.setPaperAbstract(d.getFieldValue());
			}
			if (d.getFieldId().equals("author")) {
				paperDTO.setAuthor(d.getFieldValue());
			}

		}

		return paperDTO;
	}

	@PostMapping(value = "/papers/decisions/{taskId}")
	public ResponseEntity<FormSubmissionDto> basicReview(@RequestBody List<FormSubmissionDto> dto,
			@PathVariable String taskId) {
		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "basicReviewDecision", dto);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);

	}

	@GetMapping(value = "/papers/details/files/{processInstanceId}", produces = "application/json")
	public @ResponseBody PaperDTO getPaperFile(@PathVariable String processInstanceId) {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) runtimeService.getVariable(processInstanceId,
				"paperDetails");
		PaperDTO paperDTO = new PaperDTO();

		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().equals("file")) {
				paperDTO.setFile(d.getFieldValue());
			}

		}
		return paperDTO;
	}

	@PostMapping(value = "/papers/formats/{taskId}")
	public ResponseEntity<FormSubmissionDto> formatReview(@RequestBody List<FormSubmissionDto> dto,
			@PathVariable String taskId) {
		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "formatReviewDecision", dto);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);

	}

	@PostMapping(value = "/papers/comments/{taskId}")
	public ResponseEntity<FormSubmissionDto> addComment(@RequestBody List<FormSubmissionDto> dto,
			@PathVariable String taskId) {
		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "reviewComment", dto);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);

	}

	@GetMapping(value = "/papers/comments/{processInstanceId}", produces = "application/json")
	public @ResponseBody List<FormSubmissionDto> getComment(@PathVariable String processInstanceId) {
		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) runtimeService.getVariable(processInstanceId,
				"reviewComment");
		return dto;
	}

	@PostMapping(value = "/papers/resubmits/{pid}/{taskId}")
	public ResponseEntity<FormSubmissionDto> fileResubmit(@PathVariable String pid, @PathVariable String taskId,
			@RequestBody FormSubmissionDto dto) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<FormSubmissionDto> paper = (List<FormSubmissionDto>) runtimeService.getVariable(pid, "paperDetails");
		Paper p = new Paper();

		for (FormSubmissionDto d : paper) {
			if (d.getFieldId().equals("title")) {
				p = paperRepository.findByTitle(d.getFieldValue());
			}
			if (d.getFieldId().equals("file")) {
				d.setFieldValue(dto.getFieldValue());
				p.setFile(dto.getFieldValue());
				paperRepository.saveAndFlush(p);
			}
		}
		runtimeService.setVariable(pid, "paperDetails", paper);
		formService.submitTaskForm(taskId, map);

		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);

	}

	@GetMapping(value = "/reviewers/{pid}", produces = "application/json")
	@SuppressWarnings("unused")
	public @ResponseBody List<UserDTO> getReviewers(@PathVariable String pid) {

		List<FormSubmissionDto> paperDetails = (List<FormSubmissionDto>) runtimeService.getVariable(pid,
				"paperDetails");

		ScientificArea sc = new ScientificArea();
		Magazine m = new Magazine();
		String fileName = "";
		for (FormSubmissionDto d : paperDetails) {
			if (d.getFieldId().equals("scientificArea")) {
				sc = scientificAreaRepository.findByName(d.getFieldValue());
			}
			if (d.getFieldId().equals("magazine")) {
				m = magazineRepository.findByName(d.getFieldValue());
			}
			if (d.getFieldId().equals("file")) {
				fileName = d.getFieldValue();
			}
		}

		List<UserDTO> dto = new ArrayList<UserDTO>();

		/*//pod A
		 * List<User> magazineReviewers = m.getReviewers(); List<User> matchingReviewers
		 * = new ArrayList<User>(); for (User u : magazineReviewers) { for
		 * (ScientificArea s : u.getScientificAreas()) { if
		 * (s.getName().equals(sc.getName())) { matchingReviewers.add(u); } } }
		 * 
		 * for (User u : matchingReviewers) { UserDTO userDto = new UserDTO();
		 * userDto.setUsername(u.getUsername()); dto.add(userDto); }
		 * 
		 * return dto;
		 */

		// filtriranje pod B;

		DocumentHandler documentHandler = indexer.getHandler(fileName);
		Resource resource = storageService.loadFile(fileName);
		File newFile = null;
		try {
			newFile = new File(resource.getURI());
		} catch (IOException e) {
			e.printStackTrace();
		}
		String value = documentHandler.getText(newFile);
		SimpleQuery sq = new SimpleQuery();
		sq.setField("text");
		sq.setValue(value);
		Set<User> getMoreLikeThisReviewers = resultRetriever.getMoreLikeThisReviewers(sq);
		List<User> MLTReviewers = new ArrayList<>(getMoreLikeThisReviewers);
		for (User u : MLTReviewers) {
			UserDTO userDto = new UserDTO();
			userDto.setUsername(u.getUsername());
			dto.add(userDto);
		}

		return dto;

	}

	@PostMapping(value = "/reviewers/{pid}/{taskId}")
	public ResponseEntity<List<UserDTO>> addChosenReviewers(@PathVariable String taskId, @PathVariable String pid,
			@RequestBody List<UserDTO> dto) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<String> usernames = new ArrayList<String>();
		for (UserDTO u : dto) {
			usernames.add(u.getUsername());
		}
		runtimeService.setVariable(pid, "reviewersList", usernames);
		runtimeService.setVariable(pid, "reviewersListSize", usernames.size());
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<List<UserDTO>>(HttpStatus.OK);
	}

	@GetMapping(value = "/reviewers/reelections/{pid}", produces = "application/json")
	public @ResponseBody List<UserDTO> getAvailableReviewers(@PathVariable String pid) {
		List<String> available = (List<String>) runtimeService.getVariable(pid, "availableAdditionalReviewers");
		List<UserDTO> dto = new ArrayList<UserDTO>();

		for (String username : available) {
			UserDTO user = new UserDTO();
			user.setUsername(username);
			dto.add(user);
		}

		return dto;
	}

	@GetMapping(value = "/unfinishedReviews/{pid}", produces = "application/json")
	public @ResponseBody UnfinishedReviewDTO getNum(@PathVariable String pid) {
		int reviewsUnfinished = (int) runtimeService.getVariable(pid, "reviewsUnfinished");
		UnfinishedReviewDTO num = new UnfinishedReviewDTO();
		num.setNumberOfUnfinishedReviews(reviewsUnfinished);

		return num;

	}

	@PostMapping(value = "/reviewers/reelections/{pid}/{taskId}")
	public ResponseEntity<List<UserDTO>> addRechosenReviewers(@PathVariable String taskId, @PathVariable String pid,

			@RequestBody List<UserDTO> dto) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		List<String> usernames = new ArrayList<String>();
		for (UserDTO u : dto) {
			usernames.add(u.getUsername());
		}
		runtimeService.setVariable(pid, "reviewersList", usernames);
		runtimeService.setVariable(pid, "reviewersListSize", usernames.size());
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<List<UserDTO>>(HttpStatus.OK);
	}

	@PostMapping(value = "/reviewers/decisions/{taskId}")
	public ResponseEntity<FormSubmissionDto> submitReview(@PathVariable String taskId,
			@RequestBody List<FormSubmissionDto> dto) {
		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		List<FormSubmissionDto> paper = (List<FormSubmissionDto>) runtimeService
				.getVariable(task.getProcessInstanceId(), "paperDetails");
		String paperName = "";
		for (FormSubmissionDto d : paper) {
			if (d.getFieldId().equals("title")) {
				paperName = d.getFieldValue();
			}
		}

		int noOfFinishedReviews;
		try {
			noOfFinishedReviews = (int) runtimeService.getVariable(task.getProcessInstanceId(), "noOfFinishedReviews");
		} catch (Exception e) {
			System.out.println("No reviews yet");
			noOfFinishedReviews = 0;
		}
		noOfFinishedReviews++;

		reviewService.addReview(paperName, dto);
		runtimeService.setVariable(task.getProcessInstanceId(), "noOfFinishedReviews", noOfFinishedReviews);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);

	}

	@PostMapping(value = "/editors/decisions/{taskId}")
	public ResponseEntity<FormSubmissionDto> submitDecision(@PathVariable String taskId,
			@RequestBody List<FormSubmissionDto> dto) {

		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "assignedEditorDecision", dto);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);
	}

	@GetMapping(value = "/finished/{taskId}", produces = "application/json")
	public @ResponseBody List<ReviewDTO> loadReviews(@PathVariable String taskId) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		List<FormSubmissionDto> paperr = (List<FormSubmissionDto>) runtimeService
				.getVariable(task.getProcessInstanceId(), "paperDetails");
		Paper paper = null;
		for (FormSubmissionDto d : paperr) {
			if (d.getFieldId().equals("title")) {
				paper = paperRepository.findByTitle(d.getFieldValue());
			}
		}

		List<Review> reviewsList = paper.getReviews();
		List<ReviewDTO> dto = new ArrayList<>();
		for (Review r : reviewsList) {
			ReviewDTO d = new ReviewDTO();
			d.setReviewer(r.getReviewer().getUsername());
			d.setReviewerAuthorComment(r.getReviewerAuthorComment());
			d.setReviewerComment(r.getReviewerComment());
			d.setSuggestion(r.getSuggestion());
			dto.add(d);
		}

		return dto;
	}

}
