import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PfLoanApplicationComponent } from './list/pf-loan-application.component';
import { PfLoanApplicationDetailComponent } from './detail/pf-loan-application-detail.component';
import { PfLoanApplicationUpdateComponent } from './update/pf-loan-application-update.component';
import { PfLoanApplicationDeleteDialogComponent } from './delete/pf-loan-application-delete-dialog.component';
import { PfLoanApplicationRoutingModule } from './route/pf-loan-application-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import { NgSelectModule } from '@ng-select/ng-select';
import { PfLoanApplicationRejectComponent } from './reject/pf-loan-application-reject.component';
import { PfLoanApplicationApprovalComponent } from './approval/pf-loan-application-approval.component';

@NgModule({
    imports: [SharedModule, PfLoanApplicationRoutingModule, BitsHrPayrollHeaderModule, NgSelectModule],
  declarations: [
    PfLoanApplicationComponent,
    PfLoanApplicationDetailComponent,
    PfLoanApplicationUpdateComponent,
    PfLoanApplicationDeleteDialogComponent,
    PfLoanApplicationRejectComponent,
    PfLoanApplicationApprovalComponent
  ],
})
export class PfLoanApplicationModule {}
