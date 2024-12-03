import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAitPayment } from '../ait-payment.model';
import { AitPaymentService } from '../service/ait-payment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './ait-payment-delete-dialog.component.html',
})
export class AitPaymentDeleteDialogComponent {
  aitPayment?: IAitPayment;

  constructor(protected aitPaymentService: AitPaymentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.aitPaymentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
