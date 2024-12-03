import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'app/core/util/event-manager.service';
import { ManualAttendanceEntryCommonService } from './manual-attendance-entry-common.service';
import { IManualAttendanceEntry } from '../../shared/legacy/legacy-model/manual-attendance-entry.model';

@Component({
  templateUrl: './manual-attendance-entry-delete-dialog.component.html',
})
export class ManualAttendanceEntryDeleteDialogComponent {
  manualAttendanceEntry?: IManualAttendanceEntry;

  constructor(
    protected manualAttendanceEntryService: ManualAttendanceEntryCommonService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manualAttendanceEntryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('manualAttendanceEntryListModification');
      this.activeModal.close();
    });
  }
}
