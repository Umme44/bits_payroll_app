import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PfLoanRepaymentComponent } from '../list/pf-loan-repayment.component';
import { PfLoanRepaymentDetailComponent } from '../detail/pf-loan-repayment-detail.component';
import { PfLoanRepaymentUpdateComponent } from '../update/pf-loan-repayment-update.component';
import { PfLoanRepaymentRoutingResolveService } from './pf-loan-repayment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pfLoanRepaymentRoute: Routes = [
  {
    path: '',
    component: PfLoanRepaymentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PfLoanRepaymentDetailComponent,
    resolve: {
      pfLoanRepayment: PfLoanRepaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PfLoanRepaymentUpdateComponent,
    resolve: {
      pfLoanRepayment: PfLoanRepaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PfLoanRepaymentUpdateComponent,
    resolve: {
      pfLoanRepayment: PfLoanRepaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pfLoanRepaymentRoute)],
  exports: [RouterModule],
})
export class PfLoanRepaymentRoutingModule {}
