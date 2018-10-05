package com.tx.co.back_office.tasktemplate.resource;

import static com.tx.co.common.constants.ApiConstants.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tx.co.back_office.task.api.model.TaskResult;
import com.tx.co.back_office.task.model.Task;
import com.tx.co.back_office.tasktemplate.api.model.ObjectSearchTaskTemplateResult;
import com.tx.co.back_office.tasktemplate.api.model.TaskTemplateResult;
import com.tx.co.back_office.tasktemplate.domain.TaskTemplate;
import com.tx.co.back_office.tasktemplate.service.ITaskTemplateService;
import com.tx.co.common.api.model.StringResult;
import com.tx.co.common.api.provider.ObjectResult;

@Component
@Path(BACK_OFFICE)
public class TaskTemplateResource extends ObjectResult {

	private static final Logger logger = LogManager.getLogger(TaskTemplateResource.class);

	@Context
	private UriInfo uriInfo; 

	private ITaskTemplateService taskTemplateService;

	@Autowired
	public void setTaskTemplateService(ITaskTemplateService taskTemplateService) {
		this.taskTemplateService = taskTemplateService;
	}

	@GET
    @Path(TASK_TEMPLATE_LIST_FOR_TABLE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
//    @PreAuthorize("hasAuthority('"+ ADMIN_ROLE +"')")
    public Response getTaskTemplatesForTable() {

		Iterable<Task> taskForTableIterable = taskTemplateService.getTasksForTable();
        List<TaskResult> queryDetailsList =
                StreamSupport.stream(taskForTableIterable.spliterator(), false)
                        .map(this::toTaskResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
    }
	
	@POST
	@Path(TASK_TEMPLATE_CREATE_UPDATE)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createUpdateTaskTemplate(TaskTemplateResult taskTemplateResult) {

		TaskTemplate taskTemplateStored = taskTemplateService.saveUpdateTaskTemplate(toTaskTemplate(taskTemplateResult));

		return Response.ok(toTaskTemplateWithTaskResult(taskTemplateStored)).build();
	}
	
	@POST
	@Path(TASK_TEMPLATE_SEARCH)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response searchTaskTemplateTable(ObjectSearchTaskTemplateResult objectSearchTaskTemplateResult) {

		Iterable<Task> taskForTableIterable = taskTemplateService.searchTaskTemplate(toObjectSearchTaskTemplate(objectSearchTaskTemplateResult));
        List<TaskResult> queryDetailsList =
                StreamSupport.stream(taskForTableIterable.spliterator(), false)
                        .map(this::toTaskResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
	}
	
	@POST
	@Path(TASK_TEMPLATE_SEARCH_BY_DESCR)
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Response searchTaskTemplateTable(StringResult description) {

		List<TaskTemplate> taskTemplateIterable = taskTemplateService.searchTaskTemplateByDescr(description.getResult());
        List<TaskTemplateResult> queryDetailsList =
                StreamSupport.stream(taskTemplateIterable.spliterator(), false)
                        .map(this::toTaskTemplateResult)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
	}

}
