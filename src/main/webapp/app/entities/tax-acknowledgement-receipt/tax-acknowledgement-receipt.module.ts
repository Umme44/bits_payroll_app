import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { TaxAcknowledgementReceiptComponent } from './list/tax-acknowledgement-receipt.component';
import { TaxAcknowledgementReceiptUpdateComponent } from './update/tax-acknowledgement-receipt-update.component';
import { TaxAcknowledgementReceiptRoutingModule } from './route/tax-acknowledgement-receipt-routing.module';
import {
  BitsHrPayrollSimpleSelectEmployeeFormModule
} from "../../shared/simple-select-employee/simple-select-employee-form.module";
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {
  TaxAcknowledgementReceiptFinanceApprovalsComponent
} from "./approval/tax-acknowledgement-receipt-finance-approvals.component";

@NgModule({
  imports: [
    SharedModule,
    TaxAcknowledgementReceiptRoutingModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    BitsHrPayrollHeaderModule],
  declarations: [
    TaxAcknowledgementReceiptComponent,
    TaxAcknowledgementReceiptUpdateComponent,
    TaxAcknowledgementReceiptFinanceApprovalsComponent,
  ],
})
export class TaxAcknowledgementReceiptModule {}
