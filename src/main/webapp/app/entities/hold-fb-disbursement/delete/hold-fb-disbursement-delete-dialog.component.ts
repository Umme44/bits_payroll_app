import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHoldFbDisbursement } from '../hold-fb-disbursement.model';
import { HoldFbDisbursementService } from '../service/hold-fb-disbursement.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './hold-fb-disbursement-delete-dialog.component.html',
})
export class HoldFbDisbursementDeleteDialogComponent {
  holdFbDisbursement?: IHoldFbDisbursement;

  constructor(protected holdFbDisbursementService: HoldFbDisbursementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.holdFbDisbursementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
