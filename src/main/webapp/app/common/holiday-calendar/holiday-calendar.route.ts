import { Route } from '@angular/router';
import { HolidayCalendarComponent } from './holiday-calendar.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const HOLIDAY_CALENDAR_ROUTE: Route = {
  path: '',
  component: HolidayCalendarComponent,
  data: {
    authorities: [],
    pageTitle: 'bitsHrPayrollApp.holidayCalendar.home.title',
  },
  canActivate: [UserRouteAccessService],
};
