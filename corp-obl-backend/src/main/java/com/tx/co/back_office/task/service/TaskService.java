package com.tx.co.back_office.task.service;

import static org.springframework.util.ObjectUtils.isEmpty;
import static com.tx.co.common.constants.AppConstants.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.tx.co.back_office.office.domain.Office;
import com.tx.co.back_office.office.service.OfficeService;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.task.model.TaskOffice;
import com.tx.co.back_office.task.model.TaskOfficeRelations;
import com.tx.co.back_office.task.repository.TaskOfficeRelationRepository;
import com.tx.co.back_office.task.repository.TaskOfficeRepository;
import com.tx.co.back_office.task.repository.TaskRepository;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplate.service.ITaskTemplateService;
import com.tx.co.cache.service.UpdateCacheData;
import com.tx.co.common.scheduler.service.Scheduler;
import com.tx.co.common.utils.UtilStatic;
import com.tx.co.front_end.expiration.domain.Expiration;
import com.tx.co.front_end.expiration.service.IExpirationService;
import com.tx.co.security.api.AuthenticationTokenUserDetails;
import com.tx.co.security.api.usermanagement.IUserManagementDetails;
import com.tx.co.security.domain.Authority;
import com.tx.co.security.exception.GeneralException;
import com.tx.co.user.domain.User;

/**
 * Service for {@link TaskService}s.
 *
 * @author aazo
 */
@Service
public class TaskService extends UpdateCacheData implements ITaskService, IUserManagementDetails {

	private static final Logger logger = LogManager.getLogger(TaskService.class);

	private TaskRepository taskRepository;
	private TaskOfficeRepository taskOfficeRepository;
	private TaskOfficeRelationRepository taskOfficeRelationRepository;
	private IExpirationService expirationService; 
	private OfficeService officeService;
	private ITaskTemplateService taskTemplateService;
	private Scheduler scheduler;

	@Autowired
	public void setTaskRepository(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	@Autowired
	public void setTaskOfficeRepository(TaskOfficeRepository taskOfficeRepository) {
		this.taskOfficeRepository = taskOfficeRepository;
	}

	@Autowired
	public void setTaskOfficeRelationRepository(TaskOfficeRelationRepository taskOfficeRelationRepository) {
		this.taskOfficeRelationRepository = taskOfficeRelationRepository;
	}

	@Autowired
	public void setExpirationService(IExpirationService expirationService) {
		this.expirationService = expirationService;
	}

	@Autowired
	@Override
	public void setOfficeService(OfficeService officeService) {
		this.officeService = officeService;
	}

	@Autowired
	public void setTaskTemplateService(ITaskTemplateService taskTemplateService) {
		this.taskTemplateService = taskTemplateService;
	}

	@Autowired
	public void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
	}

	/**
	 * @author rfratti
	 * @return return list of enabled task for scheduler
	 */
	@Override
	public List<Task> getAllTasksForScheduler() {
		return taskRepository.getTasks();
	}

	@Override
	public List<Task> getTasks() {

		User userLoggedIn = getTokenUserDetails().getUser();
		String username = userLoggedIn.getUsername();

		List<Task> tasks = new ArrayList<>();
		if (userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_ADMIN)) {
			tasks = taskRepository.getTasks();
		} else if (userLoggedIn.getAuthorities().contains(Authority.CORPOBLIG_BACKOFFICE_FOREIGN)) {
			tasks = taskRepository.getTasksByRole(username);
		}

		logger.info("The number of the tasks: " + tasks.size());

		return tasks;
	}

	@Override
	public AuthenticationTokenUserDetails getTokenUserDetails() {
		return (AuthenticationTokenUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	}

	@Override
	public Task saveUpdateTask(Task task) {
		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		Task taskStored = null;

		// New Task template
		if (isEmpty(task.getIdTask())) {
			task.setCreationDate(new Date());
			task.setCreatedBy(username);
			task.setEnabled(true);
			taskStored = task;

			logger.info("Creating the new task");
		} else { // Existing Task template
			Optional<Task> taskOptional = taskRepository.findById(task.getIdTask());

			if (taskOptional.isPresent()) {
				taskStored = taskOptional.get();
			} else {
				throw new GeneralException("Task not found, id: " + task.getIdTask());
			}

			logger.info("Updating the task with id: " + taskStored.getIdTask());
		}

		taskStored.setDay(task.getDay());
		taskStored.setDaysBeforeShowExpiration(task.getDaysBeforeShowExpiration());
		taskStored.setDaysOfNotice(task.getDaysOfNotice());
		taskStored.setExpirationType(task.getExpirationType());
		taskStored.setFrequenceOfNotice(task.getFrequenceOfNotice());
		taskStored.setRecurrence(task.getRecurrence());
		taskStored.setModificationDate(new Date());
		taskStored.setModifiedBy(username);

		taskStored = taskRepository.save(taskStored);

		scheduler.elaborateTask(taskStored);

		return taskTaskOfficeSaveUpdate(taskStored, task);
	}

	public Task taskTaskOfficeSaveUpdate(Task taskStored, Task task) {

		taskStored.setOffice(task.getOffice());

		long startTime = System.currentTimeMillis();

		taskStored.setTaskOffices(mergeTaskOffice(taskStored, task.getTaskOfficesFilterEnabled()));

		UtilStatic.DurationExecutionMethod(startTime, "mergeTaskOffice", logger);

		if (!isEmpty(task.getTaskOfficesFilterEnabled())) {
			List<TaskOffice> taskOffices = new ArrayList<>();
			for (TaskOffice taskOfficeLoop : task.getTaskOffices()) {

				startTime = System.currentTimeMillis();

				TaskOffice taskOffice = saveUpdateTaskOffice(taskStored, taskOfficeLoop);

				UtilStatic.DurationExecutionMethod(startTime, "saveUpdateTaskOffice", logger);

				taskOffices.add(taskOffice);
			}
			taskStored.setTaskOffices(new HashSet<TaskOffice>(taskOffices));
		}

		logger.info("Stored the task with id: " + taskStored.getIdTask());

		return taskStored;
	}

	@Override
	public TaskOffice saveUpdateTaskOffice(Task task, TaskOffice taskOffice) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskOffice taskOfficeStored = null;

		// New Task office
		if (isEmpty(taskOffice.getIdTaskOffice())) {

			if (!isEmpty(task.getTaskTemplate()) && !isEmpty(task.getTaskTemplate().getIdTaskTemplate()) &&
					!isEmpty(taskOffice.getTask()) && !isEmpty(taskOffice.getTask().getIdTask()) &&
					!isEmpty(taskOffice.getOffice()) && !isEmpty(taskOffice.getOffice().getIdOffice())) {
				taskOfficeStored = taskOfficeRepository.getAllTaskOfficeByTaskTemplateAndTaskAndOffice(task.getTaskTemplate(), taskOffice.getTask(), taskOffice.getOffice());	
			}

			if (isEmpty(taskOfficeStored)) {
				taskOffice.setCreationDate(new Date());
				taskOffice.setCreatedBy(username);

				taskOfficeStored = taskOffice;

				logger.info("Creating the new taskOffice");
			}

			Date date = null;
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_DATETIME);
			try {
				date = sdf.parse(OFFICE_TASK_END_DATE);
			} catch (ParseException e) {
				logger.error("Error parsing date", e);
			}
			taskOfficeStored.setEndDate(date);

			taskOfficeStored.setTask(task);
			taskOfficeStored.setStartDate(new Date());
			taskOfficeStored.setEnabled(taskOfficeStored.getTask().getEnabled());
			taskOfficeStored.setCreatedBy(username);
			taskOfficeStored.setCreationDate(new Date());
			taskOfficeStored.setModifiedBy(username);
			taskOfficeStored.setModificationDate(new Date());

			taskOfficeStored = taskOfficeRepository.save(taskOfficeStored);

		} else { // Existing Task template
			Optional<TaskOffice> taskOfficeOptional = taskOfficeRepository.findById(taskOffice.getIdTaskOffice());

			if (taskOfficeOptional.isPresent()) {
				taskOfficeStored = taskOfficeOptional.get();
			} else {
				throw new GeneralException("Task office not found, id: " + taskOffice.getIdTaskOffice());
			}

			logger.info("Updating the taskOffice with id: " + taskOfficeStored.getIdTaskOffice());
		}

		// Verify if the object has been changed by the user
		// if yes, process the data otherwise skip it
		Boolean taskOfficeWithRelationOnChange = checkTaskOfficeWithRelation(taskOffice, taskOfficeStored);

		if (!isEmpty(taskOffice.getTaskOfficeRelations()) && 
				!isEmpty(taskOfficeStored.getIdTaskOffice()) &&
				taskOfficeWithRelationOnChange) {
			List<TaskOfficeRelations> taskOfficeRelations = new ArrayList<>();
			for (TaskOfficeRelations taskOfficeRelationLoop : taskOffice.getTaskOfficeRelations()) {
				taskOfficeRelationLoop.setTaskOffice(taskOfficeStored);
				taskOfficeRelations.add(taskOfficeRelationLoop);
			}
			taskOfficeStored.setTaskOfficeRelations(new HashSet<>(taskOfficeRelations));
		}


		if (!isEmpty(taskOfficeStored.getTaskOfficeRelations())) {

			if (taskOfficeWithRelationOnChange) {
				taskOfficeRelationRepository.updateTaskOfficeRelationsNotEnableByTaskOffice(taskOfficeStored, username);	
			}

			for (TaskOfficeRelations taskOfficeRelationLoop : taskOfficeStored.getTaskOfficeRelations()) {

				long startTime = System.currentTimeMillis();

				if (taskOfficeWithRelationOnChange) {
					saveUpdateTaskOfficeRelation(taskOfficeRelationLoop, taskOfficeStored);	
				}


				UtilStatic.DurationExecutionMethod(startTime, "saveUpdateTaskOfficeRelation", logger);
			}
		}

		logger.info("Stored the taskOffice with id: " + taskOfficeStored.getIdTaskOffice());

		return taskOffice;
	}

	/**
	 * Verify if the object has been changed by the user
	 * 
	 * @param taskOffice
	 * @param taskOfficeStored
	 * @return if true the data base been changed process the data, otherwise skip it
	 */
	private boolean checkTaskOfficeWithRelation(TaskOffice taskOffice, TaskOffice taskOfficeStored) {

		if (isEmpty(taskOffice.getTaskOfficeRelations()) ||
				taskOffice.getTaskOfficeRelations().size() != taskOfficeStored.getTaskOfficeRelations().size()) {
			return true;
		} else if (taskOffice.getTaskOfficeRelations().size() == taskOfficeStored.getTaskOfficeRelations().size() &&
				taskOffice.getTaskOfficeRelations().equals(taskOfficeStored.getTaskOfficeRelations())) {
			return false;
		}

		return true;
	}

	@Override
	public TaskOfficeRelations saveUpdateTaskOfficeRelation(TaskOfficeRelations taskOfficeRelation, TaskOffice taskOffice) {

		TaskOffice taskOfficeLight = new TaskOffice();
		taskOfficeLight.setIdTaskOffice(taskOffice.getIdTaskOffice());

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		TaskOfficeRelations taskOfficeRelationStored = null;

		long startTime = System.currentTimeMillis();

		Optional<TaskOfficeRelations> taskOfficeRelationOptional = taskOfficeRelationRepository.
				getTaskOfficeRelationsByUsernameAndTaskOffice(taskOfficeRelation.getUsername(), taskOffice);

		UtilStatic.DurationExecutionMethod(startTime, "getTaskOfficeRelationsByUsernameAndTaskOffice", logger);

		// Existing Task template
		if (taskOfficeRelationOptional.isPresent()){ 
			taskOfficeRelationStored = taskOfficeRelationOptional.get();

			if (taskOfficeRelationStored == null) {
				taskOfficeRelationStored = taskOfficeRelation;
			}
			logger.info("Updating the taskOfficeRelation with id: " + (!isEmpty(taskOfficeRelationStored) ? taskOfficeRelationStored.getIdTaskOfficeRelation() : ""));

			// New Task office relation
		} else if (isEmpty(taskOfficeRelation.getIdTaskOfficeRelation())) {
			taskOfficeRelation.setCreationDate(new Date());
			taskOfficeRelation.setCreatedBy(username);

			taskOfficeRelationStored = taskOfficeRelation;

			logger.info("Creating the new taskOfficeRelation");
		} 

		if (!isEmpty(taskOfficeRelationStored)) {

			taskOfficeRelationStored.setUsername(taskOfficeRelation.getUsername());
			taskOfficeRelationStored.setTaskOffice(taskOffice);
			taskOfficeRelationStored.setRelationType(taskOfficeRelation.getRelationType());
			taskOfficeRelationStored.setModifiedBy(username);
			taskOfficeRelationStored.setModificationDate(new Date());
			taskOfficeRelationStored.setEnabled(taskOffice.getEnabled());

			startTime = System.currentTimeMillis();

			taskOfficeRelationStored = taskOfficeRelationRepository.save(taskOfficeRelationStored);

			UtilStatic.DurationExecutionMethod(startTime, "save taskOfficeRelationStored", logger);

			logger.info("Stored the taskOfficeRelation with id: " + taskOfficeRelationStored.getIdTaskOfficeRelation());
		}

		return taskOfficeRelationStored;
	}

	public Set<TaskOffice> mergeTaskOffice(Task taskStored, Set<TaskOffice> taskOffices) {

		logger.info("Merging Task - Office");

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		Set<TaskOffice> taskOfficesToSave = new HashSet<>();
		for (TaskOffice taskOffice : taskOffices) {

			if (isEmpty(taskOffice.getIdTaskOffice())) {
				taskOffice.setTaskTemplate(taskStored.getTaskTemplate());
				taskOfficesToSave.add(taskOffice);
			} else if (!taskStored.getTaskOfficesFilterEnabled()
					.stream()
					.anyMatch(toff -> toff.getIdTaskOffice()
							.compareTo(taskOffice.getIdTaskOffice()) == 0)) {

				taskOfficesToSave.add(taskOffice);
			}


			for (TaskOffice taskOfficeStored : taskStored.getTaskOfficesFilterEnabled()) {
				if (isEmpty(taskOffice.getIdTaskOffice()) || 
						(!isEmpty(taskOffice.getIdTaskOffice()) && !isEmpty(taskOfficeStored.getIdTaskOffice()) && 
								taskOffice.getIdTaskOffice().compareTo(taskOfficeStored.getIdTaskOffice()) == 0)) {

					taskOfficesToSave.add(taskOffice);
				}
			}
		}

		Set<TaskOffice> taskOfficesToRemove = taskStored.getTaskOfficesFilterEnabled();
		taskOfficesToRemove.removeAll(taskOfficesToSave);
		if (!isEmpty(taskOfficesToRemove)) {
			logger.info("Number of task offices to remove: " + taskOfficesToRemove.size());

			for (TaskOffice taskOffice : taskOfficesToRemove) {
				if ( (isEmpty(taskStored.getOffice()) || 
						(!isEmpty(taskOffice.getOffice()) && 
								!isEmpty(taskStored.getOffice()) &&
								taskOffice.getOffice().getIdOffice().compareTo(taskStored.getOffice().getIdOffice()) == 0))) {

					taskOffice.setEnabled(false);
					taskOffice.setEndDate(new Date());
					taskOffice.setModifiedBy(username);

					if (taskOfficeRepository.findById(taskOffice.getIdTaskOffice()).isPresent()) {
						taskOfficeRepository.save(taskOffice);	

						if (!isEmpty(taskOffice.getTaskOfficeRelations())) {
							for (TaskOfficeRelations taskOfficeRelations : taskOffice.getTaskOfficeRelations()) {
								officeService.deleteTaskOfficeRelations(taskOfficeRelations);
							}
						}
					}
				}
			}
			taskStored.setTaskOffices(new HashSet<>());
		}

		logger.info("Number of task offices to save: " + taskOfficesToSave.size());
		taskStored.getTaskOffices().addAll(taskOfficesToSave);

		return taskStored.getTaskOffices();
	}

	@Override
	public TaskOffice getTaskOfficeByTaskTemplateAndOffice(TaskTemplate taskTemplate, Office office) {
		return taskOfficeRepository.getTaskOfficeByTaskTemplateAndOffice(taskTemplate, office);
	}

	@Override
	public void deleteTask(Task task) {

		if (!isEmpty(task)) {

			// The modification of User
			String username = getTokenUserDetails().getUser().getUsername();

			Task taskStored;
			Optional<Task> taskOptional = taskRepository.findById(task.getIdTask());
			if (taskOptional.isPresent()) {
				taskStored = taskOptional.get();
			} else {
				throw new GeneralException("Task not found, id: " + task.getIdTask());
			}

			taskStored.setEnabled(false);
			taskStored.setModificationDate(new Date());
			taskStored.setModifiedBy(username);

			taskStored = taskRepository.save(taskStored);

			taskTaskOfficeSaveUpdate(taskStored, task);

			if (!isEmpty(taskStored.getExpirationsFilterEnabled())) {
				for (Expiration expiration : taskStored.getExpirationsFilterEnabled()) {
					expirationService.deleteExpiration(expiration.getIdExpiration());
				}
			}

			// Reuse task later on
			task = taskStored;
		}
	}

	@Override
	public void deleteTaskOffice(TaskOffice taskOffice) {

		// The modification of User
		String username = getTokenUserDetails().getUser().getUsername();

		if (!isEmpty(taskOffice) && !isEmpty(taskOffice.getOffice())) {

			Optional<TaskOffice> taskOfficeOptional = taskOfficeRepository.findById(taskOffice.getIdTaskOffice());
			if (taskOfficeOptional.isPresent()) {

				TaskOffice taskOfficeStored = taskOfficeOptional.get();

				taskOfficeStored.setEnabled(false);
				taskOfficeStored.setEndDate(new Date());
				taskOfficeStored.setModifiedBy(username);

				taskOfficeRepository.save(taskOfficeStored);	

				taskOfficeRelationRepository.updateTaskOfficeRelationsNotEnableByTaskOffice(taskOfficeStored, username);

				if (isEmpty(taskOfficeStored.getTask().getTaskOfficesFilterEnabled())) {
					deleteTask(taskOfficeStored.getTask());

					if (isEmpty(taskOfficeStored.getTask().getTaskTemplate().getTaskFilterEnabled())) {
						taskTemplateService.deleteTaskTemplate(taskOfficeStored.getTask().getTaskTemplate());
					}
				}
			}
		} else if (!isEmpty(taskOffice) && 
				!isEmpty(taskOffice.getTask()) && 
				isEmpty(taskOffice.getTask().getTaskOfficesFilterEnabled())) {

			deleteTask(taskOffice.getTask());

			if (isEmpty(taskOffice.getTask().getTaskTemplate().getTaskFilterEnabled())) {
				taskTemplateService.deleteTaskTemplate(taskOffice.getTask().getTaskTemplate());
			}
		}
	}

	@Override
	public Task getTasksByTaskTemplate(TaskTemplate taskTemplate) {

		List<Task> tasks = taskRepository.getTasksByTaskTemplate(taskTemplate);

		Optional<Task> firstTask = tasks.stream().findFirst();

		if (firstTask.isPresent()) {
			return firstTask.get();
		}
		return null;
	}
}
