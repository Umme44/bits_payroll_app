import { Routes } from '@angular/router';
import { Authority } from 'app/config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { PfImportComponent } from './pf-import.component';

export const pfImportRoute: Routes = [
  {
    path: 'pf-import',
    component: PfImportComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.config.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
