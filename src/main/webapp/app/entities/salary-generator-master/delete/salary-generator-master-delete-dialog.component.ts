import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISalaryGeneratorMaster } from '../salary-generator-master.model';
import { SalaryGeneratorMasterService } from '../service/salary-generator-master.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './salary-generator-master-delete-dialog.component.html',
})
export class SalaryGeneratorMasterDeleteDialogComponent {
  salaryGeneratorMaster?: ISalaryGeneratorMaster;

  constructor(protected salaryGeneratorMasterService: SalaryGeneratorMasterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.salaryGeneratorMasterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
