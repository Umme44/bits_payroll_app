import { Route } from '@angular/router';
import { UserAttendanceEntryComponent } from './user-attendance-entry.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const USER_ATTENDANCE_ENTRY_ROUTE: Route = {
  path: 'user-attendance-entry',
  component: UserAttendanceEntryComponent,
  data: {
    authorities: [],
    pageTitle: 'user-attendance-entry.title',
  },
  canActivate: [UserRouteAccessService],
};
