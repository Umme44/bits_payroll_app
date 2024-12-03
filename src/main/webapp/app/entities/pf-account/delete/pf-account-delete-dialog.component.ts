import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfAccount } from '../pf-account.model';
import { PfAccountService } from '../service/pf-account.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './pf-account-delete-dialog.component.html',
})
export class PfAccountDeleteDialogComponent {
  pfAccount?: IPfAccount;

  constructor(protected pfAccountService: PfAccountService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pfAccountService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
