import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PfLoanComponent } from './list/pf-loan.component';
import { PfLoanDetailComponent } from './detail/pf-loan-detail.component';
import { PfLoanUpdateComponent } from './update/pf-loan-update.component';
import { PfLoanDeleteDialogComponent } from './delete/pf-loan-delete-dialog.component';
import { PfLoanRoutingModule } from './route/pf-loan-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, PfLoanRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [PfLoanComponent, PfLoanDetailComponent, PfLoanUpdateComponent, PfLoanDeleteDialogComponent],
})
export class PfLoanModule {}
