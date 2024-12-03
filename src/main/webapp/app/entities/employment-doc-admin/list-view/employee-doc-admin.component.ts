import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { FormBuilder } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import Swal from 'sweetalert2';
import { swalConfirmationCommon } from 'app/shared/swal-common/swal-confirmation.common';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_REJECTED,
  SWAL_REJECTED_ICON,
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
  SWAL_RESPONSE_ERROR_TITLE,
} from 'app/shared/swal-common/swal.properties.constant';
import { EmployeeNocAdminService } from 'app/entities/employment-doc-admin/employee-noc-admin/service/employee-noc-admin.service';
import { EmploymentCertificateAdminService } from 'app/entities/employment-doc-admin/employment-certificate-admin/service/employment-certificate-admin.service';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import { EmployeeSalaryCertificateAdminService } from 'app/entities/employment-doc-admin/employee-salary-certificate-admin/service/employee-salary-certificate-admin.service';
import { IEmployeeNOC } from '../model/employee-noc.model';
import { IEmploymentCertificate } from '../model/employment-certificate.model';
import { ISalaryCertificate } from '../model/salary-certificate.model';
import dayjs from 'dayjs/esm';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { CertificateApprovalDto, ICertificateApprovalDto } from '../model/certificate-approval-dto';
import {CustomValidator} from "../../../validators/custom-validator";

export const NO_OBJECTION_CERTIFICATE = 'NOC';
export const EMPLOYMENT_CERTIFICATE = 'EC';
export const EMPLOYEE_SALARY_CERTIFICATE = 'ESC';

@Component({
  selector: 'jhi-employee-docs-admin',
  templateUrl: './employee-doc-admin.component.html',
})
export class EmployeeDocsAdminComponent implements OnInit {
  employeeNOCS: IEmployeeNOC[] = [];
  employmentCertificates: IEmploymentCertificate[] = [];
  salaryCertificates: ISalaryCertificate[] = [];
  employees: IEmployee[] = [];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  years: number[] = [];
  currentYear: number = new Date().getFullYear();

  // selectedStatus = 'ALL';
  // searchText = '';
  // selectedCertificateType = 'NOC';
  // selectedYear = 0;

  selectedEmployeeNoc?: IEmployeeNOC;
  selectedEmploymentCertificate?: IEmploymentCertificate;
  selectedSalaryCertificate?: ISalaryCertificate;

  today: dayjs.Dayjs = dayjs().startOf('day');

  searchForm = this.fb.group({
    selectedStatus: ['ALL'],
    searchText: ['', [CustomValidator.naturalTextValidator()]],
    selectedCertificateType: [''],
    selectedYear: [0],
  });
  editForm = this.fb.group({
    signatoryPersonId: [],
    issueDate: [],
    reason: [],
  });

  isInvalidRefId = false;
  isInvalidSignatoryPerson = false;
  isInvalidIssueDate = false;
  isInvalidReason = false;

  constructor(
    protected employeeNocAdminService: EmployeeNocAdminService,
    protected employmentCertificateAdminService: EmploymentCertificateAdminService,
    protected employeeSalaryCertificateAdminService: EmployeeSalaryCertificateAdminService,
    protected modalService: NgbModal,
    private fb: FormBuilder,
    private employeeService: EmployeeService
  ) {
    this.employeeNOCS = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;

    this.editForm.get('issueDate')!.setValue(this.today);

    this.years = [
      this.currentYear + 1,
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
      this.currentYear - 6,
    ];
  }

  onChangeCertificateType(type: string): void {
    this.searchForm.get('selectedStatus')!.setValue('ALL');

    if (type === NO_OBJECTION_CERTIFICATE) {
      this.searchForm.get('selectedCertificateType')!.setValue(NO_OBJECTION_CERTIFICATE);
    } else if (type === EMPLOYEE_SALARY_CERTIFICATE) {
      this.searchForm.get('selectedCertificateType')!.setValue(EMPLOYEE_SALARY_CERTIFICATE);
    } else {
      this.searchForm.get('selectedCertificateType')!.setValue(EMPLOYMENT_CERTIFICATE);
    }
    this.reset();
  }

  loadAll(): void {
    if(this.searchForm.get('searchText').errors?.pattern){
      return;
    }
    if (this.searchForm.get('selectedCertificateType')!.value === NO_OBJECTION_CERTIFICATE) {
      this.loadEmployeeNoc();
    } else if (this.searchForm.get('selectedCertificateType')!.value === EMPLOYEE_SALARY_CERTIFICATE) {
      this.loadEmployeeSalaryCertificate();
    } else {
      this.loadEmploymentCertificate();
    }
  }

  loadEmployeeNoc(): void {
    this.employeeNocAdminService
      .queryEmployeeNoc(this.getRequestObject())
      .subscribe((res: HttpResponse<IEmployeeNOC[]>) => (this.employeeNOCS = res.body ?? []));
  }

  loadEmploymentCertificate(): void {
    this.employmentCertificateAdminService
      .query(this.getRequestObject())
      .subscribe((res: HttpResponse<IEmploymentCertificate[]>) => (this.employmentCertificates = res.body ?? []));
  }

  loadEmployeeSalaryCertificate(): void {
    this.employeeSalaryCertificateAdminService
      .querySalaryCertificate(this.getRequestObject())
      .subscribe((res: HttpResponse<ISalaryCertificate[]>) => (this.salaryCertificates = res.body ?? []));
  }

  getRequestObject(): any {
    const reqObj: any = {
      searchText: this.searchForm.get('searchText')!.value,
      page: this.page,
      size: this.itemsPerPage,
      selectedYear: this.searchForm.get('selectedYear')!.value,
    };

    const status = this.searchForm.get('selectedStatus')!.value;
    if (status !== null && status !== undefined && status !== 'ALL') {
      reqObj.status = this.searchForm.get('selectedStatus')!.value;
    }
    return reqObj;
  }

  reset(): void {
    this.page = 0;
    this.employeeNOCS = [];
    this.employmentCertificates = [];
    this.salaryCertificates = [];
    this.loadAll();
  }

  loadAllEmployees(): void {
    this.employeeService.getAllMinimal().subscribe(res => {
      this.employees = res.body!;
      this.employees = this.employees.map(item => {
        return {
          id: item.id,
          fullName: item.pin + ' - ' + item.fullName,
        };
      });
    });
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.populateDataToTheSearchForm();
    this.loadAllEmployees();
    this.loadAll();
  }

  // ngOnDestroy(): void {
  //   if (this.eventSubscriber) {
  //     this.eventManager.destroy(this.eventSubscriber);
  //   }
  // }

  populateDataToTheSearchForm(): void {
    this.searchForm.get('selectedStatus')!.setValue('ALL');
    this.searchForm.get('searchText')!.setValue('');
    this.searchForm.get('selectedCertificateType')!.setValue(NO_OBJECTION_CERTIFICATE);
    this.searchForm.get('selectedYear')!.setValue(0);
  }

  trackId(index: number, item: IEmployeeNOC): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  trackSalaryCertificateId(index: number, item: ISalaryCertificate): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  // registerChangeInEmployeeNOCS(): void {
  //   this.eventSubscriber = this.eventManager.subscribe('employeeNOCListModification', () => this.reset());
  // }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  // protected paginateEmployeeNOCS(data: IEmployeeNOC[] | null, headers: HttpHeaders): void {
  //   const headersLink = headers.get('link');
  //   this.links = this.parseLinks.parse(headersLink ? headersLink : '');
  //   if (data) {
  //     for (let i = 0; i < data.length; i++) {
  //       this.employeeNOCS.push(data[i]);
  //     }
  //   }
  // }

  // protected paginateEmploymentCertificate(data: IEmploymentCertificate[] | null, headers: HttpHeaders): void {
  //   const headersLink = headers.get('link');
  //   this.links = this.parseLinks.parse(headersLink ? headersLink : '');
  //   if (data) {
  //     for (let i = 0; i < data.length; i++) {
  //       this.employmentCertificates.push(data[i]);
  //     }
  //   }
  // }

  // protected paginateSalaryCertificate(data: ISalaryCertificate[] | null, headers: HttpHeaders): void {
  //   const headersLink = headers.get('link');
  //   this.links = this.parseLinks.parse(headersLink ? headersLink : '');
  //   if (data) {
  //     for (let i = 0; i < data.length; i++) {
  //       this.salaryCertificates.push(data[i]);
  //     }
  //   }
  // }

  deleteNOC(employeeNOC: IEmployeeNOC): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeeNocAdminService.deleteEmployeeNoc(employeeNOC.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.reset();
          },
          err => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  deleteSalaryCertificate(salaryCertificate: ISalaryCertificate): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeeSalaryCertificateAdminService.delete(salaryCertificate.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.reset();
          },
          err => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  deleteEmploymentCertificate(employmentCertificate: IEmploymentCertificate): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employmentCertificateAdminService.delete(employmentCertificate.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.reset();
          },
          err => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  onStatusChange(status: string): void {
    if (status === 'SENT_FOR_GENERATION') {
      this.searchForm.get('selectedStatus')!.setValue('SENT_FOR_GENERATION');
    } else if (status === 'GENERATED') {
      this.searchForm.get('selectedStatus')!.setValue('GENERATED');
    } else {
      this.searchForm.get('selectedStatus')!.setValue('ALL');
    }
    this.reset();
  }

  onSearchTextChange(event: any): void {
    // this.searchText = event.target.value;
    this.reset();
  }

  onChangeYear(event: any): void {
    // this.selectedYear = event.target.value;
    this.reset();
  }

  createCertificateApprovalDto(): ICertificateApprovalDto {
    return {
      ...new CertificateApprovalDto(),
      signatoryPersonId: this.editForm.get(['signatoryPersonId'])!.value ?? null,
      issueDate: this.editForm.get(['issueDate'])!.value ?? null,
      reason: this.editForm.get(['reason'])!.value ?? null,
    };
  }

  approve(content: any, employeeNOC?: IEmployeeNOC, employmentCertificate?: IEmploymentCertificate): void {
    if (employeeNOC && employeeNOC.id !== undefined) {
      this.editForm.get('issueDate')!.setValue(this.today);

      this.selectedEmployeeNoc = employeeNOC;
      this.selectedEmploymentCertificate = undefined;
      this.modalService.open(content).result.then(
        result => {
          this.onApproveEmployeeNoc();
        },
        reason => {
          this.resetEditForm();
        }
      );
    } else if (employmentCertificate && employmentCertificate !== undefined) {
      this.editForm.get('issueDate')!.setValue(this.today);

      this.selectedEmploymentCertificate = employmentCertificate;
      this.selectedEmployeeNoc = undefined;
      this.modalService.open(content).result.then(
        result => {
          this.onApproveEmploymentCertificate();
        },
        reason => {
          this.resetEditForm();
        }
      );
    }
  }

  approveSalaryCertificate(content: any, salaryCertificate: ISalaryCertificate): void {
    if (salaryCertificate && salaryCertificate.id !== undefined) {
      this.editForm.get('issueDate')!.setValue(this.today);

      this.selectedSalaryCertificate = salaryCertificate;
      this.modalService.open(content).result.then(
        result => {
          this.onApproveSalaryCertificate();
        },
        reason => {
          this.resetEditForm();
        }
      );
    }
  }

  // reject(content: any, employeeNOC?: IEmployeeNOC, employmentCertificate?: IEmploymentCertificate): void {
  //   if (employeeNOC && employeeNOC.id !== undefined) {
  //     this.selectedEmployeeNoc = employeeNOC;
  //     this.selectedEmploymentCertificate = undefined;
  //     this.modalService.open(content).result.then(
  //       result => {
  //         this.onRejectEmployeeNoc();
  //       },
  //       reason => {
  //         this.resetEditForm();
  //       }
  //     );
  //   } else if (employmentCertificate && employmentCertificate !== undefined) {
  //     this.selectedEmploymentCertificate = employmentCertificate;
  //     this.selectedEmployeeNoc = undefined;
  //     this.modalService.open(content).result.then(
  //       result => {
  //         this.onRejectEmploymentCertificate();
  //       },
  //       reason => {
  //         this.resetEditForm();
  //       }
  //     );
  //   }
  // }

  onApproveEmployeeNoc(): void {
    swalConfirmationCommon().then(result => {
      if (result.isConfirmed) {
        this.employeeNocAdminService.approveEmployeeNoc(this.createCertificateApprovalDto(), this.selectedEmployeeNoc!.id!).subscribe(
          () => this.onSaveSuccess(),
          () => this.onSaveError()
        );
      }
    });
  }

  onApproveSalaryCertificate(): void {
    swalConfirmationCommon().then(result => {
      if (result.isConfirmed) {
        this.employeeSalaryCertificateAdminService
          .approveSalaryCertificate(this.createCertificateApprovalDto(), this.selectedSalaryCertificate?.id!)
          .subscribe(
            () => this.onSaveSuccess(),
            () => this.onSaveError()
          );
      }
    });
  }

  onApproveEmploymentCertificate(): void {
    swalConfirmationCommon().then(result => {
      if (result.isConfirmed) {
        this.employmentCertificateAdminService
          .approveEmploymentCertificate(this.createCertificateApprovalDto(), this.selectedEmploymentCertificate!.id!)
          .subscribe(
            () => this.onSaveSuccess(),
            () => this.onSaveError()
          );
      }
    });
  }

  // onRejectEmployeeNoc(): void {
  //   swalConfirmationCommon().then(result => {
  //     if (result.isConfirmed) {
  //       this.employeeNocAdminService.rejectEmployeeNoc(this.createCertificateApprovalDto(), this.selectedEmployeeNoc!.id!).subscribe(
  //         () => this.onSaveSuccess(),
  //         () => this.onSaveError()
  //       );
  //     }
  //   });
  // }
  //
  // onRejectEmploymentCertificate(): void {
  //   swalConfirmationCommon().then(result => {
  //     if (result.isConfirmed) {
  //       this.employmentCertificateAdminService
  //         .rejectEmploymentCertificate(this.createCertificateApprovalDto(), this.selectedEmploymentCertificate!.id!)
  //         .subscribe(
  //           () => this.onSaveSuccess(),
  //           () => this.onSaveError()
  //         );
  //     }
  //   });
  // }

  protected onSaveSuccess(): void {
    Swal.fire({
      icon: SWAL_APPROVED_ICON,
      text: 'Approved',
      timer: SWAL_APPROVE_REJECT_TIMER,
      showConfirmButton: false,
    });
    this.resetEditForm();
    this.reset();
  }

  protected swalOnRejectedSuccess(): void {
    Swal.fire({
      icon: SWAL_REJECTED_ICON,
      text: SWAL_REJECTED,
      timer: SWAL_APPROVE_REJECT_TIMER,
      showConfirmButton: false,
    });
    this.resetEditForm();
    this.reset();
  }

  protected onSaveError(): void {
    Swal.fire({
      icon: SWAL_RESPONSE_ERROR_ICON,
      title: SWAL_RESPONSE_ERROR_TITLE,
      text: SWAL_RESPONSE_ERROR_TEXT,
    });
  }

  resetEditForm(): void {
    this.editForm.get(['referenceId'])!.reset();
    this.editForm.get(['signatoryPersonId'])!.reset();
    this.editForm.get(['issueDate'])!.reset();
    this.editForm.get(['reason'])!.reset();

    this.isInvalidRefId = false;
  }

  // onChangeReferenceNumber(type: string): void {
  //   this.isInvalidRefId = false;
  //   const refNumber = this.editForm.get(['referenceId'])!.value;
  //
  //   if (type === EMPLOYMENT_CERTIFICATE) {
  //     this.employmentCertificateAdminService.isReferenceNumberUnique(refNumber).subscribe(res => {
  //       if (res.body! === true) {
  //         this.isInvalidRefId = false;
  //       } else {
  //         this.isInvalidRefId = true;
  //       }
  //     });
  //   } else if (type === NO_OBJECTION_CERTIFICATE) {
  //     this.employeeNocAdminService.isReferenceNumberUnique(refNumber).subscribe(res => {
  //       if (res.body! === true) {
  //         this.isInvalidRefId = false;
  //       } else {
  //         this.isInvalidRefId = true;
  //       }
  //     });
  //   } else if (type === EMPLOYEE_SALARY_CERTIFICATE) {
  //     this.employeeSalaryCertificateAdminService.isReferenceNumberUnique(refNumber).subscribe(res => {
  //       if (res.body! === true) {
  //         this.isInvalidRefId = false;
  //       } else {
  //         this.isInvalidRefId = true;
  //       }
  //     });
  //   }
  //
  //   this.shouldDisableApproveButton();
  // }

  onChangeSignatoryPerson(): void {
    this.shouldDisableApproveButton();
  }

  onChangeIssueDate(): void {
    this.shouldDisableApproveButton();
  }

  onChangeReason(): void {
    this.shouldDisableRejectButton();
  }

  shouldDisableApproveButton(): boolean {
    const signatoryPersonId = this.editForm.get(['signatoryPersonId'])!.value;
    const issueDate = this.editForm.get(['issueDate'])!.value;

    // if (
    //   this.searchForm.get('selectedCertificateType')!.value === EMPLOYEE_SALARY_CERTIFICATE &&
    //   signatoryPersonId !== null &&
    //   signatoryPersonId !== undefined
    // ) {
    //   return false;
    // }

    if (signatoryPersonId === null || signatoryPersonId === undefined || issueDate === null || issueDate === undefined) {
      return true;
    } else {
      return false;
    }
  }

  shouldDisableRejectButton(): boolean {
    const reason = this.editForm.get(['reason'])!.value;
    if (reason === null || reason === undefined || reason === '') {
      return true;
    } else {
      return false;
    }
  }

  monthNameNormaCapitalize(month: any): string {
    month = month.toString().toLowerCase();
    month = month.charAt(0).toUpperCase() + month.slice(1).toLowerCase();
    return month;
  }
}
