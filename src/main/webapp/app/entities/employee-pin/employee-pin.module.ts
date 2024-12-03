import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmployeePinComponent } from './list/employee-pin.component';
import { EmployeePinDetailComponent } from './detail/employee-pin-detail.component';
import { EmployeePinUpdateComponent } from './update/employee-pin-update.component';
import { EmployeePinDeleteDialogComponent } from './delete/employee-pin-delete-dialog.component';
import { EmployeePinRoutingModule } from './route/employee-pin-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
  imports: [SharedModule, EmployeePinRoutingModule, BitsHrPayrollHeaderModule, NgSelectModule],
  declarations: [EmployeePinComponent, EmployeePinDetailComponent, EmployeePinUpdateComponent, EmployeePinDeleteDialogComponent],
})
export class EmployeePinModule {}
