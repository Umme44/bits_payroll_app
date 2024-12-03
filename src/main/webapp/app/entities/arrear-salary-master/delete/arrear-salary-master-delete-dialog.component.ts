import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IArrearSalaryMaster } from '../arrear-salary-master.model';
import { ArrearSalaryMasterService } from '../service/arrear-salary-master.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './arrear-salary-master-delete-dialog.component.html',
})
export class ArrearSalaryMasterDeleteDialogComponent {
  arrearSalaryMaster?: IArrearSalaryMaster;

  constructor(protected arrearSalaryMasterService: ArrearSalaryMasterService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.arrearSalaryMasterService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
