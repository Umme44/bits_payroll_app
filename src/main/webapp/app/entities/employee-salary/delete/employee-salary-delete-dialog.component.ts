import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {IEmployeeSalary} from "../employee-salary.model";
import {EmployeeSalaryService} from "../service/employee-salary.service";
import {EventManager} from "../../../core/util/event-manager.service";


@Component({
  templateUrl: './employee-salary-delete-dialog.component.html',
})
export class EmployeeSalaryDeleteDialogComponent {
  employeeSalary?: IEmployeeSalary;

  constructor(
    protected employeeSalaryService: EmployeeSalaryService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeSalaryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('employeeSalaryListModification');
      this.activeModal.close();
    });
  }
}
