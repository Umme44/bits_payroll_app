import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { USER_PAYSLIP_ROUTES, UserPayslipComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { FestivalBonusPayslipComponent } from 'app/payroll-management-system/user-payslip/festival-bonus/festival-bonus-payslip.component';
import { ArrearSlipComponent } from './arrear-slip/arrear-slip.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(USER_PAYSLIP_ROUTES), BitsHrPayrollHeaderModule],
  declarations: [UserPayslipComponent, FestivalBonusPayslipComponent, ArrearSlipComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppUserPayslipModule {}
