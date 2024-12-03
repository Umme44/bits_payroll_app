import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeSalaryTempData } from '../employee-salary-temp-data.model';
import { EmployeeSalaryTempDataService } from '../service/employee-salary-temp-data.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-salary-temp-data-delete-dialog.component.html',
})
export class EmployeeSalaryTempDataDeleteDialogComponent {
  employeeSalaryTempData?: IEmployeeSalaryTempData;

  constructor(protected employeeSalaryTempDataService: EmployeeSalaryTempDataService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeSalaryTempDataService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
