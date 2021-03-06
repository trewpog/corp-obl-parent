import {Component, Input, OnInit} from '@angular/core';
import {Task} from '../../../task/model/task';
import {Router} from '@angular/router';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {TaskService} from '../../../task/service/task.service';
import {TranslateService} from '@ngx-translate/core';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-tasktemplate-table',
    templateUrl: './tasktemplate-table.component.html',
    styleUrls: ['./tasktemplate-table.component.css']
})
export class TaskTemplateTableComponent implements OnInit {

    isMobile = false;

    @Input() taskTemplatesArray = [];

    constructor(
        private router: Router,
        private transferService: TransferDataService,
        private taskService: TaskService,
        private deviceService: DeviceDetectorService
    ) {

        this.isMobile = this.deviceService.isMobile();
    }

    ngOnInit() {
        console.log('TaskTemplateTableComponent - ngOnInit');
    }

    modifyTaskTemplate(taskTemplate) {
        console.log('TaskTemplateTableComponent - modifyTaskTemplate');

        const me = this;
        const task: Task = new Task();
        task.taskTemplate = taskTemplate;
        const tempObject = {
            isNewForm: false,
            task: task,
            office: undefined,
            taskOffice: null
        };

        this.taskService.getSingleTaskByTaskTemplate(taskTemplate).subscribe(
            data => {

                tempObject.task = data;
                me.transferService.objectParam = tempObject;

                this.router.navigate(['/back-office/quick-configuration/edit']);

                console.log('TaskTemplateTableComponent - modifyTaskTemplate - next');
            },
            error => {
                console.error('TaskTemplateTableComponent - modifyTaskTemplate - error \n', error);
            }
        );
    }

    createTask() {

        this.transferService.objectParam = {
            isNewForm: true,
            task: undefined,
            office: undefined
        };

        this.router.navigate(['/back-office/quick-configuration/create']);
    }

}
