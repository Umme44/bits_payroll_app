import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManualAttendanceEntryComponent } from '../list/manual-attendance-entry.component';
import { ManualAttendanceEntryDetailComponent } from '../detail/manual-attendance-entry-detail.component';
import { ManualAttendanceEntryUpdateComponent } from '../update/manual-attendance-entry-update.component';
import { ManualAttendanceEntryRoutingResolveService } from './manual-attendance-entry-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const manualAttendanceEntryRoute: Routes = [
  {
    path: '',
    component: ManualAttendanceEntryComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManualAttendanceEntryDetailComponent,
    resolve: {
      manualAttendanceEntry: ManualAttendanceEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManualAttendanceEntryUpdateComponent,
    resolve: {
      manualAttendanceEntry: ManualAttendanceEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManualAttendanceEntryUpdateComponent,
    resolve: {
      manualAttendanceEntry: ManualAttendanceEntryRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(manualAttendanceEntryRoute)],
  exports: [RouterModule],
})
export class ManualAttendanceEntryRoutingModule {}
