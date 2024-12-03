import { Component } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { IOffer } from '../../shared/legacy/legacy-model/offer.model';

@Component({
  selector: 'jhi-notice-details-modal',
  templateUrl: 'offer-details-modal.component.html',
  styleUrls: ['./offer-details-modal.component.scss', '../dashboard.scss'],
})
export class OfferDetailsModalComponent {
  offer!: IOffer;
  commonService!: DashboardModalsCommon;

  constructor(protected activeModal: NgbActiveModal, private router: Router, protected applicationConfigService: ApplicationConfigService) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }

  // this type of method should be in service layer.
  getOfferImage(offerId: number): string {
    const url = this.applicationConfigService.getEndpointFor('files/offer-image/' + offerId);
    return url;
  }
}
