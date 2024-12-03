import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { MONTHLY_SALARY_PAY_SLIP_ROUTE, MonthlySalaryPaySlipComponent } from './index';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([MONTHLY_SALARY_PAY_SLIP_ROUTE]), BitsHrPayrollHeaderModule],
  declarations: [MonthlySalaryPaySlipComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppMonthlySalaryPaySlipModule {}
