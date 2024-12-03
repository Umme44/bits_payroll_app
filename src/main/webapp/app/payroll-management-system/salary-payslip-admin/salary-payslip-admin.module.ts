import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { SALARY_PAYSLIP_ADMIN_ROUTES } from './salary-payslip-admin.route';
import { SalaryPayslipAdminComponent } from './salary-payslip-admin.component';

@NgModule({
  imports: [NgSelectModule, SharedModule, RouterModule.forChild(SALARY_PAYSLIP_ADMIN_ROUTES), BitsHrPayrollHeaderModule],
  declarations: [SalaryPayslipAdminComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppSalaryPayslipAdminModule {}
