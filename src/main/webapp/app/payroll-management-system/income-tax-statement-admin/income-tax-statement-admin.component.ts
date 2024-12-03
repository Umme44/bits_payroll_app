import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { IIncomeTaxStatement } from '../../shared/model/income-tax-statement-model';
import { IIncomeTaxDropDownMenu } from '../../shared/model/drop-down-income-tax.model';
import { IncomeTaxStatementAdminService } from './income-tax-statement-admin.service';
import { IEmployeeMinimal } from '../../shared/model/employee-minimal.model';
import { OrganizationFilesUrl } from '../../shared/constants/organization-files-url';
import { EventManager } from '../../core/util/event-manager.service';

@Component({
  selector: 'jhi-income-tax-statement-admin',
  templateUrl: './income-tax-statement-admin.component.html',
  styleUrls: ['income-tax-statement-admin.component.scss'],
})
export class IncomeTaxStatementAdminComponent implements OnInit {
  eventSubscriber?: Subscription;
  currentYear: number = new Date().getFullYear();

  taxStatementModel!: IIncomeTaxStatement;
  dropDownMenus!: IIncomeTaxDropDownMenu[];
  activeEmployeeList!: IEmployeeMinimal[];
  selectedEmployeeId = '';

  organizationFullName = '';

  editForm = this.fb.group({
    year: [],
    selectedEmployeeId: [],
  });

  public id!: number;

  constructor(
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected fb: FormBuilder,
    protected incomeTaxStatementAdminService: IncomeTaxStatementAdminService
  ) {
    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {
    this.getAllYear();
  }

  getEmployeeListByAitConfigId(id: number): void {
    this.incomeTaxStatementAdminService.getAllEligibleEmployeeForTaxStatement(id).subscribe(res => {
      this.activeEmployeeList = res.body!;

      this.activeEmployeeList = this.activeEmployeeList.map(employee => {
        return {
          id: employee.id,
          pin: employee.pin,
          fullName: employee.pin + '-' + employee.fullName + '-' + employee.designationName + '-' + employee.departmentName,
        };
      });
    });
  }

  getAllYear(): void {
    this.incomeTaxStatementAdminService.getAllYear().subscribe(data => (this.dropDownMenus = data.body!));
  }

  onYearSelect(event: any): void {
    const id = event.target.value;
    if (id) {
      this.id = parseInt(id, 10);
      this.getEmployeeListByAitConfigId(this.id);
    }
  }

  downloadAsPDF(): void {
    window.print();
  }

  onSelectEmployee(employeeId: any): void {
    if (employeeId !== null && employeeId !== undefined) {
      this.incomeTaxStatementAdminService.getTaxReport(employeeId, this.editForm.get(['year'])!.value).subscribe(res => {
        this.taxStatementModel = res.body!;
      });
    }
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
