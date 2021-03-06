import {Injectable} from '@angular/core';
import {ApiRequestService} from '../../shared/common/service/api-request.service';
import {AppConfig} from '../../shared/common/api/app-config';
import {Observable} from 'rxjs';

@Injectable()
export class ExpirationService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {
    }

    searchDateExpirationOffices(dateExpirationOfficesArchived): Observable<any> {
        return this.apiRequest.post(this.appConfig.expirationSearchTaskTemplateOfficeArchived, dateExpirationOfficesArchived);
    }

    saveUpdateExpirationActivity(expiration): Observable<any> {
        return this.apiRequest.post(this.appConfig.saveUpdateExpirationActivity, expiration);
    }

    updateTaskExpiration(idExpiration): Observable<any> {
        return this.apiRequest.post(this.appConfig.updateTaskExpiration, idExpiration);
    }

    statusExpirationOnChange(expiration): Observable<any> {
        return this.apiRequest.post(this.appConfig.statusExpirationOnChange, expiration);
    }

    saveStatusAllExpirationsOnChange(taskExpiration): Observable<any> {
        return this.apiRequest.post(this.appConfig.statusAllExpirationsOnChange, taskExpiration);
    }
}