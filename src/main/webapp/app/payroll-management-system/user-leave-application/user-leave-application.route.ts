import { Routes } from '@angular/router';
import { UserLeaveApplicationComponent } from './user-leave-application.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const USER_LEAVE_APPLICATION_ROUTE: Routes = [
  {
    path: 'user-leave-application',
    component: UserLeaveApplicationComponent,
    data: {
      authorities: [],
      pageTitle: 'bitsHrPayrollApp.userLeaveApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'user-leave-application/:id/edit',
    component: UserLeaveApplicationComponent,
    data: {
      authorities: [],
      pageTitle: 'bitsHrPayrollApp.userLeaveApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
