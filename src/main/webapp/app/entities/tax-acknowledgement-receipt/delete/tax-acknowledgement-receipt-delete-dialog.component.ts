import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ITaxAcknowledgementReceipt } from '../tax-acknowledgement-receipt.model';
import { TaxAcknowledgementReceiptService } from '../service/tax-acknowledgement-receipt.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './tax-acknowledgement-receipt-delete-dialog.component.html',
})
export class TaxAcknowledgementReceiptDeleteDialogComponent {
  taxAcknowledgementReceipt?: ITaxAcknowledgementReceipt;

  constructor(protected taxAcknowledgementReceiptService: TaxAcknowledgementReceiptService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taxAcknowledgementReceiptService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
