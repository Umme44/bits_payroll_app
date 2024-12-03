import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeePin } from '../employee-pin.model';
import { EmployeePinService } from '../service/employee-pin.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-pin-delete-dialog.component.html',
})
export class EmployeePinDeleteDialogComponent {
  employeePin?: IEmployeePin;

  constructor(protected employeePinService: EmployeePinService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeePinService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
