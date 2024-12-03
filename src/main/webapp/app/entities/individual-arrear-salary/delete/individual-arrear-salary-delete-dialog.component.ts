import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IIndividualArrearSalary } from '../individual-arrear-salary.model';
import { IndividualArrearSalaryService } from '../service/individual-arrear-salary.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './individual-arrear-salary-delete-dialog.component.html',
})
export class IndividualArrearSalaryDeleteDialogComponent {
  individualArrearSalary?: IIndividualArrearSalary;

  constructor(protected individualArrearSalaryService: IndividualArrearSalaryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.individualArrearSalaryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
