import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHoldSalaryDisbursement } from '../hold-salary-disbursement.model';
import { HoldSalaryDisbursementService } from '../service/hold-salary-disbursement.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './hold-salary-disbursement-delete-dialog.component.html',
})
export class HoldSalaryDisbursementDeleteDialogComponent {
  holdSalaryDisbursement?: IHoldSalaryDisbursement;

  constructor(protected holdSalaryDisbursementService: HoldSalaryDisbursementService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.holdSalaryDisbursementService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
