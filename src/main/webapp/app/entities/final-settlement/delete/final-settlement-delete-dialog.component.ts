import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IFinalSettlement } from '../final-settlement.model';
import { FinalSettlementService } from '../service/final-settlement.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './final-settlement-delete-dialog.component.html',
})
export class FinalSettlementDeleteDialogComponent {
  finalSettlement?: IFinalSettlement;

  constructor(protected finalSettlementService: FinalSettlementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.finalSettlementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
