import { Routes } from '@angular/router';
import { PfAmountDetailedReportComponent } from './pf-amount-detailed-report.component';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';

export const pfAmountDetailedReportRoute: Routes = [
  {
    path: ':pfCode',
    component: PfAmountDetailedReportComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfCollection.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
