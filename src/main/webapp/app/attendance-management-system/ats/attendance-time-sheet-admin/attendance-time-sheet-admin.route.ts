import { Route } from '@angular/router';
import { AttendanceTimeSheetAdminComponent } from './attendance-time-sheet-admin.component';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

export const ATTENDANCE_TIME_SHEET_ROUTE: Route = {
  path: 'attendance-time-sheet-admin',
  component: AttendanceTimeSheetAdminComponent,
  data: {
    authorities: [],
    // pageTitle: 'attendance-time-sheet.titleAdmin',
  },
  canActivate: [UserRouteAccessService],
};
