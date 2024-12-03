import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { MovementEntryService } from './movement-entry.service';
import { IMovementEntry } from '../../shared/legacy/legacy-model/movement-entry.model';

@Component({
  templateUrl: './movement-entry-delete-dialog.component.html',
})
export class MovementEntryDeleteDialogComponent {
  movementEntry?: IMovementEntry;

  constructor(
    protected movementEntryService: MovementEntryService,
    public activeModal: NgbActiveModal
  ) // protected eventManager: JhiEventManager
  {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.movementEntryService.delete(id).subscribe(() => {
      // this.eventManager.broadcast('movementEntryListModification');
      this.activeModal.close();
    });
  }
}
