import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IAttendanceSummary } from '../attendance-summary.model';
import { AttendanceSummaryService } from '../service/attendance-summary.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './attendance-summary-delete-dialog.component.html',
})
export class AttendanceSummaryDeleteDialogComponent {
  attendanceSummary?: IAttendanceSummary;

  constructor(protected attendanceSummaryService: AttendanceSummaryService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attendanceSummaryService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
