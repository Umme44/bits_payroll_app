import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { IncomeTaxStatementService } from 'app/payroll-management-system/income-tax-statement/income-tax-statement.service';
import { IIncomeTaxStatement } from '../../shared/model/income-tax-statement-model';
import { IIncomeTaxDropDownMenu } from '../../shared/model/drop-down-income-tax.model';
import { EventManager } from '../../core/util/event-manager.service';
import { OrganizationFilesUrl } from '../../shared/constants/organization-files-url';
import { IConfig } from '../../entities/config/config.model';
import { ConfigService } from '../../entities/config/service/config.service';
import { DefinedKeys } from '../../config/defined-keys.constant';
import { swalForWarningWithMessage } from '../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-income-tax-statement',
  templateUrl: './income-tax-statement.component.html',
  styleUrls: ['income-tax-statement.component.scss'],
})
export class IncomeTaxStatementComponent implements OnInit {
  eventSubscriber?: Subscription;
  currentYear: number = new Date().getFullYear();

  taxStatementModel!: IIncomeTaxStatement;
  dropDownMenus!: IIncomeTaxDropDownMenu[];

  isIncomeTaxStatementVisibilityOn = false;
  isIncomeTaxStatementVisibilityConfig!: IConfig;

  editForm = this.fb.group({
    year: [],
  });

  public id!: number;

  organizationFullName = '';

  constructor(
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected fb: FormBuilder,
    protected incomeTaxStatementService: IncomeTaxStatementService,
    protected configService: ConfigService
  ) {
    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {
    this.getAllYear();
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

  getTaxReportById(id: number): void {
    this.incomeTaxStatementService.getTaxReportById(id).subscribe(data => (this.taxStatementModel = data.body!));
  }

  getAllYear(): void {
    this.incomeTaxStatementService.getAllAitConfigYears().subscribe(data => (this.dropDownMenus = data.body!));
  }

  onYearSelect(event: any): void {
    const id = event.target.value;
    if (id) {
      if (this.isIncomeTaxStatementVisibilityOn === false) {
        swalForWarningWithMessage(
          'Due to ongoing upgrades in the Income Tax configuration, the availability of tax statements is temporarily disabled.',
          5000
        );
        return;
      } else {
        this.id = parseInt(id, 10);
        this.getTaxReportById(this.id);
      }
    }
  }

  downloadAsPDF(): void {
    window.print();
  }

  getDocumentLetterHead(): string {
    return OrganizationFilesUrl.DOCUMENT_LETTER_HEAD;
  }

  getFinanceManagerSignature(): string {
    return OrganizationFilesUrl.FINANCE_MANAGER_SIGNATURE;
  }

  getOrganizationStamp(): string {
    return OrganizationFilesUrl.ORGANIZATION_STAMP;
  }
}
