import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalaryDeduction } from '../salary-deduction.model';
import { SalaryDeductionService } from '../service/salary-deduction.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './salary-deduction-delete-dialog.component.html',
})
export class SalaryDeductionDeleteDialogComponent {
  salaryDeduction?: ISalaryDeduction;

  constructor(protected salaryDeductionService: SalaryDeductionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salaryDeductionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
