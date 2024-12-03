import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EmploymentCertificateComponent } from './list/employment-certificate.component';
import { EmploymentCertificateDetailComponent } from './detail/employment-certificate-detail.component';
import { EmploymentCertificateUpdateComponent } from './update/employment-certificate-update.component';
import { EmploymentCertificateDeleteDialogComponent } from './delete/employment-certificate-delete-dialog.component';
import { EmploymentCertificateRoutingModule } from './route/employment-certificate-routing.module';

@NgModule({
  imports: [SharedModule, EmploymentCertificateRoutingModule],
  declarations: [
    EmploymentCertificateComponent,
    EmploymentCertificateDetailComponent,
    EmploymentCertificateUpdateComponent,
    EmploymentCertificateDeleteDialogComponent,
  ],
})
export class EmploymentCertificateModule {}
