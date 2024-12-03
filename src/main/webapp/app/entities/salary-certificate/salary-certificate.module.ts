import { NgModule } from '@angular/core';
import { NgSelectModule } from '@ng-select/ng-select';
import { SharedModule } from 'app/shared/shared.module';
import { SalaryCertificateComponent } from './list/salary-certificate.component';
import { SalaryCertificateDetailComponent } from './detail/salary-certificate-detail.component';
import { SalaryCertificateUpdateComponent } from './update/salary-certificate-update.component';
import { SalaryCertificateDeleteDialogComponent } from './delete/salary-certificate-delete-dialog.component';
import { SalaryCertificateRoutingModule } from './route/salary-certificate-routing.module';
import { BitsHrPayrollHeaderModule } from '../../layouts/header/header.module';
import { BitsHrPayrollShowStatusModule } from '../../shared/show-status/show-stauts.module';
import { BitsHrPayrollSelectEmployeeFormModule } from '../../shared/select-employee-form/select-employee-form.module';
import {
  BitsHrPayrollSimpleSelectEmployeeFormModule
} from '../../shared/simple-select-employee/simple-select-employee-form.module';
import { BitsHrPayrollSelectMonthModule } from '../../shared/select-month/select-month.module';
import { BitsHrPayrollSelectYearModule } from '../../shared/select-year/select-year.module';
import { SalaryCertificateApprovalComponent } from './approval/salary-certificate-approval.component';
import { SalaryCertificatePrintableComponent } from './approval/salary-certificate-printable.component';

@NgModule({
  imports: [
    SharedModule,
    SalaryCertificateRoutingModule,
    BitsHrPayrollHeaderModule,
    BitsHrPayrollShowStatusModule,
    BitsHrPayrollSelectEmployeeFormModule,
    BitsHrPayrollSimpleSelectEmployeeFormModule,
    BitsHrPayrollSelectMonthModule,
    BitsHrPayrollSelectYearModule,
    NgSelectModule
  ],
  declarations: [
    SalaryCertificateComponent,
    SalaryCertificateDetailComponent,
    SalaryCertificateUpdateComponent,
    SalaryCertificateDeleteDialogComponent,
    SalaryCertificateApprovalComponent,
    SalaryCertificatePrintableComponent
  ]
})
export class SalaryCertificateModule {
}
