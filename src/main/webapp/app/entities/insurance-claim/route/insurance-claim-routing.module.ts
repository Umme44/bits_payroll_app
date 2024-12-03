import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InsuranceClaimComponent } from '../list/insurance-claim.component';
import { InsuranceClaimDetailComponent } from '../detail/insurance-claim-detail.component';
import { InsuranceClaimUpdateComponent } from '../update/insurance-claim-update.component';
import { InsuranceClaimRoutingResolveService } from './insurance-claim-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const insuranceClaimRoute: Routes = [
  {
    path: '',
    component: InsuranceClaimComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InsuranceClaimDetailComponent,
    resolve: {
      insuranceClaim: InsuranceClaimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InsuranceClaimUpdateComponent,
    resolve: {
      insuranceClaim: InsuranceClaimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InsuranceClaimUpdateComponent,
    resolve: {
      insuranceClaim: InsuranceClaimRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(insuranceClaimRoute)],
  exports: [RouterModule],
})
export class InsuranceClaimRoutingModule {}
