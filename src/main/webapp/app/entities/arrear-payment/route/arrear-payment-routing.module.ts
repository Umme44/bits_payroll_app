import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ArrearPaymentComponent } from '../list/arrear-payment.component';
import { ArrearPaymentDetailComponent } from '../detail/arrear-payment-detail.component';
import { ArrearPaymentUpdateComponent } from '../update/arrear-payment-update.component';
import { ArrearPaymentRoutingResolveService } from './arrear-payment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const arrearPaymentRoute: Routes = [
  {
    path: '',
    component: ArrearPaymentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ArrearPaymentDetailComponent,
    resolve: {
      arrearPayment: ArrearPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ArrearPaymentUpdateComponent,
    resolve: {
      arrearPayment: ArrearPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ArrearPaymentUpdateComponent,
    resolve: {
      arrearPayment: ArrearPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(arrearPaymentRoute)],
  exports: [RouterModule],
})
export class ArrearPaymentRoutingModule {}
