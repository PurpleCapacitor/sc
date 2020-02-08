package root.demo.dto;

import java.io.Serializable;
import java.util.List;

import root.demo.model.CustomFormField;

@SuppressWarnings("serial")
public class CustomFormFieldDTO implements Serializable {
	
	String taskId;
	List<CustomFormField> formFields;
	String processInstanceId;
	
	public CustomFormFieldDTO(String taskId, String processInstanceId, List<CustomFormField> formFields) {
		super();
		this.taskId = taskId;
		this.formFields = formFields;
		this.processInstanceId = processInstanceId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public List<CustomFormField> getFormFields() {
		return formFields;
	}

	public void setFormFields(List<CustomFormField> formFields) {
		this.formFields = formFields;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
	
	

}
