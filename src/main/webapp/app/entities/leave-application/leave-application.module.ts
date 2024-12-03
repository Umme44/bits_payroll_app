import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaveApplicationComponent } from './list/leave-application.component';
import { LeaveApplicationDetailComponent } from './detail/leave-application-detail.component';
import { LeaveApplicationUpdateComponent } from './update/leave-application-update.component';
import { LeaveApplicationDeleteDialogComponent } from './delete/leave-application-delete-dialog.component';
import { LeaveApplicationRoutingModule } from './route/leave-application-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {BitsHrPayrollSelectEmployeeFormModule} from "../../shared/select-employee-form/select-employee-form.module";
import {
    BitsHrPayrollSimpleSelectEmployeeFormModule
} from "../../shared/simple-select-employee/simple-select-employee-form.module";

@NgModule({
    imports: [
      SharedModule,
      LeaveApplicationRoutingModule,
      BitsHrPayrollHeaderModule,
      BitsHrPayrollSelectEmployeeFormModule,
      BitsHrPayrollSimpleSelectEmployeeFormModule
    ],
  declarations: [
    LeaveApplicationComponent,
    LeaveApplicationDetailComponent,
    LeaveApplicationUpdateComponent,
    LeaveApplicationDeleteDialogComponent,
  ],
})
export class LeaveApplicationModule {}
