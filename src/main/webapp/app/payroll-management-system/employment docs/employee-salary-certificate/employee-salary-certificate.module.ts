import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from '../../../shared/shared.module';
import { EmployeeSalaryCertificateDetailComponent } from './employee-salary-certificate-detail.component';
import { EmployeeSalaryCertificateUpdateComponent } from './employee-salary-certificate-update.component';
import { EmployeeSalaryCertificateDeleteDialogComponent } from './employee-salary-certificate-delete-dialog.component';
import { employeeSalaryCertificateRoute } from './employee-salary-certificate.route';
import { BitsHrPayrollHeaderModule } from '../../../layouts/header/header.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../../shared/select-employee-form/select-employee-form.module';

@NgModule({
  imports: [
    NgSelectModule,
    RouterModule.forChild(employeeSalaryCertificateRoute),
    BitsHrPayrollHeaderModule,
    BitsHrPayrollSelectEmployeeFormModule,
    SharedModule,
  ],
  declarations: [
    EmployeeSalaryCertificateDetailComponent,
    EmployeeSalaryCertificateUpdateComponent,
    EmployeeSalaryCertificateDeleteDialogComponent,
  ],
  entryComponents: [EmployeeSalaryCertificateDeleteDialogComponent],
})
export class BitsHrPayrollEmployeeSalaryCertificateModule {}
