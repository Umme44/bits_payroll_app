import { Route } from '@angular/router';
import { AttendanceTimeSheetComponent } from './attendance-time-sheet.component';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

export const ATTENDANCE_TIME_SHEET_ROUTE: Route = {
  path: 'attendance-time-sheet',
  component: AttendanceTimeSheetComponent,
  data: {
    authorities: [],
    pageTitle: 'bitsHrPayrollApp.ats.home.title',
  },
  canActivate: [UserRouteAccessService],
};
