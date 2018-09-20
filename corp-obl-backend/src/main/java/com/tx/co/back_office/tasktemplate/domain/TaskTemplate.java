package com.tx.co.back_office.tasktemplate.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplateattachment.model.TaskTemplateAttachment;
import com.tx.co.back_office.topic.domain.Topic;

/**
 * Domain model that represents a task template.
 *
 * @author aazo
 */
@Entity
@Table(name = "co_tasktemplate")
public class TaskTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	private Long idTaskTemplate;

	@ManyToOne
	@JoinColumn(name = "topic_id")
	private Topic topic;
	
	@OneToMany(mappedBy="taskTemplate", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<TaskTemplateAttachment> taskTemplateAttachments = new HashSet<>();
	
	@OneToMany(mappedBy="taskTemplate", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Where(clause = "enabled = 1")
    private Set<Task> tasks = new HashSet<>();

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private String recurrence;

	@Column(nullable = false, name = "expirationtype")
	private String expirationType;

	@Column(nullable = false)
	private Integer day;

	@Column(nullable = false, name = "daysofnotice")
	private Integer daysOfNotice;
	
	@Column(nullable = false, name = "frequenceofnotice")
	private Integer frequenceOfNotice;

	@Column(nullable = false, name = "daysbeforeshowexpiration")
	private Integer daysBeforeShowExpiration;

	@Column(nullable = false, name = "expirationclosableby")
	private Integer expirationClosableBy;

	@Column(nullable = false)
    private Boolean enabled;
	
	@Column(nullable = false, name = "creationdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date creationDate;

	@Column(nullable = false, name = "createdby")
	private String createdBy;

	@Column(nullable = false, name = "modificationdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date modificationDate;

	@Column(nullable = false, name = "modifiedby")
	private String modifiedBy;

	public Long getIdTaskTemplate() {
		return idTaskTemplate;
	}

	public void setIdTaskTemplate(Long idTaskTemplate) {
		this.idTaskTemplate = idTaskTemplate;
	}

	public Topic getTopic() {
		return topic;
	}

	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	public Set<TaskTemplateAttachment> getTaskTemplateAttachments() {
		return taskTemplateAttachments;
	}

	public void setTaskTemplateAttachments(Set<TaskTemplateAttachment> taskTemplateAttachments) {
		this.taskTemplateAttachments = taskTemplateAttachments;
	}

	public Set<Task> getTasks() {
		return tasks;
	}

	public void setTasks(Set<Task> tasks) {
		this.tasks = tasks;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecurrence() {
		return recurrence;
	}

	public void setRecurrence(String recurrence) {
		this.recurrence = recurrence;
	}

	public String getExpirationType() {
		return expirationType;
	}

	public void setExpirationType(String expirationType) {
		this.expirationType = expirationType;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

	public Integer getDaysOfNotice() {
		return daysOfNotice;
	}

	public void setDaysOfNotice(Integer daysOfNotice) {
		this.daysOfNotice = daysOfNotice;
	}

	public Integer getFrequenceOfNotice() {
		return frequenceOfNotice;
	}

	public void setFrequenceOfNotice(Integer frequenceOfNotice) {
		this.frequenceOfNotice = frequenceOfNotice;
	}

	public Integer getDaysBeforeShowExpiration() {
		return daysBeforeShowExpiration;
	}

	public void setDaysBeforeShowExpiration(Integer daysBeforeShowExpiration) {
		this.daysBeforeShowExpiration = daysBeforeShowExpiration;
	}

	public Integer getExpirationClosableBy() {
		return expirationClosableBy;
	}

	public void setExpirationClosableBy(Integer expirationClosableBy) {
		this.expirationClosableBy = expirationClosableBy;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getModificationDate() {
		return modificationDate;
	}

	public void setModificationDate(Date modificationDate) {
		this.modificationDate = modificationDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}


}
