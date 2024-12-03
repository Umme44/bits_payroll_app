import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SalaryDeductionComponent } from './list/salary-deduction.component';
import { SalaryDeductionDetailComponent } from './detail/salary-deduction-detail.component';
import { SalaryDeductionUpdateComponent } from './update/salary-deduction-update.component';
import { SalaryDeductionDeleteDialogComponent } from './delete/salary-deduction-delete-dialog.component';
import { SalaryDeductionRoutingModule } from './route/salary-deduction-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, SalaryDeductionRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    SalaryDeductionComponent,
    SalaryDeductionDetailComponent,
    SalaryDeductionUpdateComponent,
    SalaryDeductionDeleteDialogComponent,
  ],
})
export class SalaryDeductionModule {}
