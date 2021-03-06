import {Injectable} from '@angular/core';
import {ApiRequestService} from '../../../shared/common/service/api-request.service';
import {AppConfig} from '../../../shared/common/api/app-config';
import {Observable} from 'rxjs';

@Injectable()
export class OfficeService {

    constructor(
        private apiRequest: ApiRequestService,
        private appConfig: AppConfig
    ) {}

    getOffices(): Observable<any> {
        return this.apiRequest.get(this.appConfig.getOffices);
    }

    getOfficesByRole(): Observable<any> {
        return this.apiRequest.get(this.appConfig.getOfficesByRole);
    }

    saveUpdateOffice(office): Observable<any> {
        return this.apiRequest.post(this.appConfig.createUpdateOffice, office);
    }

    deleteOffice(office): Observable<any> {
        return this.apiRequest.put(this.appConfig.deleteOffice, office);
    }

}
