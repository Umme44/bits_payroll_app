import { Component, OnInit } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';

@Component({
  selector: 'jhi-special-access-modal',
  templateUrl: './special-access-modal.component.html',
  styleUrls: ['../dashboard.scss'],
})
export class SpecialAccessModalComponent implements OnInit {
  commonService!: DashboardModalsCommon;
  currentEmployee!: IEmployee;

  constructor(protected activeModal: NgbActiveModal, private router: Router, protected modalService: NgbModal) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }
  ngOnInit(): void {}
}
