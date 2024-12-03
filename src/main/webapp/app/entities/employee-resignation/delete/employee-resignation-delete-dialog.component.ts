import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {IEmployeeResignation} from "../employee-resignation.model";
import {EmployeeResignationService} from "../service/employee-resignation.service";
import {EventManager} from "../../../core/util/event-manager.service";

@Component({
  templateUrl: './employee-resignation-delete-dialog.component.html',
})
export class EmployeeResignationDeleteDialogComponent {
  employeeResignation?: IEmployeeResignation;

  constructor(
    protected employeeResignationService: EmployeeResignationService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeResignationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('employeeResignationListModification');
      this.activeModal.close();
    });
  }
}
