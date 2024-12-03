import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalaryCertificate } from '../salary-certificate.model';
import { SalaryCertificateService } from '../service/salary-certificate.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './salary-certificate-delete-dialog.component.html',
})
export class SalaryCertificateDeleteDialogComponent {
  salaryCertificate?: ISalaryCertificate;

  constructor(protected salaryCertificateService: SalaryCertificateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salaryCertificateService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
