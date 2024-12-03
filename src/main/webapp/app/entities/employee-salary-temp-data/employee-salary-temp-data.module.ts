import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeSalaryTempDataComponent } from './list/employee-salary-temp-data.component';
import { EmployeeSalaryTempDataDetailComponent } from './detail/employee-salary-temp-data-detail.component';
import { EmployeeSalaryTempDataUpdateComponent } from './update/employee-salary-temp-data-update.component';
import { EmployeeSalaryTempDataDeleteDialogComponent } from './delete/employee-salary-temp-data-delete-dialog.component';
import { EmployeeSalaryTempDataRoutingModule } from './route/employee-salary-temp-data-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeSalaryTempDataRoutingModule],
  declarations: [
    EmployeeSalaryTempDataComponent,
    EmployeeSalaryTempDataDetailComponent,
    EmployeeSalaryTempDataUpdateComponent,
    EmployeeSalaryTempDataDeleteDialogComponent,
  ],
})
export class EmployeeSalaryTempDataModule {}
