package com.tx.co.back_office.tasktemplate.service;

import java.util.Optional;

import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;

public interface ITaskTemplateService {

	TaskTemplate saveUpdateTaskTemplate(TaskTemplate taskTemplate);
	
	Optional<TaskTemplate> findByIdTaskTemplate(Long idTaskTemplate);
}