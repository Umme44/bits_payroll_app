import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfLoanApplication } from '../pf-loan-application.model';
import { PfLoanApplicationService } from '../service/pf-loan-application.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pf-loan-application-delete-dialog.component.html',
})
export class PfLoanApplicationDeleteDialogComponent {
  pfLoanApplication?: IPfLoanApplication;

  constructor(protected pfLoanApplicationService: PfLoanApplicationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pfLoanApplicationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
