import {Injectable} from '@angular/core';

@Injectable()
export class AppConfig {

    // Provide all the Application Configs here
    public version = '1.0.0';
    public locale = 'en-US';
    public dateFormat = {day: 'numeric', month: 'short', year: 'numeric'};

    // API Related configs
    public apiPort = '8100';
    public apiProtocol: string;
    public apiHostName: string;
    public apiBasePath = 'admin-rest';
    public baseApiPath: string;

    public list = '/list';
    public delete = '/delete';
    public createupdate = '/create-update';

    // User
    public userPath = 'user';
    public userList = this.userPath + this.list;
    public userListExceptRole = this.userPath + '/user-except';

    // Back office
    public backOfficePath = 'back-office';

    // Company URL's
    public companyPath = '/company';
    public getCompanies = this.backOfficePath + this.companyPath + this.list;                       // GET
    public getCompaniesByRole = this.backOfficePath + this.companyPath + this.list + '/by-role';    // GET
    public getCompanyById = this.backOfficePath + this.companyPath + '/{idCompany}';                // GET
    public createUpdateCompany = this.backOfficePath + this.companyPath + this.createupdate;        // POST
    public deleteCompany = this.backOfficePath + this.companyPath + this.delete;                    // PUT
    public assocCompanyUsers = this.backOfficePath + this.companyPath + '/assoc-user-company';      // POST

    // Office URL's
    public officePath = '/office';
    public getOffices = this.backOfficePath + this.officePath + this.list;                      // GET
    public getOfficesByRole = this.backOfficePath + this.officePath + this.list + '/by-role';                      // GET
    public createUpdateOffice = this.backOfficePath + this.officePath + this.createupdate;      // POST
    public deleteOffice = this.backOfficePath + this.officePath + this.delete;                  // PUT

    // Topic URL's
    public topicPath = '/topic';
    public getTopics = this.backOfficePath + this.topicPath + this.list;                        // GET
    public getTopicsByRole = this.backOfficePath + this.topicPath + this.list + '/by-role';     // GET
    public deleteTopic = this.backOfficePath + this.topicPath + this.delete;                    // PUT
    public createUpdateTopic = this.backOfficePath + this.topicPath + this.createupdate;        // POST

    // Company Consultant URL's
    public consultantPath = '/consultant';
    public getConsultants = this.backOfficePath + this.consultantPath + this.list;                          // GET
    public deleteConsultant = this.backOfficePath + this.consultantPath + this.delete;                      // PUT
    public createUpdateConsultant = this.backOfficePath + this.consultantPath + this.createupdate;          // POST

    // Company Topic URL's
    public topicConsutantPath = '/topic-consultant';
    public getTopicConsultant = this.backOfficePath + this.topicConsutantPath + this.list;                           // GET
    public deleteTopicConsultant = this.backOfficePath + this.topicConsutantPath + this.delete;                      // PUT
    public deleteTopicConsultants = this.backOfficePath + this.topicConsutantPath + this.delete + '/all';            // PUT
    public createUpdateTopicConsultant = this.backOfficePath + this.topicConsutantPath + this.createupdate;          // POST

    // Task template URL's
    public taskTemplatePath = '/task-template';
    public getTaskTemplates = this.backOfficePath + this.taskTemplatePath + this.list;                          // GET
    public getTaskTemplatesForTable = this.backOfficePath + this.taskTemplatePath + this.list + '/for-table';   // GET
    public deleteTaskTemplate = this.backOfficePath + this.taskTemplatePath + this.delete;                      // PUT
    public createUpdateTaskTemplate = this.backOfficePath + this.taskTemplatePath + this.createupdate;          // POST
    public searchTaskTemplate = this.backOfficePath + this.taskTemplatePath + '/search-task-template';          // POST

    // Translation URL's
    public translationPath = '/translation';
    public getTranslationsLikeTablename = this.backOfficePath + this.translationPath + '/like-tablename';     // GET

    // File upload URL's
    public uploadFile = this.backOfficePath + '/upload-files';              // POST
    public downloadFile = this.backOfficePath + '/download-files';          // POST
    public removeFile = this.backOfficePath + '/remove-files';              // PUT

    // Task URL's
    public taskPath = '/task';
    public getTasks = this.backOfficePath + this.taskPath + this.list;                                      // GET
    public createUpdateTask = this.backOfficePath + this.taskPath + this.createupdate;                      // POST
    public getTaskByDescrOrCompOrTopic = this.backOfficePath + this.taskPath + '/like-desc-comp-topic';     // GET

    // Office task URL's
    public officeTaskPath = '/office-task';
    public searchOfficeTask = this.backOfficePath + this.officeTaskPath + '/search-office';

    constructor() {
        console.log('AppConfig - constructor');

        if (this.apiProtocol === undefined) {
            this.apiProtocol = window.location.protocol;
        }
        if (this.apiHostName === undefined) {
            this.apiHostName = window.location.hostname;
        }
        if (this.apiPort === undefined) {
            this.apiPort = window.location.port;
        } else {
            this.baseApiPath = this.apiProtocol + '//' + this.apiHostName + ':' + this.apiPort + '/' + this.apiBasePath + '/';
        }
        if (this.locale === undefined) {
            this.locale = navigator.language;
        }
    }
}
