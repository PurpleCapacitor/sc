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
import org.springframework.web.servlet.view.RedirectView;

import root.demo.dto.FormFieldsDto;
import root.demo.dto.FormSubmissionDto;
import root.demo.dto.TaskDto;
import root.demo.dto.UserDTO;
import root.demo.model.users.User;
import root.demo.model.users.UserDetails;
import root.demo.model.users.VerificationToken;
import root.demo.repositories.UserDetailsRepository;
import root.demo.repositories.UserRepository;
import root.demo.repositories.VerificationTokenRepository;

@Controller
@RequestMapping(value = "/users")
public class UserController {

	@Autowired
	VerificationTokenRepository verificationTokenRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	UserDetailsRepository userDetailsRepository;

	@Autowired
	RuntimeService runtimeService;

	@Autowired
	FormService formService;
	
	@Autowired
	TaskService taskService;
	
	@Autowired
	IdentityService identityService;

	@GetMapping(value = "/registrationConfirm/{token}")
	public RedirectView confirmEmail(@PathVariable String token) {
		VerificationToken vt = verificationTokenRepository.findByToken(token);
		if (vt != null) {
			UserDetails userDetails = userDetailsRepository.findByEmail(vt.getEmail());
			List<User> users = userRepository.findAll();

			for (User user : users) {
				if (user.getUserDetails().getId() == userDetails.getId()) {
					user.setActivated(true);
					userRepository.save(user);
					System.out.println("User updated");
					runtimeService.signalEventReceived("emailConfirmed");
					break;
				}
			}
		} else {
			throw new RuntimeException("Verification token not found");
		}

		return new RedirectView("http://localhost:4200");

	}

	@PostMapping(value = "/login")
	public ResponseEntity<UserDTO> login(@RequestBody UserDTO userDTO) {

		User user = userRepository.findByUsername(userDTO.getUsername());
		if (identityService.checkPassword(userDTO.getUsername(), userDTO.getPassword())) { 
				userDTO.setRole(user.getUserType().toString());
				System.out.println("Credentials valid, logging in");
				return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
			
		}
		if (user.getPassword().equals(userDTO.getPassword())) {
			userDTO.setRole(user.getUserType().toString());
			System.out.println("Credentials valid, logging in");
			return new ResponseEntity<UserDTO>(userDTO, HttpStatus.OK);
		} else {
			System.out.println("Bad credentials");
			identityService.checkPassword(userDTO.getUsername(), userDTO.getPassword());
			return new ResponseEntity<UserDTO>(HttpStatus.FORBIDDEN);
		}

	}

	@PostMapping(value = "/approveReviewer/{processInstanceId}/{taskId}")
	public ResponseEntity<?> approveReviewer(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId,
			@PathVariable String processInstanceId) {

		runtimeService.setVariable(processInstanceId, "formSubmission", dto);
		DummyController dc = new DummyController();
		HashMap<String, Object> map = dc.mapListToDto(dto);
		formService.submitTaskForm(taskId, map);
		return new ResponseEntity<FormSubmissionDto>(HttpStatus.OK);

	}
	
	@GetMapping(value = "/editors/{pid}/{username}")
	public ResponseEntity<List<TaskDto>> getEditorTasks(@PathVariable String pid, @PathVariable String username) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
		List<TaskDto> dtoList = new ArrayList<TaskDto>();
		for (Task task : taskList) {
			TaskDto dto = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtoList.add(dto);
		}

		return new ResponseEntity<List<TaskDto>>(dtoList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/editors/tasks/{taskId}", produces = "application/json")
	public @ResponseBody FormFieldsDto getEditorTaskFields(@PathVariable String taskId) {		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), properties);		
	}
	
	@GetMapping(value = "/tasks/{pid}/{username}", produces = "application/json")
	public ResponseEntity<List<TaskDto>> getAuthorTasks(@PathVariable String pid, @PathVariable String username) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
		List<TaskDto> dtoList = new ArrayList<TaskDto>();
		for (Task task : taskList) {
			TaskDto dto = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtoList.add(dto);
		}

		return new ResponseEntity<List<TaskDto>>(dtoList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/tasks/{taskId}", produces = "application/json")
	public @ResponseBody FormFieldsDto getAuthorTaskFields(@PathVariable String taskId) {		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), properties);		
	}
	
	@GetMapping(value = "/reviewers/{pid}/{username}")
	public ResponseEntity<List<TaskDto>> getReviewerTasks(@PathVariable String pid, @PathVariable String username) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
		List<TaskDto> dtoList = new ArrayList<TaskDto>();
		for (Task task : taskList) {
			TaskDto dto = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtoList.add(dto);
		}

		return new ResponseEntity<List<TaskDto>>(dtoList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/reviewers/tasks/{taskId}", produces = "application/json")
	public @ResponseBody FormFieldsDto getReviewerTaskFields(@PathVariable String taskId) {		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();		
		return new FormFieldsDto(task.getId(), properties);		
	}
	
	@GetMapping(value = "/admins/{pid}/{username}")
	public ResponseEntity<List<TaskDto>> getAdminTasks(@PathVariable String pid, @PathVariable String username) {
		List<Task> taskList = taskService.createTaskQuery().processInstanceId(pid).taskAssignee(username).list();
		List<TaskDto> dtoList = new ArrayList<TaskDto>();
		for (Task task : taskList) {
			TaskDto dto = new TaskDto(task.getId(), task.getName(), task.getAssignee());
			dtoList.add(dto);
		}

		return new ResponseEntity<List<TaskDto>>(dtoList, HttpStatus.OK);
	}
	
	@GetMapping(value = "/admins/tasks/{taskId}", produces = "application/json")
	public @ResponseBody FormFieldsDto getAdminTaskFields(@PathVariable String taskId) {		
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();		
		return new FormFieldsDto(task.getId(), properties);		
	}
	
	
	
	

}
