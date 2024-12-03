import { Routes } from '@angular/router';
import { LeaveApprovalSuperordinateComponent } from './leave-approval-superordinate.component';
import { LeaveApprovedByMeComponent } from 'app/attendance-management-system/leave-approval-superordinate/leave-approved-by-me.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const LEAVE_APPROVAL_SUPERORDINATE_ROUTE: Routes = [
  {
    path: 'leave-approval-superordinate',
    component: LeaveApprovalSuperordinateComponent,
    data: {
      authorities: [],
      pageTitle: 'bitsHrPayrollApp.leaveApprovals.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'leave-approval-superordinate/approved-by-me',
    component: LeaveApprovedByMeComponent,
    data: {
      authorities: [],
      pageTitle: 'bitsHrPayrollApp.leaveApprovals.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
