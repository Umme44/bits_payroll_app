import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeePinConfigurationComponent } from './list/employee-pin-configuration.component';
import { EmployeePinConfigurationDetailComponent } from './detail/employee-pin-configuration-detail.component';
import { EmployeePinConfigurationUpdateComponent } from './update/employee-pin-configuration-update.component';
import { EmployeePinConfigurationDeleteDialogComponent } from './delete/employee-pin-configuration-delete-dialog.component';
import { EmployeePinConfigurationRoutingModule } from './route/employee-pin-configuration-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";

@NgModule({
    imports: [SharedModule, EmployeePinConfigurationRoutingModule, BitsHrPayrollHeaderModule],
  declarations: [
    EmployeePinConfigurationComponent,
    EmployeePinConfigurationDetailComponent,
    EmployeePinConfigurationUpdateComponent,
    EmployeePinConfigurationDeleteDialogComponent,
  ],
})
export class EmployeePinConfigurationModule {}
