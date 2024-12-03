import { Route } from '@angular/router';
import { OnboardComponent } from './onboard.component';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

export const ONBOARD_ROUTE: Route = {
  path: 'onboard',
  component: OnboardComponent,
  data: {
    authorities: [],
    pageTitle: 'onboard.title',
  },
  canActivate: [UserRouteAccessService],
};
