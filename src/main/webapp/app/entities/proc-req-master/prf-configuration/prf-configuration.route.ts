import { Routes } from '@angular/router';

import { PrfConfigurationUpdateComponent } from './prf-configuration-update.component';
import { Authority } from '../../../config/authority.constants';
import { UserRouteAccessService } from '../../../core/auth/user-route-access.service';

export const prfConfigurationRoute: Routes = [
  {
    path: '',
    component: PrfConfigurationUpdateComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.config.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
