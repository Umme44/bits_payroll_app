import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IEmployee } from '../employee-custom.model';
import { EmployeeCustomService } from '../service/employee-custom.service';

@Component({
  templateUrl: './employee-custom-delete-dialog.component.html',
})
export class EmployeeCustomDeleteDialogComponent {
  employee?: IEmployee;

  constructor(protected employeeService: EmployeeCustomService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeService.delete(id).subscribe(() => {
      this.activeModal.close();
    });
  }
}
