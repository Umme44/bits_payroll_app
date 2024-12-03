import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArrearPayment } from '../arrear-payment.model';
import { ArrearPaymentService } from '../service/arrear-payment.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './arrear-payment-delete-dialog.component.html',
})
export class ArrearPaymentDeleteDialogComponent {
  arrearPayment?: IArrearPayment;

  constructor(protected arrearPaymentService: ArrearPaymentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arrearPaymentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
