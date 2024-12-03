import { Route } from '@angular/router';
import { MonthlyAttendanceTimeSheetComponent } from './monthly-attendance-time-sheet.component';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

export const MONTHLY_ATTENDANCE_TIME_SHEET_ROUTE: Route = {
  path: 'monthly-attendance-time-sheet',
  component: MonthlyAttendanceTimeSheetComponent,
  data: {
    authorities: [],
    pageTitle: 'attendance-time-sheet.title',
  },
  canActivate: [UserRouteAccessService],
};
