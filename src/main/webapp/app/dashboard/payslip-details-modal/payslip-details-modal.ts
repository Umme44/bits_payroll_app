import { Component, OnInit } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { UserPfStatementService } from '../../provident-fund-management/user-pf-statement/user-pf-statement.service';
import { SearchModalService } from '../../shared/specialized-search/search-modal/search-modal.service';

@Component({
  selector: 'jhi-payslip-details-modal',
  templateUrl: './payslip-details-modal.html',
  styleUrls: ['../dashboard.scss'],
})
export class PayslipDetailsModalComponent implements OnInit {
  commonService!: DashboardModalsCommon;
  isValidUserPfStatement!: boolean;

  constructor(
    protected activeModal: NgbActiveModal,
    private router: Router,
    protected userPfStatementService: UserPfStatementService,
    protected modalService: NgbModal,
    protected searchModalService: SearchModalService
  ) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }
  ngOnInit(): void {
    this.loadUserPfStatementValidity();
  }

  loadUserPfStatementValidity(): void {
    this.userPfStatementService.checkValidityOfUserPfStatement().subscribe(response => {
      this.isValidUserPfStatement = response;
    });
  }

  routeToPfStatement(): void {
    if (this.isValidUserPfStatement) {
      this.commonService.routerNavigation('/pf/user-pf-statement');
    }
  }
}
