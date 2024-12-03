import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfLoanRepayment } from '../pf-loan-repayment.model';
import { PfLoanRepaymentService } from '../service/pf-loan-repayment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pf-loan-repayment-delete-dialog.component.html',
})
export class PfLoanRepaymentDeleteDialogComponent {
  pfLoanRepayment?: IPfLoanRepayment;

  constructor(protected pfLoanRepaymentService: PfLoanRepaymentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pfLoanRepaymentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
