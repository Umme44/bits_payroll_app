import { Route } from '@angular/router';

import { DashboardComponent } from './dashboard.component';
import { Authority } from '../config/authority.constants';
import { UserRouteAccessService } from '../core/auth/user-route-access.service';

export const DASHBOARD_ROUTE: Route = {
  path: '',
  component: DashboardComponent,
  data: {
    authorities: [Authority.ADMIN, Authority.HR_ADMIN, Authority.EMPLOYEE, Authority.USER],
    defaultSort: 'publishFrom,dsc',
    pageTitle: 'home.title',
  },
};
