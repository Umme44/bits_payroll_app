import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ArrearPaymentComponent } from './list/arrear-payment.component';
import { ArrearPaymentDetailComponent } from './detail/arrear-payment-detail.component';
import { ArrearPaymentUpdateComponent } from './update/arrear-payment-update.component';
import { ArrearPaymentDeleteDialogComponent } from './delete/arrear-payment-delete-dialog.component';
import { ArrearPaymentRoutingModule } from './route/arrear-payment-routing.module';

@NgModule({
  imports: [SharedModule, ArrearPaymentRoutingModule],
  declarations: [ArrearPaymentComponent, ArrearPaymentDetailComponent, ArrearPaymentUpdateComponent, ArrearPaymentDeleteDialogComponent],
})
export class ArrearPaymentModule {}
