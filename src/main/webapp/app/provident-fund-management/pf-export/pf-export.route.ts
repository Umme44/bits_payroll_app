import { Routes } from '@angular/router';
import { PfExportComponent } from './pf-export.component';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const pfExportRoute: Routes = [
  {
    path: '',
    component: PfExportComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfCollection.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
