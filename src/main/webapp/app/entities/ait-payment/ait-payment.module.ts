import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { AitPaymentComponent } from './list/ait-payment.component';
import { AitPaymentDetailComponent } from './detail/ait-payment-detail.component';
import { AitPaymentUpdateComponent } from './update/ait-payment-update.component';
import { AitPaymentDeleteDialogComponent } from './delete/ait-payment-delete-dialog.component';
import { AitPaymentRoutingModule } from './route/ait-payment-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';

@NgModule({
  imports: [SharedModule, AitPaymentRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSelectEmployeeFormModule],
  declarations: [AitPaymentComponent, AitPaymentDetailComponent, AitPaymentUpdateComponent, AitPaymentDeleteDialogComponent],
})
export class AitPaymentModule {}
