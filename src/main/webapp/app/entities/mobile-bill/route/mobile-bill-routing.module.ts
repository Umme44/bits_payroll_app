import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MobileBillComponent } from '../list/mobile-bill.component';
import { MobileBillDetailComponent } from '../detail/mobile-bill-detail.component';
import { MobileBillUpdateComponent } from '../update/mobile-bill-update.component';
import { MobileBillRoutingResolveService } from './mobile-bill-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import { Authority } from '../../../config/authority.constants';

const mobileBillRoute: Routes = [
  {
    path: '',
    component: MobileBillComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MobileBillDetailComponent,
    resolve: {
      mobileBill: MobileBillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MobileBillUpdateComponent,
    resolve: {
      mobileBill: MobileBillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MobileBillUpdateComponent,
    resolve: {
      mobileBill: MobileBillRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':year/:month',
    component: MobileBillComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.mobileBill.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':year/:month',
    component: MobileBillComponent,
    data: {
      defaultSort: 'id,asc',
      pageTitle: 'bitsHrPayrollApp.mobileBill.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mobileBillRoute)],
  exports: [RouterModule],
})
export class MobileBillRoutingModule {}
