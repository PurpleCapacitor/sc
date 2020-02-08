package root.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import root.demo.dto.CustomFormFieldDTO;
import root.demo.dto.FormFieldsDto;
import root.demo.dto.FormSubmissionDto;
import root.demo.model.CustomFormField;
import root.demo.model.Magazine;
import root.demo.model.ScientificArea;
import root.demo.repositories.MagazineRepository;
import root.demo.repositories.UserRepository;

@Controller
@RequestMapping(value = "/magazines")
public class MagazineController {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	UserRepository userRepository;
	
	@Autowired
	MagazineRepository magazineRepository;

	private DummyController dc = new DummyController();

	@GetMapping(value = "/initializeTask", produces = "application/json")
	public @ResponseBody FormFieldsDto startMagazineProcess() {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("magazine");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
	}

	@PostMapping(value = "/login/{taskId}")
	public ResponseEntity<FormSubmissionDto> authenticate(@RequestBody List<FormSubmissionDto> list,
			@PathVariable String taskId) {

		HashMap<String, Object> map = dc.mapListToDto(list);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "magazineLoginDetails", list);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);
	}

	@GetMapping(value = "/fields/{ProcessInstanceId}", produces = "application/json")
	public @ResponseBody FormFieldsDto newMagazineFields(@PathVariable String ProcessInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(ProcessInstanceId).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), ProcessInstanceId, properties);
	}

	@PostMapping(value = "/{taskId}")
	public ResponseEntity<FormSubmissionDto> makeMagazine(@PathVariable String taskId,
			@RequestBody List<FormSubmissionDto> list) {
		HashMap<String, Object> map = dc.mapListToDto(list);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "magazineDetails", list);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);
	}

	@GetMapping(value = "/fields/personnel/{ProcessInstanceId}", produces = "application/json")
	public @ResponseBody CustomFormFieldDTO addEditorsReviewers(@PathVariable String ProcessInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(ProcessInstanceId).list().get(0);
		//TODO verovatno nista od ovoga ne radi, menjao sam model usera, tako da ceo proces magazine je pod pitanjem, ako stignes
		// proveri da li radi, ako ne, jbg
		List<CustomFormField> properties = new ArrayList<CustomFormField>();
		String editorUsername = (String) runtimeService.getVariable(task.getProcessInstanceId(), "editorValue");
		@SuppressWarnings("unchecked")
		List<String> reviewersUsernames = (List<String>) runtimeService.getVariable(task.getProcessInstanceId(),
				"reviewerList");
		CustomFormField editorForm = new CustomFormField("editor", "Editor", "checkBox", editorUsername);
		properties.add(editorForm);
		for (String s : reviewersUsernames) {
			CustomFormField reviewer = new CustomFormField("reviewer", "Reviewer", "checkBox", s);
			properties.add(reviewer);
		}

		return new CustomFormFieldDTO(task.getId(), ProcessInstanceId, properties);
	}

	@PostMapping(value = "/submitPersonnel/{magName}/{taskId}")
	public ResponseEntity<FormSubmissionDto> submitFields(@PathVariable String taskId, @PathVariable String magName,
			@RequestBody List<FormSubmissionDto> dto) {
		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getProcessInstanceId(), "magazineName", magName);

		List<String> reviewers = new ArrayList<String>();		
		for (FormSubmissionDto d : dto) {
			if (d.getFieldId().contains("editor")) {
				if (d.getFieldValue().equals("true")) {
					runtimeService.setVariable(task.getProcessInstanceId(), "chosenEditorUsername", d.getFieldId());
				}
			}
			
			if (d.getFieldId().contains("reviewer")) {
				if (d.getFieldValue().equals("true")) {
					reviewers.add(d.getFieldId());
				}
			}
		}

		runtimeService.setVariable(task.getProcessInstanceId(), "chosenReviewersUsernames", reviewers);
		taskService.complete(taskId);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/approve/{ProcessInstanceId}", produces = "application/json")
	public @ResponseBody FormFieldsDto approveMagazine(@PathVariable String ProcessInstanceId) {
		Task task = taskService.createTaskQuery().processInstanceId(ProcessInstanceId).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), ProcessInstanceId, properties);
	}
	
	@PostMapping(value = "/decisions/{taskId}")
	public ResponseEntity<FormSubmissionDto> submitDecision(@PathVariable String taskId, @RequestBody List<FormSubmissionDto> dto) {
		HashMap<String, Object> map = dc.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		boolean decision = false; 
		for (FormSubmissionDto d : dto) {
			if(d.getFieldId().equals("accept")) {
				if(d.getFieldValue().equals("true")) 
					decision = true;
			}
		}
		runtimeService.setVariable(task.getProcessInstanceId(), "decision", decision);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);
	}
	
	@GetMapping(value = "/scAreas/{magazineName}", produces = "application/json")
	public ResponseEntity<List<String>> getScAreas(@PathVariable String magazineName) { 
		Magazine m = magazineRepository.findByName(magazineName);
		List<ScientificArea> scs = m.getScientificAreas();
		List<String> list = new ArrayList<String>();
		for (ScientificArea sc : scs) {
			list.add(sc.getName());
		}
		return new ResponseEntity<List<String>>(list, HttpStatus.OK);
	}

}
