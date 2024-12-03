import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../../shared/shared.module';
import { EmployeeNOCDetailComponent } from './employee-noc-detail.component';
import { EmployeeNOCUpdateComponent } from './employee-noc-update.component';
import { EmployeeNOCDeleteDialogComponent } from './employee-noc-delete-dialog.component';
import { employeeNOCRoute } from './employee-noc.route';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';

@NgModule({
  imports: [SharedModule, NgSelectModule, RouterModule.forChild(employeeNOCRoute), BitsHrPayrollHeaderModule],
  declarations: [EmployeeNOCDetailComponent, EmployeeNOCUpdateComponent, EmployeeNOCDeleteDialogComponent],
  entryComponents: [EmployeeNOCDeleteDialogComponent],
})
export class BitsHrPayrollEmployeeNOCModule {}
