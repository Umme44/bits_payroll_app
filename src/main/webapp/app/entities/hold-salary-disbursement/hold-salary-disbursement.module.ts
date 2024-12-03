import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HoldSalaryDisbursementComponent } from './list/hold-salary-disbursement.component';
import { HoldSalaryDisbursementDetailComponent } from './detail/hold-salary-disbursement-detail.component';
import { HoldSalaryDisbursementUpdateComponent } from './update/hold-salary-disbursement-update.component';
import { HoldSalaryDisbursementDeleteDialogComponent } from './delete/hold-salary-disbursement-delete-dialog.component';
import { HoldSalaryDisbursementRoutingModule } from './route/hold-salary-disbursement-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';

@NgModule({
  imports: [SharedModule, HoldSalaryDisbursementRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [
    HoldSalaryDisbursementComponent,
    HoldSalaryDisbursementDetailComponent,
    HoldSalaryDisbursementUpdateComponent,
    HoldSalaryDisbursementDeleteDialogComponent,
  ],
})
export class HoldSalaryDisbursementModule {}
