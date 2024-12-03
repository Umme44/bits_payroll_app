import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IItemInformation } from '../item-information.model';
import { ItemInformationService } from '../service/item-information.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  templateUrl: './item-information-delete-dialog.component.html',
})
export class ItemInformationDeleteDialogComponent {
  itemInformation?: IItemInformation;

  constructor(
    protected itemInformationService: ItemInformationService,
    public activeModal: NgbActiveModal,
    protected eventManager: EventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemInformationService.delete(id).subscribe(() => {
      this.eventManager.broadcast('itemInformationListModification');
      this.activeModal.close();
    });
  }
}

/*
import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemInformation } from '../item-information.model';
import { ItemInformationService } from '../service/item-information.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './item-information-delete-dialog.component.html',
})
export class ItemInformationDeleteDialogComponent {
  itemInformation?: IItemInformation;

  constructor(protected itemInformationService: ItemInformationService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.itemInformationService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
*/
