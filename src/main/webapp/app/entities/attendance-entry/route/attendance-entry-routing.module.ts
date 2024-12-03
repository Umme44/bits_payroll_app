import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AttendanceEntryComponent } from '../list/attendance-entry.component';
import { AttendanceEntryDetailComponent } from '../detail/attendance-entry-detail.component';
import { AttendanceEntryUpdateComponent } from '../update/attendance-entry-update.component';
import { AttendanceEntryRoutingResolveService } from './attendance-entry-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const attendanceEntryRoute: Routes = [
  {
    path: '',
    component: AttendanceEntryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AttendanceEntryDetailComponent,
    resolve: {
      attendanceEntry: AttendanceEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AttendanceEntryUpdateComponent,
    resolve: {
      attendanceEntry: AttendanceEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AttendanceEntryUpdateComponent,
    resolve: {
      attendanceEntry: AttendanceEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(attendanceEntryRoute)],
  exports: [RouterModule],
})
export class AttendanceEntryRoutingModule {}
