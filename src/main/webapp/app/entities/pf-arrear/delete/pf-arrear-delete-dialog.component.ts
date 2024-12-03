import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfArrear } from '../pf-arrear.model';
import { PfArrearService } from '../service/pf-arrear.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pf-arrear-delete-dialog.component.html',
})
export class PfArrearDeleteDialogComponent {
  pfArrear?: IPfArrear;

  constructor(protected pfArrearService: PfArrearService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pfArrearService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
