import { Route } from '@angular/router';
import { LeaveApprovalHrdComponent } from './leave-approval-hrd.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const LEAVE_APPROVAL_HRD_ROUTE: Route = {
  path: 'leave-approval-hrd',
  component: LeaveApprovalHrdComponent,
  data: {
    authorities: [],
    pageTitle: 'bitsHrPayrollApp.leaveApprovals.home.title',
  },
  canActivate: [UserRouteAccessService],
};
