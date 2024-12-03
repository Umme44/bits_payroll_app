import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IManualAttendanceEntry } from '../manual-attendance-entry.model';
import { ManualAttendanceEntryService } from '../service/manual-attendance-entry.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './manual-attendance-entry-delete-dialog.component.html',
})
export class ManualAttendanceEntryDeleteDialogComponent {
  manualAttendanceEntry?: IManualAttendanceEntry;

  constructor(protected manualAttendanceEntryService: ManualAttendanceEntryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manualAttendanceEntryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
