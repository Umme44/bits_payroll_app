import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { TaxAcknowledgementReceiptComponent } from '../list/tax-acknowledgement-receipt.component';
import { TaxAcknowledgementReceiptUpdateComponent } from '../update/tax-acknowledgement-receipt-update.component';
import { TaxAcknowledgementReceiptRoutingResolveService } from './tax-acknowledgement-receipt-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';
import {
  TaxAcknowledgementReceiptFinanceApprovalsComponent
} from "../approval/tax-acknowledgement-receipt-finance-approvals.component";

const taxAcknowledgementReceiptRoute: Routes = [
  {
    path: 'received',
    component: TaxAcknowledgementReceiptComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'pending',
    component: TaxAcknowledgementReceiptFinanceApprovalsComponent,
    data: {
      pageTitle: 'bitsHrPayrollApp.taxAcknowledgementReceipt.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: ':id/admin-new/:employeeId',
    component: TaxAcknowledgementReceiptUpdateComponent,
    resolve: {
      taxAcknowledgementReceipt: TaxAcknowledgementReceiptRoutingResolveService,
    },
    data: {
      pageTitle: 'bitsHrPayrollApp.taxAcknowledgementReceipt.home.title',
    },
    canActivate: [UserRouteAccessService],
  },

  {
    path: 'admin-new',
    component: TaxAcknowledgementReceiptUpdateComponent,
    resolve: {
      taxAcknowledgementReceipt: TaxAcknowledgementReceiptRoutingResolveService,
    },
    data: {
      pageTitle: 'bitsHrPayrollApp.taxAcknowledgementReceipt.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(taxAcknowledgementReceiptRoute)],
  exports: [RouterModule],
})
export class TaxAcknowledgementReceiptRoutingModule {}
