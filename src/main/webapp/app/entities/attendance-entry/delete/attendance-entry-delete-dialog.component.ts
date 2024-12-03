import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {IAttendanceEntry} from "../attendance-entry.model";
import {AttendanceEntryService} from "../service/attendance-entry.service";
import {EventManager} from "../../../core/util/event-manager.service";

@Component({
  templateUrl: './attendance-entry-delete-dialog.component.html',
})
export class AttendanceEntryDeleteDialogComponent {
  attendanceEntry?: IAttendanceEntry;

  constructor(
    protected attendanceEntryService: AttendanceEntryService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.attendanceEntryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('attendanceEntryListModification');
      this.activeModal.close();
    });
  }
}
