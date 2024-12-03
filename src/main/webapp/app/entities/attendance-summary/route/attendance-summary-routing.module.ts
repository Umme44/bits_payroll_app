import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttendanceSummaryComponent } from '../list/attendance-summary.component';
import { AttendanceSummaryDetailComponent } from '../detail/attendance-summary-detail.component';
import { AttendanceSummaryUpdateComponent } from '../update/attendance-summary-update.component';
import { AttendanceSummaryRoutingResolveService } from './attendance-summary-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from '../../../config/authority.constants';

const attendanceSummaryRoute: Routes = [
  {
    path: '',
    component: AttendanceSummaryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttendanceSummaryDetailComponent,
    resolve: {
      attendanceSummary: AttendanceSummaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttendanceSummaryUpdateComponent,
    resolve: {
      attendanceSummary: AttendanceSummaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttendanceSummaryUpdateComponent,
    resolve: {
      attendanceSummary: AttendanceSummaryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
/*  {
    path: ':year/:month',
    component: AttendanceSummaryComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.attendanceSummary.home.title',
    },
    canActivate: [UserRouteAccessService],
  },*/
  {
    path: ':year/:month',
    component: AttendanceSummaryComponent,
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.attendanceSummary.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attendanceSummaryRoute)],
  exports: [RouterModule],
})
export class AttendanceSummaryRoutingModule {}
