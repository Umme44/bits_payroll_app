import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { employeeIdCardRoute } from './employee-id-card.route';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import {EmployeeIdCardComponent} from "./employee-id-card-details-component";
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(employeeIdCardRoute),
    BitsHrPayrollHeaderModule,
  ],
  declarations: [
    EmployeeIdCardComponent,
  ],
})
export class BitsHrPayrollEmployeeIdCardModule {}
