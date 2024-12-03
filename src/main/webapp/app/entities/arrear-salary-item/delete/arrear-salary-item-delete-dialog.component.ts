import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArrearSalaryItem } from '../arrear-salary-item.model';
import { ArrearSalaryItemService } from '../service/arrear-salary-item.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './arrear-salary-item-delete-dialog.component.html',
})
export class ArrearSalaryItemDeleteDialogComponent {
  arrearSalaryItem?: IArrearSalaryItem;

  constructor(protected arrearSalaryItemService: ArrearSalaryItemService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arrearSalaryItemService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
