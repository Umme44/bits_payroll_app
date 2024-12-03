import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEmploymentHistory } from '../employment-history.model';
import { EmploymentHistoryService } from '../service/employment-history.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './employment-history-delete-dialog.component.html',
})
export class EmploymentHistoryDeleteDialogComponent {
  employmentHistory?: IEmploymentHistory;

  constructor(protected employmentHistoryService: EmploymentHistoryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.employmentHistoryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
