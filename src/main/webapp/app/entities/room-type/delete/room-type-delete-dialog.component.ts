import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IRoomType } from '../room-type.model';
import { RoomTypeService } from '../service/room-type.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './room-type-delete-dialog.component.html',
})
export class RoomTypeDeleteDialogComponent {
  roomType?: IRoomType;

  constructor(protected roomTypeService: RoomTypeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.roomTypeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
