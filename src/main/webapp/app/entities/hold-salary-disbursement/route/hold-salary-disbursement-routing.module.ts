import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HoldSalaryDisbursementComponent } from '../list/hold-salary-disbursement.component';
import { HoldSalaryDisbursementDetailComponent } from '../detail/hold-salary-disbursement-detail.component';
import { HoldSalaryDisbursementUpdateComponent } from '../update/hold-salary-disbursement-update.component';
import { HoldSalaryDisbursementRoutingResolveService } from './hold-salary-disbursement-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const holdSalaryDisbursementRoute: Routes = [
  {
    path: '',
    component: HoldSalaryDisbursementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HoldSalaryDisbursementDetailComponent,
    resolve: {
      holdSalaryDisbursement: HoldSalaryDisbursementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HoldSalaryDisbursementUpdateComponent,
    resolve: {
      holdSalaryDisbursement: HoldSalaryDisbursementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HoldSalaryDisbursementUpdateComponent,
    resolve: {
      holdSalaryDisbursement: HoldSalaryDisbursementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(holdSalaryDisbursementRoute)],
  exports: [RouterModule],
})
export class HoldSalaryDisbursementRoutingModule {}
