import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoomRequisition } from '../room-requisition.model';
import { RoomRequisitionService } from '../service/room-requisition.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './room-requisition-delete-dialog.component.html',
})
export class RoomRequisitionDeleteDialogComponent {
  roomRequisition?: IRoomRequisition;

  constructor(protected roomRequisitionService: RoomRequisitionService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roomRequisitionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
