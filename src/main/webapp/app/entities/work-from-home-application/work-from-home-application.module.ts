import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { WorkFromHomeApplicationComponent } from './list/work-from-home-application.component';
import { WorkFromHomeApplicationDetailComponent } from './detail/work-from-home-application-detail.component';
import { WorkFromHomeApplicationUpdateComponent } from './update/work-from-home-application-update.component';
import { WorkFromHomeApplicationDeleteDialogComponent } from './delete/work-from-home-application-delete-dialog.component';
import { WorkFromHomeApplicationRoutingModule } from './route/work-from-home-application-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {
  BitsHrPayrollSimpleSelectEmployeeFormModule
} from "../../shared/simple-select-employee/simple-select-employee-form.module";
import {BitsHrPayrollSelectEmployeeFormModule} from "../../shared/select-employee-form/select-employee-form.module";

@NgModule({
  imports: [SharedModule, WorkFromHomeApplicationRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSimpleSelectEmployeeFormModule, BitsHrPayrollSelectEmployeeFormModule],
  declarations: [
    WorkFromHomeApplicationComponent,
    WorkFromHomeApplicationDetailComponent,
    WorkFromHomeApplicationUpdateComponent,
    WorkFromHomeApplicationDeleteDialogComponent,
  ],
})
export class WorkFromHomeApplicationModule {}
