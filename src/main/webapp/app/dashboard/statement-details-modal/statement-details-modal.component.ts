import { Component, OnInit } from '@angular/core';
import { DashboardModalsCommon } from '../dashboard-modals-common';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { UserPfStatementService } from '../../provident-fund-management/user-pf-statement/user-pf-statement.service';
import { SearchModalService } from '../../shared/specialized-search/search-modal/search-modal.service';
import { myPfStatementRoute } from '../../provident-fund-management/provident-fund-management.module';
import { IncomeTaxStatementService } from '../../payroll-management-system/income-tax-statement/income-tax-statement.service';
import { UserTaxAcknowledgementValidation } from '../../shared/model/user-tax-acknowledgement-validation.model';
import { UserTaxAcknowledgementReceiptService } from '../../shared/legacy/legacy-service/user-tax-acknowledgement-receipt.service';
import { IConfig } from '../../entities/config/config.model';
import { ConfigService } from '../../entities/config/service/config.service';
import { DefinedKeys } from '../../config/defined-keys.constant';
import { swalForWarningWithMessage } from '../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-statement-details-modal',
  templateUrl: './statement-details-modal.component.html',
  styleUrls: ['../dashboard.scss'],
})
export class StatementDetailsModalComponent implements OnInit {
  commonService!: DashboardModalsCommon;
  isValidUserPfStatement!: boolean;
  isValidUserForTaxStatement!: boolean;
  userTaxAcknowledgementValidation!: UserTaxAcknowledgementValidation;

  isIncomeTaxStatementVisibilityOn = false;
  isIncomeTaxStatementVisibilityConfig!: IConfig;

  constructor(
    protected activeModal: NgbActiveModal,
    private router: Router,
    protected userPfStatementService: UserPfStatementService,
    protected incomeTaxService: IncomeTaxStatementService,
    protected userTaxAcknowledgementReceiptService: UserTaxAcknowledgementReceiptService,
    protected modalService: NgbModal,
    protected searchModalService: SearchModalService,
    protected configService: ConfigService
  ) {
    this.commonService = new DashboardModalsCommon(this.activeModal, this.router);
  }
  ngOnInit(): void {
    this.loadUserPfStatementValidity();
    this.loadUserTaxStatementValidity();
    this.loadIncomeTaxStatementVisibilityConfig();
  }

  loadIncomeTaxStatementVisibilityConfig(): void {
    this.configService.findByKeyCommon(DefinedKeys.is_income_tax_visibility_enabled_for_user_end).subscribe(res => {
      this.isIncomeTaxStatementVisibilityConfig = res.body!;
      if (this.isIncomeTaxStatementVisibilityConfig.value === 'TRUE') {
        this.isIncomeTaxStatementVisibilityOn = true;
      } else {
        this.isIncomeTaxStatementVisibilityOn = false;
      }
    });
  }

  loadUserPfStatementValidity(): void {
    this.userPfStatementService.checkValidityOfUserPfStatement().subscribe(response => {
      this.isValidUserPfStatement = response;
    });
  }

  loadUserTaxStatementValidity(): void {
    this.incomeTaxService.checkValidityOfUserIncomeTaxStatement().subscribe(response => {
      this.isValidUserForTaxStatement = response;
    });
  }

  routeToPfStatement(): void {
    if (this.isValidUserPfStatement) {
      this.commonService.routerNavigation(myPfStatementRoute);
    }
  }

  routeToTaxStatement(): void {
    if (this.isIncomeTaxStatementVisibilityOn === false) {
      swalForWarningWithMessage(
        'Due to ongoing upgrades in the Income Tax configuration, the availability of tax statements is temporarily disabled.',
        5000
      );
      return;
    }
    if (this.isValidUserForTaxStatement) {
      this.commonService.routerNavigation('/income-tax-statement');
    }
  }

  checkValidityTaxAcknowledgementReceipt(): void {
    this.commonService.routerNavigation('user-tax-acknowledgement-receipt');
  }
}
