import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmploymentCertificate } from '../employment-certificate.model';
import { EmploymentCertificateService } from '../service/employment-certificate.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employment-certificate-delete-dialog.component.html',
})
export class EmploymentCertificateDeleteDialogComponent {
  employmentCertificate?: IEmploymentCertificate;

  constructor(protected employmentCertificateService: EmploymentCertificateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employmentCertificateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
