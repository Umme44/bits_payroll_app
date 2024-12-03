import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from '../../../shared/shared.module';
import { EmploymentCertificateDetailComponent } from './employment-certificate-detail.component';
import { EmploymentCertificateDeleteDialogComponent } from './employment-certificate-delete-dialog.component';
import { employmentCertificateRoute } from './employment-certificate.route';
import { BitsHrPayrollHeaderModule } from 'app/layouts/header/header.module';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(employmentCertificateRoute), BitsHrPayrollHeaderModule],
  declarations: [EmploymentCertificateDetailComponent, EmploymentCertificateDeleteDialogComponent],
  entryComponents: [EmploymentCertificateDeleteDialogComponent],
})
export class BitsHrPayrollEmploymentCertificateModule {}
