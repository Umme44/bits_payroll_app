import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ILocation } from 'app/shared/model/location.model';
import { LocationService } from './location.service';
import { EventManager } from '@angular/platform-browser';

@Component({
  templateUrl: './location-delete-dialog.component.html',
})
export class LocationDeleteDialogComponent {
  location?: ILocation;

  constructor(protected locationService: LocationService, public activeModal: NgbActiveModal, protected eventManager: EventManager) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.locationService.delete(id).subscribe(() => {
      //this.eventManager.broadcast('locationListModification');
      this.activeModal.close();
    });
  }
}
