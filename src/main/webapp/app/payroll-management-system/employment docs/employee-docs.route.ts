import { Routes } from '@angular/router';

import { Authority } from '../../../app/config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { EmployeeDocsComponent } from './employee-docs.component';

export const employeeDOCRoute: Routes = [
  {
    path: '',
    component: EmployeeDocsComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.employeeNOC.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
