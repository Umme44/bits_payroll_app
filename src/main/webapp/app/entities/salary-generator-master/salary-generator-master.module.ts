import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SalaryGeneratorMasterComponent } from './list/salary-generator-master.component';
import { SalaryGeneratorMasterDetailComponent } from './detail/salary-generator-master-detail.component';
import { SalaryGeneratorMasterUpdateComponent } from './update/salary-generator-master-update.component';
import { SalaryGeneratorMasterDeleteDialogComponent } from './delete/salary-generator-master-delete-dialog.component';
import { SalaryGeneratorMasterRoutingModule } from './route/salary-generator-master-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollSearchTextBoxModule } from '../../shared/search-text-box/search-text-box.module';
import { SalaryLockComponent } from './salary-lock/salary-lock.component';
import { HoldSalariesApprovalComponent } from './hold-salaries-approval/hold-salaries-approval.component';

@NgModule({
  imports: [SharedModule, SalaryGeneratorMasterRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSearchTextBoxModule],
  declarations: [
    SalaryGeneratorMasterComponent,
    SalaryGeneratorMasterDetailComponent,
    SalaryGeneratorMasterUpdateComponent,
    SalaryGeneratorMasterDeleteDialogComponent,
    SalaryLockComponent,
    HoldSalariesApprovalComponent,
  ],
})
export class SalaryGeneratorMasterModule {}
