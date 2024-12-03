import { Route } from '@angular/router';

import { UserEditAccountInformationComponent } from './user-edit-account-information.component';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const USER_EDIT_ACCOUNT_INFORMATION_ROUTE: Route = {
  path: 'user-edit-account-information',
  component: UserEditAccountInformationComponent,
  data: {
    authorities: [],
    pageTitle: 'bitsHrPayrollApp.editProfile.home.title',
  },
  canActivate: [UserRouteAccessService],
};
