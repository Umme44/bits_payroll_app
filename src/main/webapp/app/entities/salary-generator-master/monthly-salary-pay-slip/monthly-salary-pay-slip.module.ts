import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';


import { MONTHLY_SALARY_PAY_SLIP_ROUTE, MonthlySalaryPaySlipComponent } from './index';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';
import { SharedModule } from '../../../shared/shared.module';

@NgModule({
  imports: [SharedModule, RouterModule.forRoot([MONTHLY_SALARY_PAY_SLIP_ROUTE], { useHash: true }), BitsHrPayrollHeaderModule],
  declarations: [MonthlySalaryPaySlipComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppMonthlySalaryPaySlipModule {}
