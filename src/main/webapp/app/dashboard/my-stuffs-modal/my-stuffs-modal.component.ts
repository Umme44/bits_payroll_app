import { Component, OnInit } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { UserPfStatementService } from '../../provident-fund-management/user-pf-statement/user-pf-statement.service';
import { SearchModalService } from '../../shared/specialized-search/search-modal/search-modal.service';

import { StatementDetailsModalComponent } from '../statement-details-modal/statement-details-modal.component';
import { PayslipDetailsModalComponent } from '../payslip-details-modal/payslip-details-modal';
import { NomineeDetailsModalComponent } from '../nominee-details-modal/nominee-details-modal';
import { myPfNomineeRoute, myPfStatementRoute } from '../../provident-fund-management/provident-fund-management.module';
import { DashboardService } from '../dashboard.service';
import { SearchModalComponent } from '../../shared/specialized-search/search-modal/search-modal.component';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';

@Component({
  selector: 'jhi-dashboard-my-stuffs-modal',
  templateUrl: './my-stuffs-modal.component.html',
  styleUrls: ['../dashboard.scss'],
})
export class MyStuffsModalComponent implements OnInit {
  commonService!: DashboardModalsCommon;
  isValidUserPfStatement!: boolean;
  currentEmployee!: IEmployee;
  isEmployeeEligible!: boolean;

  pfNomineeRoute = myPfNomineeRoute;

  constructor(
    protected activeModal: NgbActiveModal,
    private router: Router,
    protected userPfStatementService: UserPfStatementService,
    protected modalService: NgbModal,
    protected dashboardService: DashboardService,
    protected searchModalService: SearchModalService
  ) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }

  ngOnInit(): void {
    this.loadUserPfStatementValidity();
    this.checkEmployeeEligibilityForInsurance();
  }

  loadUserPfStatementValidity(): void {
    this.userPfStatementService.checkValidityOfUserPfStatement().subscribe(response => {
      this.isValidUserPfStatement = response;
    });
  }

  routeToPfStatement(): void {
    if (this.isValidUserPfStatement) {
      this.commonService.routerNavigation(myPfStatementRoute);
    }
  }

  checkEmployeeEligibilityForInsurance(): void {
    this.dashboardService.isEmployeeEligibleForInsuranceRegistration().subscribe(res => (this.isEmployeeEligible = res.body!));
  }

  openMyHierarchyModal(): void {
    this.searchModalService.getCurrentEmployee().subscribe(response => {
      this.currentEmployee = response.body!;
      if (this.currentEmployee.id !== undefined) {
        this.searchModalService.find(this.currentEmployee.id).subscribe(result => {
          const modalRef = this.modalService.open(SearchModalComponent, { size: 'xl', backdrop: 'static' });
          modalRef.componentInstance.employeeSpecializedSearch = result.body;
        });
      } else {
        alert(this.currentEmployee.id);
      }
    });
  }

  openMyStatementModal(): void {
    this.closeAllModal();
    this.modalService.open(StatementDetailsModalComponent, {
      centered: true,
    });
  }

  openMyPaySlipModal(): void {
    this.closeAllModal();
    this.modalService.open(PayslipDetailsModalComponent, {
      centered: true,
    });
  }

  openMyNomineeModal(): void {
    this.closeAllModal();
    this.modalService.open(NomineeDetailsModalComponent, {
      centered: true,
    });
  }

  goToNomineeDashboard(routerLink: string): void {
    if (this.isEmployeeEligible) {
      this.router.navigate([routerLink]);
    } else {
      return;
    }
  }

  goToInsuranceProfile(routerLink: string): void {
    if (this.isEmployeeEligible) {
      this.router.navigate([routerLink]);
    } else {
      return;
    }
  }

  // openMyInsuranceModal(): void {
  //   this.modalService.dismissAll();
  // }

  closeAllModal(): void {
    // dismiss all Modal, if navigate to other page
    this.modalService.dismissAll();
  }
}
