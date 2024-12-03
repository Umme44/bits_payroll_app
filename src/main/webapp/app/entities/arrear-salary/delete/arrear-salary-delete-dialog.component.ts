import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArrearSalary } from '../arrear-salary.model';
import { ArrearSalaryService } from '../service/arrear-salary.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './arrear-salary-delete-dialog.component.html',
})
export class ArrearSalaryDeleteDialogComponent {
  arrearSalary?: IArrearSalary;

  constructor(protected arrearSalaryService: ArrearSalaryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arrearSalaryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
