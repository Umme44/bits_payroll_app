import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { EmployeeDocsAdminComponent } from './list-view/employee-doc-admin.component';
import { EmployeeNOCDetailAdminComponent } from './employee-noc-admin/details/employee-noc-detail-page-admin.component';
import { EmployeeNOCPrintFormatComponent } from './employee-noc-admin/print-format/employee-noc-print-format.component';
import { employeeDOCAdminRoute } from './employee-doc-admin.route';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';
import { EmploymentCertificateDetailAdminComponent } from 'app/entities/employment-doc-admin/employment-certificate-admin/details/employment-certificate-detail-admin.component';
import { EmploymentCertificatePrintFormatComponent } from 'app/entities/employment-doc-admin/employment-certificate-admin/print-format/employment-certificate-print-format.component';
import { EmployeeSalaryCertificateDetailAdminComponent } from 'app/entities/employment-doc-admin/employee-salary-certificate-admin/details/employee-salary-certificate-detail-page-admin.component';
import { EmployeeSalaryCertificatePrintFormatComponent } from 'app/entities/employment-doc-admin/employee-salary-certificate-admin/print-format/employee-salary-certificate-print-format.component';
import { SharedModule } from '../../shared/shared.module';

@NgModule({
  imports: [SharedModule, NgSelectModule, RouterModule.forChild(employeeDOCAdminRoute), BitsHrPayrollHeaderModule],
  declarations: [
    EmployeeDocsAdminComponent,
    EmployeeNOCDetailAdminComponent,
    EmployeeNOCPrintFormatComponent,
    EmploymentCertificateDetailAdminComponent,
    EmploymentCertificatePrintFormatComponent,
    EmployeeSalaryCertificateDetailAdminComponent,
    EmployeeSalaryCertificatePrintFormatComponent,
  ],
  entryComponents: [EmployeeDocsAdminComponent],
})
export class BitsHrPayrollEmployeeDOCsAdminModule {}
