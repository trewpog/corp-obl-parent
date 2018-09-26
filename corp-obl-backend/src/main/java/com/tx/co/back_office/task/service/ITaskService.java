package com.tx.co.back_office.task.service;

import java.util.List;

import com.tx.co.back_office.company.domain.Company;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.topic.domain.Topic;

public interface ITaskService {

	Task saveUpdateTask(Task task);
	
	List<Task> getTasks();
	
	List<Task> getTasksByDescriptionOrCompaniesOrTopics(String description, List<Company> companies, List<Topic> topics);
}
