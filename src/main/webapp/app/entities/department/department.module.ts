import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DepartmentComponent } from './list/department.component';
import { DepartmentDetailComponent } from './detail/department-detail.component';
import { DepartmentUpdateComponent } from './update/department-update.component';
import { DepartmentDeleteDialogComponent } from './delete/department-delete-dialog.component';
import { DepartmentRoutingModule } from './route/department-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from '../../shared/simple-select-employee/simple-select-employee-form.module';

@NgModule({
  imports: [SharedModule, NgSelectModule, DepartmentRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSimpleSelectEmployeeFormModule],
  declarations: [DepartmentComponent, DepartmentDetailComponent, DepartmentUpdateComponent, DepartmentDeleteDialogComponent],
})
export class DepartmentModule {}
