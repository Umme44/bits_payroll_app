import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeeResignationComponent } from './list/employee-resignation.component';
import { EmployeeResignationDetailComponent } from './detail/employee-resignation-detail.component';
import { EmployeeResignationUpdateComponent } from './update/employee-resignation-update.component';
import { EmployeeResignationDeleteDialogComponent } from './delete/employee-resignation-delete-dialog.component';
import { EmployeeResignationRoutingModule } from './route/employee-resignation-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {BitsHrPayrollSelectEmployeeFormModule} from "../../shared/select-employee-form/select-employee-form.module";
import {EmployeeResignationRejectComponent} from "./reject/employee-resignation-reject.component";
import {EmployeeResignationApproveComponent} from "./approve/employee-resignation-approve.component";

@NgModule({
  imports: [SharedModule, EmployeeResignationRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSelectEmployeeFormModule],
  declarations: [
    EmployeeResignationComponent,
    EmployeeResignationDetailComponent,
    EmployeeResignationUpdateComponent,
    EmployeeResignationDeleteDialogComponent,
    EmployeeResignationRejectComponent,
    EmployeeResignationApproveComponent
  ],
})
export class EmployeeResignationModule {}
