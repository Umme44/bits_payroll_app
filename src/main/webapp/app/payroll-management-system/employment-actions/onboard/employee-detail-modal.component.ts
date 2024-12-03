import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './employee-detail-modal.component.html',
})
export class EmployeeDetailModalComponent {
  employee?: IEmployee;
  active = 'top';

  constructor(protected employeeService: EmployeeService, public activeModal: NgbActiveModal, protected eventManager: EventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }
}
