import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeNOC } from '../employee-noc.model';
import { EmployeeNOCService } from '../service/employee-noc.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-noc-delete-dialog.component.html',
})
export class EmployeeNOCDeleteDialogComponent {
  employeeNOC?: IEmployeeNOC;

  constructor(protected employeeNOCService: EmployeeNOCService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeNOCService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
