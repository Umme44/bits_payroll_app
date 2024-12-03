import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBankBranch } from '../bank-branch.model';
import { BankBranchService } from '../service/bank-branch.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './bank-branch-delete-dialog.component.html',
})
export class BankBranchDeleteDialogComponent {
  bankBranch?: IBankBranch;

  constructor(protected bankBranchService: BankBranchService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.bankBranchService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
