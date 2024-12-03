import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IMovementEntry } from '../movement-entry.model';
import { MovementEntryService } from '../service/movement-entry.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './movement-entry-delete-dialog.component.html',
})
export class MovementEntryDeleteDialogComponent {
  movementEntry?: IMovementEntry;

  constructor(
    protected movementEntryService: MovementEntryService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.movementEntryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('movementEntryListModification');
      this.activeModal.close();
    });
  }
}
