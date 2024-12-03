import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PfLoanRepaymentComponent } from './list/pf-loan-repayment.component';
import { PfLoanRepaymentDetailComponent } from './detail/pf-loan-repayment-detail.component';
import { PfLoanRepaymentUpdateComponent } from './update/pf-loan-repayment-update.component';
import { PfLoanRepaymentDeleteDialogComponent } from './delete/pf-loan-repayment-delete-dialog.component';
import { PfLoanRepaymentRoutingModule } from './route/pf-loan-repayment-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { MonthlyPfLoanRepaymentComponent } from './monthly-repayment/monthly-pf-loan-repayment.component';

@NgModule({
  imports: [SharedModule, PfLoanRepaymentRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    PfLoanRepaymentComponent,
    PfLoanRepaymentDetailComponent,
    PfLoanRepaymentUpdateComponent,
    PfLoanRepaymentDeleteDialogComponent,
    MonthlyPfLoanRepaymentComponent
  ],
})
export class PfLoanRepaymentModule {}
