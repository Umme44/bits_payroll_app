import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeStaticFile } from '../employee-static-file.model';
import { EmployeeStaticFileService } from '../service/employee-static-file.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-static-file-delete-dialog.component.html',
})
export class EmployeeStaticFileDeleteDialogComponent {
  employeeStaticFile?: IEmployeeStaticFile;

  constructor(protected employeeStaticFileService: EmployeeStaticFileService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeStaticFileService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
