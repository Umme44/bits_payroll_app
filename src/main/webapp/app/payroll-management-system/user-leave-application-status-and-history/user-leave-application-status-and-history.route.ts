import { Route } from '@angular/router';
import { UserLeaveApplicationStatusAndHistoryComponent } from './user-leave-application-status-and-history.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const USER_LEAVE_APPLICATION_STATUS_AND_HISTORY_ROUTE: Route = {
  path: '',
  component: UserLeaveApplicationStatusAndHistoryComponent,
  data: {
    authorities: [],
    pageTitle: 'bitsHrPayrollApp.userLeaveApplication.home.title',
  },
  canActivate: [UserRouteAccessService],
};
