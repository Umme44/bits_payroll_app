import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { EmployeeNOCService } from './employee-noc.service';
import { IEmployeeNOC } from '../../../shared/legacy/legacy-model/employee-noc.model';

@Component({
  templateUrl: './employee-noc-delete-dialog.component.html',
})
export class EmployeeNOCDeleteDialogComponent {
  employeeNOC?: IEmployeeNOC;

  constructor(protected employeeNOCService: EmployeeNOCService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeNOCService.delete(id).subscribe(() => {
      // this.eventManager.broadcast('employeeNOCListModification');
      this.activeModal.close();
    });
  }
}
