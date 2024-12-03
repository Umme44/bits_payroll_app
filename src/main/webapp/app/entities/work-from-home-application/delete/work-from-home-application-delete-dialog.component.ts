import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IWorkFromHomeApplication } from '../work-from-home-application.model';
import { WorkFromHomeApplicationService } from '../service/work-from-home-application.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './work-from-home-application-delete-dialog.component.html',
})
export class WorkFromHomeApplicationDeleteDialogComponent {
  workFromHomeApplication?: IWorkFromHomeApplication;

  constructor(protected workFromHomeApplicationService: WorkFromHomeApplicationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.workFromHomeApplicationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
