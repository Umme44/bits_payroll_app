import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeComponent } from './list/employee.component';
import { EmployeeDetailComponent } from './detail/employee-detail.component';
import { EmployeeUpdateComponent } from './update/employee-update.component';
import { EmployeeDeleteDialogComponent } from './delete/employee-delete-dialog.component';
import { EmployeeRoutingModule } from './route/employee-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";


@NgModule({
    imports: [SharedModule, EmployeeRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [EmployeeComponent, EmployeeDetailComponent, EmployeeUpdateComponent, EmployeeDeleteDialogComponent],
})
export class EmployeeModule {}
