import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { AitPaymentComponent } from '../list/ait-payment.component';
import { AitPaymentDetailComponent } from '../detail/ait-payment-detail.component';
import { AitPaymentUpdateComponent } from '../update/ait-payment-update.component';
import { AitPaymentRoutingResolveService } from './ait-payment-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const aitPaymentRoute: Routes = [
  {
    path: '',
    component: AitPaymentComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AitPaymentDetailComponent,
    resolve: {
      aitPayment: AitPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AitPaymentUpdateComponent,
    resolve: {
      aitPayment: AitPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AitPaymentUpdateComponent,
    resolve: {
      aitPayment: AitPaymentRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(aitPaymentRoute)],
  exports: [RouterModule],
})
export class AitPaymentRoutingModule {}
