import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { UserTaxAcknowledgementReceiptService } from '../service/user-tax-acknowledgement-receipt.service';
import {ITaxAcknowledgementReceipt} from "../tax-acknowledgement-receipt.model";
import {EventManager} from "../../../core/util/event-manager.service";

@Component({
  templateUrl: './user-tax-acknowledgement-receipt-delete-dialog.component.html',
})
export class UserTaxAcknowledgementReceiptDeleteDialogComponent {
  taxAcknowledgementReceipt?: ITaxAcknowledgementReceipt;

  constructor(
    protected taxAcknowledgementReceiptService: UserTaxAcknowledgementReceiptService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.taxAcknowledgementReceiptService.delete(id).subscribe(() => {
      this.eventManager.broadcast('taxAcknowledgementReceiptListModification');
      this.activeModal.close();
    });
  }
}
