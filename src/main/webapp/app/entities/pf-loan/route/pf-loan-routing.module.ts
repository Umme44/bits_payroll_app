import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PfLoanComponent } from '../list/pf-loan.component';
import { PfLoanDetailComponent } from '../detail/pf-loan-detail.component';
import { PfLoanUpdateComponent } from '../update/pf-loan-update.component';
import { PfLoanRoutingResolveService } from './pf-loan-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const pfLoanRoute: Routes = [
  {
    path: '',
    component: PfLoanComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PfLoanDetailComponent,
    resolve: {
      pfLoan: PfLoanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PfLoanUpdateComponent,
    resolve: {
      pfLoan: PfLoanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PfLoanUpdateComponent,
    resolve: {
      pfLoan: PfLoanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pfLoanRoute)],
  exports: [RouterModule],
})
export class PfLoanRoutingModule {}
