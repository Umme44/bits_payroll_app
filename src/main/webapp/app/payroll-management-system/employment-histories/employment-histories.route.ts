import { Routes } from '@angular/router';

import { EmploymentHistoriesComponent } from './employment-histories.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const employmentHistoriesRoutes: Routes = [
  {
    path: 'employment-histories',
    component: EmploymentHistoriesComponent,
    data: {
      authorities: [],
      pageTitle: 'bitsHrPayrollApp.employmentHistory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'employment-histories/:employeeId',
    component: EmploymentHistoriesComponent,
    data: {
      authorities: [],
      pageTitle: 'bitsHrPayrollApp.employmentHistory.home',
    },
    canActivate: [UserRouteAccessService],
  },
];
