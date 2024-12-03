import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { pfLoanApplicationFormRoute } from './pf-loan-application-form.route';
import { PfLoanApplicationFormUpdateComponent } from './pf-loan-application-form-update.component';
import { PfLoanApplicationFormComponent } from './pf-loan-application-form.component';
import { PfLoanApplicationFormDeleteDialogComponent } from './pf-loan-application-form-delete-dialog.component';
import { PfLoanApplicationFormDetailComponent } from './pf-loan-application-form-details.component';
import { PfLoanApplicationApprovedFormComponent } from './pf-loan-application-approved-form.component';

@NgModule({
  imports: [SharedModule, BitsHrPayrollHeaderModule, RouterModule.forChild(pfLoanApplicationFormRoute)],
  declarations: [
    PfLoanApplicationFormComponent,
    PfLoanApplicationFormDetailComponent,
    PfLoanApplicationFormUpdateComponent,
    PfLoanApplicationFormDeleteDialogComponent,
    PfLoanApplicationApprovedFormComponent,
  ],
})
export class BitsHrPayrollPfLoanApplicationFormModule {}
