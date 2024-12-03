import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IMobileBill } from '../mobile-bill.model';
import { MobileBillService } from '../service/mobile-bill.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './mobile-bill-delete-dialog.component.html',
})
export class MobileBillDeleteDialogComponent {
  mobileBill?: IMobileBill;

  constructor(protected mobileBillService: MobileBillService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.mobileBillService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
