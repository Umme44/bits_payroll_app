import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EmploymentCertificateService } from './employment-certificate.service';
import { IEmploymentCertificate } from '../../../shared/legacy/legacy-model/employment-certificate.model';

@Component({
  templateUrl: './employment-certificate-delete-dialog.component.html',
})
export class EmploymentCertificateDeleteDialogComponent {
  employmentCertificate?: IEmploymentCertificate;

  constructor(protected employmentCertificateService: EmploymentCertificateService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employmentCertificateService.delete(id).subscribe(() => {
      // this.eventManager.broadcast('employmentCertificateListModification');
      this.activeModal.close();
    });
  }
}
