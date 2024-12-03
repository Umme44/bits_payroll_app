import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HoldFbDisbursementComponent } from '../list/hold-fb-disbursement.component';
import { HoldFbDisbursementDetailComponent } from '../detail/hold-fb-disbursement-detail.component';
import { HoldFbDisbursementUpdateComponent } from '../update/hold-fb-disbursement-update.component';
import { HoldFbDisbursementRoutingResolveService } from './hold-fb-disbursement-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { HoldFestivalBonusListComponent } from '../hold-fb-bonus-list/hold-festival-bonus-list.component';

const holdFbDisbursementRoute: Routes = [
  {
    path: '',
    component: HoldFbDisbursementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HoldFbDisbursementDetailComponent,
    resolve: {
      holdFbDisbursement: HoldFbDisbursementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HoldFbDisbursementUpdateComponent,
    resolve: {
      holdFbDisbursement: HoldFbDisbursementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HoldFbDisbursementUpdateComponent,
    resolve: {
      holdFbDisbursement: HoldFbDisbursementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'hold-list',
    component: HoldFestivalBonusListComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(holdFbDisbursementRoute)],
  exports: [RouterModule],
})
export class HoldFbDisbursementRoutingModule {}
