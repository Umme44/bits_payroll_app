import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeePinConfiguration } from '../employee-pin-configuration.model';
import { EmployeePinConfigurationService } from '../service/employee-pin-configuration.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-pin-configuration-delete-dialog.component.html',
})
export class EmployeePinConfigurationDeleteDialogComponent {
  employeePinConfiguration?: IEmployeePinConfiguration;

  constructor(protected employeePinConfigurationService: EmployeePinConfigurationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeePinConfigurationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
