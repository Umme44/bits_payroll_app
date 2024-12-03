import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SharedModule } from '../../shared/shared.module';
import { EmployeeDocsComponent } from './employee-docs.component';
import { employeeDOCRoute } from './employee-docs.route';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';
import { BitsHrPayrollEmploymentCertificateModule } from './employment-certificate/employment-certificate.module';

@NgModule({
  imports: [
    SharedModule,
    BitsHrPayrollEmploymentCertificateModule,
    RouterModule.forChild([
      {
        path: 'employee-noc',
        loadChildren: () => import('../employment docs/employee-noc/employee-noc.module').then(m => m.BitsHrPayrollEmployeeNOCModule),
      },
      {
        path: 'employment-certificate',
        loadChildren: () =>
          import('../employment docs/employment-certificate/employment-certificate.module').then(
            m => m.BitsHrPayrollEmploymentCertificateModule
          ),
      },
      {
        path: 'employee-salary-certificate',
        loadChildren: () =>
          import('../employment docs/employee-salary-certificate/employee-salary-certificate.module').then(
            m => m.BitsHrPayrollEmployeeSalaryCertificateModule
          ),
      },
      ...employeeDOCRoute,
    ]),
    BitsHrPayrollHeaderModule,
  ],
  declarations: [EmployeeDocsComponent],
  entryComponents: [EmployeeDocsComponent],
})
export class BitsHrPayrollEmployeeDOCsModule {}
