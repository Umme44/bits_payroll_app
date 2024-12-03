import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { UserTaxAcknowledgementReceiptComponent } from './list/user-tax-acknowledgement-receipt.component';
import { UserTaxAcknowledgementReceiptDetailComponent } from './detail/user-tax-acknowledgement-receipt-detail.component';
import { UserTaxAcknowledgementReceiptUpdateComponent } from './update/user-tax-acknowledgement-receipt-update.component';
import { UserTaxAcknowledgementReceiptDeleteDialogComponent } from './delete/user-tax-acknowledgement-receipt-delete-dialog.component';
import { userTaxAcknowledgementReceiptRoute } from './route/user-tax-acknowledgement-receipt.route';
import { UserTaxAcknowledgementReceiptDetailModalComponent } from './detail/user-tax-acknowledgement-receipt-detail-modal.component';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {SharedModule} from "../../shared/shared.module";

@NgModule({
  imports: [RouterModule.forChild(userTaxAcknowledgementReceiptRoute), BitsHrPayrollHeaderModule, BitsHrPayrollHeaderModule, SharedModule],
  declarations: [
    UserTaxAcknowledgementReceiptComponent,
    UserTaxAcknowledgementReceiptDetailComponent,
    UserTaxAcknowledgementReceiptUpdateComponent,
    UserTaxAcknowledgementReceiptDeleteDialogComponent,
    UserTaxAcknowledgementReceiptDetailModalComponent,
  ],
  entryComponents: [UserTaxAcknowledgementReceiptDeleteDialogComponent],
})
export class BitsHrPayrollTaxAcknowledgementReceiptModule {}
