package root.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
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

import root.demo.dto.FormFieldsDto;
import root.demo.dto.FormSubmissionDto;
import root.demo.dto.TaskDto;

@Controller
@RequestMapping("/welcome")
public class DummyController {

	public static ProcessInstance PI;

	@Autowired
	IdentityService identityService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@GetMapping(path = "/formFields", produces = "application/json")
	public @ResponseBody FormFieldsDto get() {
		PI = runtimeService.startProcessInstanceByKey("registration");

		Task task = taskService.createTaskQuery().processInstanceId(PI.getId()).list().get(0);

		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for (FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}

		runtimeService.setVariable(PI.getId(), "adminUsername", "test");
		return new FormFieldsDto(task.getId(), PI.getId(), properties);
	}

	@GetMapping(value = "/tasks/users/{pid}", produces = "application/json")
	public @ResponseBody FormFieldsDto getTaskFields(@PathVariable String pid) {
		Task task = taskService.createTaskQuery().processInstanceId(pid).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), pid, properties);
	}

	@GetMapping(path = "/tasks/{processInstanceId}", produces = "application/json")
	public @ResponseBody ResponseEntity<List<TaskDto>> get(@PathVariable String processInstanceId) {

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtos.add(t);
		}

		return new ResponseEntity<List<TaskDto>>(dtos, HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/post/{taskId}")
	public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {

		HashMap<String, Object> map = this.mapListToDto(dto);

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		System.out.println(task.getId().toString());
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "registration", dto);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@SuppressWarnings("rawtypes")
	@PostMapping(path = "/tasks/claim/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity claim(@PathVariable String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		String user = (String) runtimeService.getVariable(processInstanceId, "username");
		taskService.claim(taskId, user);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping(path = "/tasks/complete/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity<List<TaskDto>> complete(@PathVariable String taskId) {
		Task taskTemp = taskService.createTaskQuery().taskId(taskId).singleResult();
		taskService.complete(taskId);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(taskTemp.getProcessInstanceId()).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtos.add(t);
		}
		return new ResponseEntity<List<TaskDto>>(dtos, HttpStatus.OK);
	}

	@GetMapping(value = "/scAreas/start/{pid}", produces = "application/json")
	public @ResponseBody FormFieldsDto getScAreas(@PathVariable String pid) {

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(pid).list();
		Task task = null;
		for (Task taskk : tasks) {
			if (taskk.getName().equals("Pick Sc Areas")) {
				task = taskk;
			}
		}
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		for (FormField fp : properties) {
			System.out.println(fp.getId() + fp.getType());
		}
		return new FormFieldsDto(task.getId(), pid, properties);

	}

	@SuppressWarnings("rawtypes")
	@PostMapping(value = "/scAreas/{taskId}", produces = "application/json")
	public ResponseEntity writeScAreas(@PathVariable String taskId, @RequestBody List<FormSubmissionDto> dto) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		runtimeService.setVariable(task.getExecutionId(), "scAreas", dto);
		formService.submitTaskForm(task.getId(), map);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	public HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmissionDto temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}

}