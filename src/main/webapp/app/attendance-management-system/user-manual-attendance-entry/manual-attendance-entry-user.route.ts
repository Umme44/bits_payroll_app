import { Routes } from '@angular/router';
import { AttendanceApprovalComponent } from './manual-attendance-approval/attendance-approval.component';
import { ManualAttendanceEntryUpdateComponent } from './manual-attendance-entry-update.component';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const manualAttendanceEntryUserRoute: Routes = [
  {
    path: 'lm',
    component: AttendanceApprovalComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.manualAttendanceEntryApproval.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'hr',
    component: AttendanceApprovalComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.manualAttendanceEntryApproval.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'apply',
    component: ManualAttendanceEntryUpdateComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.manualAttendanceEntryUpdate.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
