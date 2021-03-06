import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {SwalComponent} from '@toverux/ngx-sweetalert2';
import {Topic} from '../../../topic/model/topic';
import {ApiErrorDetails} from '../../../../shared/common/api/model/api-error-details';
import {Router} from '@angular/router';
import {TransferDataService} from '../../../../shared/common/service/transfer-data.service';
import {TopicService} from '../../service/topic.service';
import {DataFilter} from '../../../../shared/common/api/model/data-filter';
import {PageEnum} from '../../../../shared/common/api/enum/page.enum';
import {LangChangeEvent, TranslateService} from '@ngx-translate/core';
import {DeviceDetectorService} from 'ngx-device-detector';

@Component({
    selector: 'app-topic-table',
    templateUrl: './topic-table.component.html',
    styleUrls: ['./topic-table.component.css']
})
export class TopicTableComponent implements OnInit {

    @ViewChild('descriptionTopic') descriptionTopic: ElementRef;
    @ViewChild('myTable') table: any;
    @ViewChild('deleteTopicSwal') deleteTopicSwal: SwalComponent;

    langOnChange = '';
    isMobile = false;

    columns: any[];
    rows: Topic[];
    data: any;
    temp = [];
    rowSelected: Topic;
    errorDetails: ApiErrorDetails;
    dataFilter: DataFilter = new DataFilter(PageEnum.BO_TOPIC);

    constructor(
        private router: Router,
        private topicService: TopicService,
        private transferService: TransferDataService,
        private translateService: TranslateService,
        private deviceService: DeviceDetectorService
    ) {
        const me = this;

        me.langOnChange = me.translateService.currentLang;

        me.translateService.onLangChange
            .subscribe((event: LangChangeEvent) => {
                if (event.lang) {
                    me.langOnChange = event.lang;
                    me.descriptionOnChange();
                }
            });

        this.isMobile = this.deviceService.isMobile();
    }

    async ngOnInit() {
        console.log('TopicTableComponent - ngOnInit');

        const me = this;
        me.getTopics();

        me.columns = [
            {prop: 'description', name: 'Description'}
        ];

        const dataFilterTemp: DataFilter = this.transferService.dataFilter;
        if (dataFilterTemp && dataFilterTemp.page === PageEnum.BO_TOPIC) {
            this.dataFilter = dataFilterTemp;
            if (this.dataFilter.description) {
                this.descriptionTopic.nativeElement.value = this.dataFilter.description;
            }
        }
    }

    getTopics() {
        console.log('TopicTableComponent - getTopics');

        const me = this;
        this.topicService.getTopics().subscribe(
            (data) => {
                me.temp = data;
                me.rows = me.descriptionOnChange();

                me.updateDataFilter(this.dataFilter.description);
            }
        );
    }

    descriptionOnChange(): any {
        const me = this;

        if (me.temp && me.temp.length > 0) {

            me.temp.forEach(topic => {
                if (topic.translationList && topic.translationList.length > 1) {
                    topic.translationList.forEach(translation => {
                        if (translation.lang === me.langOnChange) {
                            topic.description = translation.description;
                        }
                    });
                }
            });

            return me.temp;
        } else {
            return;
        }
    }

    updateDataFilter(val) {
        console.log('TopicTableComponent - updateDataFilter');

        if (val) {
            this.filterData(val);
        }
    }

    updateFilter(event) {
        console.log('TopicTableComponent - updateFilter');

        const val = event.target.value.toLowerCase();

        this.filterData(val);
    }

    filterData(val) {
        // filter our data
        const temp = this.temp.filter(function (d) {
            return d.description.toLowerCase().indexOf(val) !== -1 || !val;
        });

        // update the rows
        this.rows = temp;
        // Whenever the filter changes, always go back to the first page
        this.table = this.data;

        this.dataFilter.description = val;
    }

    createNewTopic() {
        console.log('TopicTableComponent - createNewTopic');

        this.transferService.dataFilter = this.dataFilter;

        this.router.navigate(['/back-office/topic/create']);
    }

    editTopic(topic: Topic) {
        console.log('TopicTableComponent - editTopic');

        this.transferService.dataFilter = this.dataFilter;
        this.transferService.objectParam = topic;

        this.router.navigate(['/back-office/topic/edit']);
    }

    deleteTopicAlert(row) {
        console.log('TopicTableComponent - deleteTopicAlert');

        this.rowSelected = row;

        let msgSwal = '';

        this.translateService.get('GENERAL.DELETE_SPACE').subscribe(
            data => {
                msgSwal = data;
            });

        this.deleteTopicSwal.title = msgSwal + row.description + '?';

        this.deleteTopicSwal.show();
    }

    deleteTopicCofirm() {
        console.log('TopicTableComponent - deleteTopicCofirm' + this.rowSelected.idTopic);

        const topicSelected = this.rowSelected;

        this.topicService.deleteTopic(topicSelected).subscribe(
            (data) => {
                this.errorDetails = undefined;
                this.getTopics();
                console.log('TopicTableComponent - deleteTopicCofirm - next');
            }, error => {
                this.errorDetails = error.error;
                console.error('TopicTableComponent - deleteTopicCofirm - error \n', error);
            }
        );
    }

    toggleExpandRow(row) {
        console.log('TopicTableComponent - Toggled Expand Row!', row);
        this.table.rowDetail.toggleExpandRow(row);
    }

    onDetailToggle(event) {
    }

}
