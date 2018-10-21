package com.tx.co.front_end.expiration.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tx.co.back_office.task.api.model.TaskResult;

/**
 * API model for returning task template expirations details.
 *
 * @author aazo
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskExpirationsResult {

	private Long idTaskTemplate;
	private String description;
	private Integer totalExpirations;
	private Integer totalCompleted;
	private String colorDefined;
	private String expirationDate;
	private TaskResult task;
	private List<ExpirationResult> expirations;
	
	public Long getIdTaskTemplate() {
		return idTaskTemplate;
	}
	public void setIdTaskTemplate(Long idTaskTemplate) {
		this.idTaskTemplate = idTaskTemplate;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getTotalExpirations() {
		return totalExpirations;
	}
	public void setTotalExpirations(Integer totalExpirations) {
		this.totalExpirations = totalExpirations;
	}
	public Integer getTotalCompleted() {
		return totalCompleted;
	}
	public void setTotalCompleted(Integer totalCompleted) {
		this.totalCompleted = totalCompleted;
	}
	public String getColorDefined() {
		return colorDefined;
	}
	public void setColorDefined(String colorDefined) {
		this.colorDefined = colorDefined;
	}
	public String getExpirationDate() {
		return expirationDate;
	}
	public void setExpirationDate(String expirationDate) {
		this.expirationDate = expirationDate;
	}
	public TaskResult getTask() {
		return task;
	}
	public void setTask(TaskResult task) {
		this.task = task;
	}
	public List<ExpirationResult> getExpirations() {
		return expirations;
	}
	public void setExpirations(List<ExpirationResult> expirations) {
		this.expirations = expirations;
	}
	
	
}
