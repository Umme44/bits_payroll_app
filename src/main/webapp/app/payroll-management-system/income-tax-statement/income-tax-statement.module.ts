import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../shared/shared.module';

import { INCOME_TAX_STATEMENT_ROUTE, IncomeTaxStatementComponent } from './index';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollAppIncomeTaxReportsModule } from '../income-tax-reports/income-tax-reports.module';

@NgModule({
  imports: [
    SharedModule,
    RouterModule.forChild([INCOME_TAX_STATEMENT_ROUTE]),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollAppIncomeTaxReportsModule,
  ],
  declarations: [IncomeTaxStatementComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppIncomeTaxStatementModule {}
