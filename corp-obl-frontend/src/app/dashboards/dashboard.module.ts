import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';

import {PerfectScrollbarModule} from 'ngx-perfect-scrollbar';

import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {NgxDatatableModule} from '@swimlane/ngx-datatable';
import {CalendarModule} from 'angular-calendar';

import {DashboardRoutes} from './dashboard.routing';

import {DashboardHomeComponent} from './dashboard_home/dashboard-home.component';

@NgModule({
  imports: [
    FormsModule,
    CommonModule,
    NgbModule,
    RouterModule.forChild(DashboardRoutes),
    PerfectScrollbarModule,
    CalendarModule.forRoot(),
    NgxDatatableModule
  ],
  declarations: [
    DashboardHomeComponent,
  ]
})
export class DashboardModule {}
