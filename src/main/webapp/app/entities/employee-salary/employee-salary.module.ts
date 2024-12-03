import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeSalaryComponent } from './list/employee-salary.component';
import { EmployeeSalaryDetailComponent } from './detail/employee-salary-detail.component';
import { EmployeeSalaryUpdateComponent } from './update/employee-salary-update.component';
import { EmployeeSalaryDeleteDialogComponent } from './delete/employee-salary-delete-dialog.component';
import { EmployeeSalaryRoutingModule } from './route/employee-salary-routing.module';
import {TaxCalculationComponent} from "./tax-calculation/tax-calculation.component";
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import { DatePipe } from '@angular/common';

@NgModule({
  imports: [SharedModule, EmployeeSalaryRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    EmployeeSalaryComponent,
    EmployeeSalaryDetailComponent,
    EmployeeSalaryUpdateComponent,
    EmployeeSalaryDeleteDialogComponent,
    TaxCalculationComponent
  ],
  providers: [DatePipe],
})
export class EmployeeSalaryModule {}
