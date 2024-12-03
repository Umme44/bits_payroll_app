import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IncomeTaxChallanComponent } from '../list/income-tax-challan.component';
import { IncomeTaxChallanDetailComponent } from '../detail/income-tax-challan-detail.component';
import { IncomeTaxChallanUpdateComponent } from '../update/income-tax-challan-update.component';
import { IncomeTaxChallanRoutingResolveService } from './income-tax-challan-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const incomeTaxChallanRoute: Routes = [
  {
    path: '',
    component: IncomeTaxChallanComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IncomeTaxChallanDetailComponent,
    resolve: {
      incomeTaxChallan: IncomeTaxChallanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IncomeTaxChallanUpdateComponent,
    resolve: {
      incomeTaxChallan: IncomeTaxChallanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IncomeTaxChallanUpdateComponent,
    resolve: {
      incomeTaxChallan: IncomeTaxChallanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(incomeTaxChallanRoute)],
  exports: [RouterModule],
})
export class IncomeTaxChallanRoutingModule {}
