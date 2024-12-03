import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {ISpecialShiftTiming} from "../special-shift-timing.model";
import {SpecialShiftTimingService} from "../service/special-shift-timing.service";
import {EventManager} from "../../../core/util/event-manager.service";

@Component({
  templateUrl: './special-shift-timing-delete-dialog.component.html',
})
export class SpecialShiftTimingDeleteDialogComponent {
  specialShiftTiming?: ISpecialShiftTiming;

  constructor(
    protected specialShiftTimingService: SpecialShiftTimingService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.specialShiftTimingService.delete(id).subscribe(() => {
      this.eventManager.broadcast('specialShiftTimingListModification');
      this.activeModal.close();
    });
  }
}
