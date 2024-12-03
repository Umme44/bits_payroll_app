import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PfLoanApplicationComponent } from '../list/pf-loan-application.component';
import { PfLoanApplicationDetailComponent } from '../detail/pf-loan-application-detail.component';
import { PfLoanApplicationUpdateComponent } from '../update/pf-loan-application-update.component';
import { PfLoanApplicationRoutingResolveService } from './pf-loan-application-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from '../../../config/authority.constants';
import { PfLoanApplicationRejectComponent } from '../reject/pf-loan-application-reject.component';
import { PfLoanApplicationApprovalComponent } from '../approval/pf-loan-application-approval.component';

const pfLoanApplicationRoute: Routes = [
  {
    path: '',
    component: PfLoanApplicationComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PfLoanApplicationDetailComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PfLoanApplicationUpdateComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PfLoanApplicationUpdateComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/approval',
    component: PfLoanApplicationApprovalComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfLoanApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/reject',
    component: PfLoanApplicationRejectComponent,
    resolve: {
      pfLoanApplication: PfLoanApplicationRoutingResolveService,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'bitsHrPayrollApp.pfLoanApplication.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pfLoanApplicationRoute)],
  exports: [RouterModule],
})
export class PfLoanApplicationRoutingModule {}
