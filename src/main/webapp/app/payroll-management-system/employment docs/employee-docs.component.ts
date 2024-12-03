import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

import { ITEMS_PER_PAGE } from '../../shared/constants/pagination.constants';
import { EmployeeNOCService } from './employee-noc/employee-noc.service';
import { EmployeeNOCDeleteDialogComponent } from './employee-noc/employee-noc-delete-dialog.component';
import {
  swalClose,
  swalOnBatchDeleteConfirmation,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading,
  swalOnRequestError,
  swalOnSavedSuccess,
  swalSuccessWithMessage,
} from 'app/shared/swal-common/swal-common';
import { swalConfirmationWithMessage } from 'app/shared/swal-common/swal-confirmation.common';
import { FormBuilder, Validators } from '@angular/forms';
import { IEmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';
import { IUser } from '../../entities/user/user.model';
import { IEmployeeNOC } from '../../shared/legacy/legacy-model/employee-noc.model';
import { IEmploymentCertificate } from '../../shared/legacy/legacy-model/employment-certificate.model';
import { ISalaryCertificate } from '../../shared/legacy/legacy-model/salary-certificate.model';
import { EmploymentCertificateService } from './employment-certificate/employment-certificate.service';
import { EmployeeSalaryCertificateService } from './employee-salary-certificate/employee-salary-certificate.service';
import { ParseLinks } from '../../core/util/parse-links.service';
import { EventManager } from '../../core/util/event-manager.service';

export const NO_OBJECTION_CERTIFICATE = 'NOC';
export const EMPLOYMENT_CERTIFICATE = 'EC';
export const SALARY_CERTIFICATE = 'SC';

type SelectableEntity = IEmployeeSalary | IUser;

@Component({
  selector: 'jhi-employee-docs',
  templateUrl: './employee-docs.component.html',
})
export class EmployeeDocsComponent implements OnInit, OnDestroy {
  employeeNOCS: IEmployeeNOC[];
  employmentCertificates: IEmploymentCertificate[] = [];
  employmentSalaryCertificates: ISalaryCertificate[] = [];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  selectedStatus = 'ALL';
  selectedEmployeeNoc!: IEmployeeNOC;
  selectedCertificateType = 'NOC';

  employeesalaries: IEmployeeSalary[] = [];

  constructor(
    protected employeeNOCService: EmployeeNOCService,
    protected employmentCertificateService: EmploymentCertificateService,
    protected employeeSalaryCertificateService: EmployeeSalaryCertificateService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected router: Router,
    protected fb: FormBuilder
  ) {
    this.employeeNOCS = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  onChangeCertificateType(type: string): void {
    if (type === NO_OBJECTION_CERTIFICATE) {
      this.selectedCertificateType = NO_OBJECTION_CERTIFICATE;
    } else if (type === SALARY_CERTIFICATE) {
      this.selectedCertificateType = SALARY_CERTIFICATE;
    } else {
      this.selectedCertificateType = EMPLOYMENT_CERTIFICATE;
    }
    this.reset();
  }

  loadAll(): void {
    if (this.selectedCertificateType === NO_OBJECTION_CERTIFICATE) {
      this.loadEmployeeNoc();
    } else if (this.selectedCertificateType === SALARY_CERTIFICATE) {
      this.loadSalaryCertificate();
    } else {
      this.loadEmploymentCertificate();
    }
  }

  loadEmployeeNoc(): void {
    this.employeeNOCService
      .query(this.getFilterReqObject())
      .subscribe((res: HttpResponse<IEmployeeNOC[]>) => this.paginateEmployeeNOCS(res.body, res.headers));
  }

  loadSalaryCertificate(): void {
    this.employeeSalaryCertificateService
      .query(this.getFilterReqObject())
      .subscribe((res: HttpResponse<ISalaryCertificate[]>) => this.paginateEmploymentSalaryCertificate(res.body, res.headers));
  }

  loadEmploymentCertificate(): void {
    this.employmentCertificateService
      .query(this.getFilterReqObject())
      .subscribe((res: HttpResponse<IEmploymentCertificate[]>) => this.paginateEmploymentCertificate(res.body, res.headers));
  }

  getFilterReqObject(): any {
    if (this.selectedStatus === 'ALL') {
      return {
        page: this.page,
        size: this.itemsPerPage,
      };
    } else {
      return {
        status: this.selectedStatus,
        page: this.page,
        size: this.itemsPerPage,
      };
    }
  }

  reset(): void {
    this.page = 0;
    this.employeeNOCS = [];
    this.employmentCertificates = [];
    this.employmentSalaryCertificates = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInEmployeeNOCS();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackNOCId(index: number, item: IEmployeeNOC): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  trackSalaryCertificateId(index: number, item: ISalaryCertificate): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  monthNameNormaCapitalize(month: any): string {
    month = month.toString().toLowerCase();
    month = month.charAt(0).toUpperCase() + month.slice(1).toLowerCase();
    return month;
  }

  registerChangeInEmployeeNOCS(): void {
    this.eventSubscriber = this.eventManager.subscribe('employeeNOCListModification', () => this.reset());
  }

  deleteNOC(employeeNOC: IEmployeeNOC): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeeNOCService.delete(employeeNOC.id!).subscribe(
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
        this.employmentCertificateService.delete(employmentCertificate.id!).subscribe(
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

  deleteEmploymentSalaryCertificate(salaryCertificate: ISalaryCertificate): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employeeSalaryCertificateService.delete(salaryCertificate.id!).subscribe(
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

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateEmployeeNOCS(data: IEmployeeNOC[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.employeeNOCS.push(data[i]);
      }
    }
  }

  protected paginateEmploymentCertificate(data: IEmploymentCertificate[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.employmentCertificates.push(data[i]);
      }
    }
  }

  protected paginateEmploymentSalaryCertificate(data: ISalaryCertificate[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.employmentSalaryCertificates.push(data[i]);
      }
    }
  }

  onStatusChange(status: string): void {
    this.selectedStatus = status;
    this.reset();
  }

  applyForEmploymentCertificate(): void {
    swalConfirmationWithMessage('Are you sure you want to apply for the employment certificate?').then(result => {
      if (result.isConfirmed) {
        this.employmentCertificateService.create().subscribe(
          res => {
            if (res.body!) {
              swalSuccessWithMessage(
                'Your application for employment certificate has been submitted. You will be notified via email once your application is approved.'
              );
              this.reset();
            }
          },
          err => swalOnRequestError()
        );
      }
    });
  }

  applyForEmployeeNOC(): void {
    swalConfirmationWithMessage('Are you sure you want to apply for the employee NOC?').then(result => {
      if (result.isConfirmed) {
        this.router.navigate(['/employee-docs/employee-noc/new']);
      }
    });
  }

  applyForSalaryCertificate(): void {
    swalConfirmationWithMessage('Are you sure you want to apply for the salary certificate?').then(result => {
      if (result.isConfirmed) {
        this.router.navigate(['/employee-docs/employee-salary-certificate/new']);
      }
    });
  }
}
