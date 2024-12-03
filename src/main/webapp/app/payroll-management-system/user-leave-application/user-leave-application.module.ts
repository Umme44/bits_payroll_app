import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { USER_LEAVE_APPLICATION_ROUTE, UserLeaveApplicationComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { UserLeaveApplicationDeleteDialogComponent } from 'app/payroll-management-system/user-leave-application/user-leave-application-delete-dialog.component';
import { BitsHrPayrollSimpleSelectEmployeeFormModule } from 'app/shared/simple-select-employee/simple-select-employee-form.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild(USER_LEAVE_APPLICATION_ROUTE),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
  ],
  declarations: [UserLeaveApplicationComponent, UserLeaveApplicationDeleteDialogComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppUserLeaveApplicationModule {}
