package com.tx.co.back_office.tasktemplate.service;

import java.util.List;
import java.util.Optional;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.api.model.ObjectSearchTaskTemplate;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface ITaskTemplateService {

	List<Task> getTasksForTable();
	
	TaskTemplate saveUpdateTaskTemplate(TaskTemplate taskTemplate, Office office);
	
	Optional<TaskTemplate> findByIdTaskTemplate(Long idTaskTemplate);
	
	List<Task> searchTaskTemplate(ObjectSearchTaskTemplate objectSearchTaskTemplate);
	
	List<TaskTemplate> searchTaskTemplateByDescr(String description);
	
	void deleteTaskTemplate(TaskTemplate taskTemplate);
}
