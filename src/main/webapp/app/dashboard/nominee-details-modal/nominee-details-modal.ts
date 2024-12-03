import { Component, OnInit } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { UserPfStatementService } from '../../provident-fund-management/user-pf-statement/user-pf-statement.service';
import { SearchModalService } from '../../shared/specialized-search/search-modal/search-modal.service';
import { DashboardService } from '../dashboard.service';

@Component({
  selector: 'jhi-nominee-details-modal',
  templateUrl: './nominee-details-modal.html',
  styleUrls: ['../dashboard.scss'],
})
export class NomineeDetailsModalComponent implements OnInit {
  commonService!: DashboardModalsCommon;
  isValidUserPfStatement!: boolean;

  isEmployeeValidForGF!: boolean;
  isEmployeeValidForPF!: boolean;
  isEmployeeValidForGeneralNominee!: boolean;

  constructor(
    protected activeModal: NgbActiveModal,
    private router: Router,
    protected userPfStatementService: UserPfStatementService,
    protected dashboardService: DashboardService,
    protected modalService: NgbModal,
    protected searchModalService: SearchModalService
  ) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }
  ngOnInit(): void {
    this.loadUserPfStatementValidity();
    this.checkEmployeeEligibilityForGf();
    this.checkEmployeeEligibilityForPF();
    this.checkEmployeeEligibilityForGeneralNominee();
  }

  loadUserPfStatementValidity(): void {
    this.userPfStatementService.checkValidityOfUserPfStatement().subscribe(response => {
      this.isValidUserPfStatement = response;
    });
  }

  checkEmployeeEligibilityForPF(): void {
    this.dashboardService.isEmployeeEligibleForProvidentFund().subscribe(res => (this.isEmployeeValidForPF = res.body!));
  }

  checkEmployeeEligibilityForGf(): void {
    this.dashboardService.isEmployeeEligibleForGratuityFund().subscribe(res => (this.isEmployeeValidForGF = res.body!));
  }

  checkEmployeeEligibilityForGeneralNominee(): void {
    this.dashboardService.isEmployeeEligibleForGeneralNominee().subscribe(res => (this.isEmployeeValidForGeneralNominee = res.body!));
  }

  navigateToPfNominee(routeLink: string): void {
    if (this.isEmployeeValidForPF) {
      this.commonService.routerNavigation(routeLink);
    }
  }

  navigateToGeneralNominee(routeLink: string): void {
    if (this.isEmployeeValidForGeneralNominee) {
      this.commonService.routerNavigation(routeLink);
    }
  }

  navigateToGfNominee(routeLink: string): void {
    if (this.isEmployeeValidForGF) {
      this.commonService.routerNavigation(routeLink);
    }
  }
}
