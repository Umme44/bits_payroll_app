import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

export const myPfNomineeRoute = 'provident-fund-nominee-form';
export const myPfStatementRoute = 'my-provident-fund-statement';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'pf-mgt',
        loadChildren: () => import('./pf-import/pf-import.module').then(m => m.BitsHrPayrollPfImportModule),
      },
      {
        path: 'pf-mgt/pf-export',
        loadChildren: () => import('./pf-export/pf-export.module').then(m => m.BitsHrPayrollPfExportModule),
      },
      {
        path: myPfNomineeRoute,
        loadChildren: () => import('./pf-nominee-form/pf-nominee-form.module').then(m => m.BitsHrPayrollPfNomineeFormModule),
      },
      {
        path: 'pf/pf-loan-application-form',
        loadChildren: () =>
          import('./pf-loan-application-form/pf-loan-application-form.module').then(m => m.BitsHrPayrollPfLoanApplicationFormModule),
      },
      // {
      //   path: myPfStatementRoute,
      //   loadChildren: () => import('./user-pf-statement/user-pf-statement.module').then(m => m.BitsHrPayrollAppUserPfStatementModule),
      // },
      {
        path: 'pf/detailed-pf-amount-report',
        loadChildren: () =>
          import('./pf-account-statement/pf-amount-detailed-report.module').then(m => m.BitsHrPayrollPfAmountDetailedReportModule),
      },
    ]),
  ],
  declarations: [],
})
export class BitsHrPayrollProvidentFundManagementModule {}
