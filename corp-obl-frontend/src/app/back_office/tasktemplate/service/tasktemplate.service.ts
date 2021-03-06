import {Injectable} from '@angular/core';
import {AppConfig} from '../../../shared/common/api/app-config';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {Observable} from 'rxjs';

@Injectable()
export class TaskTemplateService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    getTaskTemplates(): Observable<any> {

        return this.apiRequest.get(this.appConfig.getTaskTemplates);
    }

    getTaskTemplatesForTable(): Observable<any> {

        return this.apiRequest.get(this.appConfig.getTaskTemplatesForTable);
    }

    saveUpdateTaskTemplate(taskTemplate): Observable<any> {
        console.log('TaskTemplateService - saveUpdateTaskTemplate');

        return this.apiRequest.post(this.appConfig.createUpdateTaskTemplate, taskTemplate);
    }

    searchTaskTemplate(objectSearchTaskTemplate): Observable<any> {
        console.log('TaskTemplateService - searchTaskTemplate');

        return this.apiRequest.post(this.appConfig.searchTaskTemplate, objectSearchTaskTemplate);
    }

    searchTaskTemplateByDescr(description): Observable<any> {
        console.log('TaskTemplateService - searchTaskTemplateByDescr');

        return this.apiRequest.post(this.appConfig.searchTaskTemplateByDescr, description);
    }

    deleteTaskTemplate(taskTemplate): Observable<any> {
        console.log('TaskTemplateService - deleteTaskTemplate');

        return this.apiRequest.put(this.appConfig.deleteTaskTemplate, taskTemplate);
    }
}
