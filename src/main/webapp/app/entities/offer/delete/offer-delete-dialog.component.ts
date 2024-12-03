import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import {IOffer} from "../offer.model";
import {OfferService} from "../service/offer.service";
import {EventManager} from "../../../core/util/event-manager.service";


@Component({
  templateUrl: './offer-delete-dialog.component.html',
})
export class OfferDeleteDialogComponent {
  offer?: IOffer;

  constructor(protected offerService: OfferService, public activeModal: NgbActiveModal, protected eventManager: EventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.offerService.delete(id).subscribe(() => {
      this.eventManager.broadcast('offerListModification');
      this.activeModal.close();
    });
  }
}
