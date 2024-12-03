import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIncomeTaxChallan } from '../income-tax-challan.model';
import { IncomeTaxChallanService } from '../service/income-tax-challan.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './income-tax-challan-delete-dialog.component.html',
})
export class IncomeTaxChallanDeleteDialogComponent {
  incomeTaxChallan?: IIncomeTaxChallan;

  constructor(protected incomeTaxChallanService: IncomeTaxChallanService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.incomeTaxChallanService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
