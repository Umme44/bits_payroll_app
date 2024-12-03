import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmployeeDocument } from '../employee-document.model';
import { EmployeeDocumentService } from '../service/employee-document.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employee-document-delete-dialog.component.html',
})
export class EmployeeDocumentDeleteDialogComponent {
  employeeDocument?: IEmployeeDocument;

  constructor(protected employeeDocumentService: EmployeeDocumentService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employeeDocumentService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
