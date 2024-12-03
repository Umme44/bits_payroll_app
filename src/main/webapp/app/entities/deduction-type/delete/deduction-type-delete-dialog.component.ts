import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDeductionType } from '../deduction-type.model';
import { DeductionTypeService } from '../service/deduction-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './deduction-type-delete-dialog.component.html',
})
export class DeductionTypeDeleteDialogComponent {
  deductionType?: IDeductionType;

  constructor(protected deductionTypeService: DeductionTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.deductionTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
