import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ITimeSlot } from '../time-slot.model';
import { TimeSlotService } from '../service/time-slot.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './time-slot-delete-dialog.component.html',
})
export class TimeSlotDeleteDialogComponent {
  timeSlot?: ITimeSlot;

  constructor(protected timeSlotService: TimeSlotService, public activeModal: NgbActiveModal, protected eventManager: EventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.timeSlotService.delete(id).subscribe(() => {
      this.eventManager.broadcast('timeSlotListModification');
      this.activeModal.close();
    });
  }
}
