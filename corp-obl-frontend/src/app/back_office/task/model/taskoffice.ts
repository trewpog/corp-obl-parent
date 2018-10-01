import {TaskTemplate} from '../../tasktemplate/model/tasktemplate';
import {Task} from './task';
import {Office} from '../../office/model/office';

export class TaskOffice {

    idTaskOffice: number;
    taskTemplate: TaskTemplate;
    task: Task;
    office: Office;
 //   taskOfficeRelations: TaskOfficeRelation[];
    startDate: Date;
    endDate: Date;
}
