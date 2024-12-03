import { CUSTOM_ELEMENTS_SCHEMA, NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../shared/shared.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { IncomeTaxStatementAdminComponent } from './income-tax-statement-admin.component';
import { INCOME_TAX_STATEMENT_ADMIN_ROUTE } from './income-tax-statement-admin.route';
import { BitsHrPayrollAppIncomeTaxReportsModule } from '../income-tax-reports/income-tax-reports.module';

@NgModule({
  imports: [
    NgSelectModule,
    SharedModule,
    RouterModule.forChild([INCOME_TAX_STATEMENT_ADMIN_ROUTE]),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollAppIncomeTaxReportsModule,
  ],
  declarations: [IncomeTaxStatementAdminComponent],
  entryComponents: [],
  providers: [],
  schemas: [CUSTOM_ELEMENTS_SCHEMA],
})
export class BitsHrPayrollAppIncomeTaxStatementAdminModule {}
