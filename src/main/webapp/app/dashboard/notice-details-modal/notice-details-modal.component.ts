import { Component } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { IOfficeNotices } from '../../shared/legacy/legacy-model/office-notices.model';

@Component({
  selector: 'jhi-notice-details-modal',
  templateUrl: 'notice-details-modal.component.html',
  styleUrls: ['./notice-details-modal.component.scss', '../dashboard.scss'],
})
export class NoticeDetailsModalComponent {
  notice!: IOfficeNotices;
  commonService!: DashboardModalsCommon;

  constructor(protected activeModal: NgbActiveModal, private router: Router) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }
}
