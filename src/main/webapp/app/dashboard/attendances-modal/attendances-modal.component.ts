import { Component } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

@Component({
  selector: 'jhi-dashboard-ats-modal',
  templateUrl: './attendances-modal.component.html',
  styleUrls: ['../dashboard.scss'],
})
export class AttendancesModalComponent {
  commonService!: DashboardModalsCommon;

  constructor(protected activeModal: NgbActiveModal, private router: Router) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }
}
