import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfLoan } from '../pf-loan.model';
import { PfLoanService } from '../service/pf-loan.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pf-loan-delete-dialog.component.html',
})
export class PfLoanDeleteDialogComponent {
  pfLoan?: IPfLoan;

  constructor(protected pfLoanService: PfLoanService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pfLoanService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
