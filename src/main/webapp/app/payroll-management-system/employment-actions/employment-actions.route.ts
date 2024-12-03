import { Route } from '@angular/router';
import { EmploymentActionsComponent } from './employment-actions.component';
import { ONBOARD_ROUTE } from './onboard/onboard.route';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const EMPLOYMENT_ACTIONS_ROUTE: Route = {
  path: 'employment-actions',
  component: EmploymentActionsComponent,
  data: {
    authorities: [],
    pageTitle: 'employment-actions.title',
  },
  canActivate: [UserRouteAccessService],
  children: [ONBOARD_ROUTE],
};
