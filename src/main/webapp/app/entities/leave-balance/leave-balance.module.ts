import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { LeaveBalanceComponent } from './list/leave-balance.component';
import { LeaveBalanceDetailComponent } from './detail/leave-balance-detail.component';
import { LeaveBalanceUpdateComponent } from './update/leave-balance-update.component';
import { LeaveBalanceDeleteDialogComponent } from './delete/leave-balance-delete-dialog.component';
import { LeaveBalanceRoutingModule } from './route/leave-balance-routing.module';
import {BitsHrPayrollHeaderModule} from "../../layouts/header/header.module";
import {BitsHrPayrollSelectEmployeeFormModule} from "../../shared/select-employee-form/select-employee-form.module";
import {ILeaveBalance} from "./leave-balance.model";
import {LeaveAmountType} from "../enumerations/leave-amount-type.model";
import {LeaveType} from "../enumerations/leave-type.model";
import {IEmployee} from "../employee/employee.model";
import {
    BitsHrPayrollSimpleSelectEmployeeFormModule
} from "../../shared/simple-select-employee/simple-select-employee-form.module";

@NgModule({
    imports: [
      SharedModule,
      LeaveBalanceRoutingModule,
      BitsHrPayrollHeaderModule,
      BitsHrPayrollSelectEmployeeFormModule,
      BitsHrPayrollSimpleSelectEmployeeFormModule
    ],
  declarations: [
    LeaveBalanceComponent,
    LeaveBalanceDetailComponent,
    LeaveBalanceUpdateComponent,
    LeaveBalanceDeleteDialogComponent
  ],
})
export class LeaveBalanceModule {}

export class LeaveBalance implements ILeaveBalance{

  constructor() {

  }

  amount: number | null;
  closingBalance: number | null;
  consumedDuringYear: number | null;
  departmentName: string | null;
  designationName: string | null;
  employee: Pick<IEmployee, "id"> | null;
  employeeId: number | null;
  fullName: string | null;
  id: number;
  leaveAmountType: LeaveAmountType | null;
  leaveType: LeaveType | null;
  openingBalance: number | null;
  pin: string | null;
  year: number | null;




}

/*

import { LeaveType } from 'app/shared/model/enumerations/leave-type.model';
import { LeaveAmountType } from 'app/shared/model/enumerations/leave-amount-type.model';
import {ILeaveBalance} from "./leave-balance.model";

@NgModule({
  imports: [SharedModule, LeaveBalanceRoutingModule, BitsHrPayrollHeaderModule, BitsHrPayrollSelectEmployeeFormModule],
  declarations: [LeaveBalanceComponent, LeaveBalanceDetailComponent, LeaveBalanceUpdateComponent, LeaveBalanceDeleteDialogComponent],
})
export class LeaveBalanceModule {}

export class LeaveBalance implements ILeaveBalance {
  constructor(
    public id?: number,
    public leaveType?: LeaveType,
    public openingBalance?: number,
    public closingBalance?: number,
    public consumedDuringYear?: number,
    public year?: number,
    public amount?: number,
    public leaveAmountType?: LeaveAmountType,
    public employeeId?: number,
    public pin?: string,
    public fullName?: string,
    public designationName?: string,
    public departmentName?: string,
    public unitName?: string
  ) {}
}

*/
