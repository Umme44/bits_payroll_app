import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { FormBuilder, FormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';

import Swal from 'sweetalert2';
import { IInsuranceConfiguration } from '../../insurance-configuration/insurance-configuration.model';
import { IInsuranceRegistration } from '../insurance-registration.model';
import { InsuranceRegistrationService } from '../service/insurance-registration.service';
import { UserInsuranceService } from '../../../payroll-management-system/insurance-profile/user-insurance.service';
import { DataUtils } from '../../../core/util/data-util.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { InsuranceRegistrationDeleteDialogComponent } from '../delete/insurance-registration-delete-dialog.component';
import {
  swalClose,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading,
  swalOnRequestError,
} from '../../../shared/swal-common/swal-common';
import { swalConfirmationCommon } from '../../../shared/swal-common/swal-confirmation.common';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_REJECTED,
  SWAL_REJECTED_ICON,
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
  SWAL_RESPONSE_ERROR_TITLE,
} from '../../../shared/swal-common/swal.properties.constant';
import { InsuranceRelation } from '../../../shared/model/enumerations/insurance-relation.model';
import { IInsuranceRegistrationAdmin } from '../insurance-registration-admin-model';
import { IInsuranceApprovalDto, InsuranceApprovalDto } from '../insurance-approval.model';
import {CustomValidator} from "../../../validators/custom-validator";

@Component({
  selector: 'jhi-insurance-registration',
  templateUrl: './insurance-registration.component.html',
  styleUrls: ['insurance-registration.component.scss'],
})
export class InsuranceRegistrationComponent implements OnInit, OnDestroy {
  insuranceRegistrations: IInsuranceRegistrationAdmin[];
  insuranceConfiguration!: IInsuranceConfiguration;
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  selectedInsuranceRegistration!: IInsuranceRegistration;

  selectedStatus = 'ALL';
  searchText = '';
  isCanceled = false;
  isSeperated = false;
  selectedYear = 0;
  selectedMonth = 0;

  insuranceId = new FormControl(null);
  reason = new FormControl(null);
  inValidInsuranceId = false;

  currentYear: number = new Date().getFullYear();
  years: number[];

  months = [
    { Value: 1, Text: 'January' },
    { Value: 2, Text: 'February' },
    { Value: 3, Text: 'March' },
    { Value: 4, Text: 'April' },
    { Value: 5, Text: 'May' },
    { Value: 6, Text: 'June' },
    { Value: 7, Text: 'July' },
    { Value: 8, Text: 'August' },
    { Value: 9, Text: 'September' },
    { Value: 10, Text: 'October' },
    { Value: 11, Text: 'November' },
    { Value: 12, Text: 'December' },
  ];

  constructor(
    protected insuranceRegistrationService: InsuranceRegistrationService,
    protected userInsuranceService: UserInsuranceService,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected fb: FormBuilder,
    private router: Router
  ) {
    this.insuranceRegistrations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;

    this.years = [
      this.currentYear + 1,
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
    ];
  }

  loadAll(): void {
    this.insuranceRegistrationService
      .getAllInsuranceRegistrations(this.getInsuranceFilterReqObject())
      .subscribe((res: HttpResponse<IInsuranceRegistrationAdmin[]>) => {
        this.paginateInsuranceRegistrations(res.body, res.headers);
      });
  }

  getInsuranceFilterReqObject(): any {
    if (this.selectedStatus === 'ALL') {
      this.selectedYear = 0;
      this.selectedMonth = 0;
      return {
        searchText: this.searchText,
        year: this.selectedYear,
        month: this.selectedMonth,
        isCancelled: false,
        isSeperated: false,
        isExcluded: false,
        page: this.page,
        size: this.itemsPerPage,
      };
    } else if (this.selectedStatus === 'EXCLUDED') {
      return {
        searchText: this.searchText,
        year: this.selectedYear,
        month: this.selectedMonth,
        isCancelled: this.isCanceled,
        isSeperated: this.isSeperated,
        isExcluded: true,
        page: this.page,
        size: this.itemsPerPage,
      };
    } else {
      return {
        searchText: this.searchText,
        year: this.selectedYear,
        month: this.selectedMonth,
        status: this.selectedStatus,
        isCancelled: false,
        isSeperated: false,
        isExcluded: false,
        page: this.page,
        size: this.itemsPerPage,
      };
    }
  }

  getInsuranceConfiguration(): void {
    this.userInsuranceService.queryInsuranceConfiguration().subscribe(res => {
      this.insuranceConfiguration = res.body!;
    });
  }

  onSearchTextChange(event: any): void {
    this.searchText = event.target.value;
    this.reset();
  }

  onStatusChange(status: string): void {
    if (status === 'EXCLUDED') {
      this.isSeperated = true;
      this.isCanceled = true;
    } else {
      this.isSeperated = false;
      this.isCanceled = false;
    }

    this.selectedStatus = status;
    this.reset();
  }

  onChangeExclusionCriteria(event: any): void {
    this.isCanceled = false;
    this.isSeperated = false;

    const category = event.target.value;

    if (category === 'CANCELLED') {
      this.isCanceled = true;
      this.isSeperated = false;
    } else if (category === 'SEPARATED') {
      this.isCanceled = false;
      this.isSeperated = true;
    } else {
      this.isCanceled = true;
      this.isSeperated = true;
    }
    this.reset();
  }

  isSearchTextInvalid = false;
  reset(): void {
    if(!CustomValidator.NATURAL_TEXT_PATTERN.test(this.searchText)){
      this.isSearchTextInvalid = true;
    }
    else{
      this.isSearchTextInvalid = false;
      this.page = 0;
      this.insuranceRegistrations = [];
      this.loadAll();
    }
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInInsuranceRegistrations();
    this.getInsuranceConfiguration();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IInsuranceRegistration): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInInsuranceRegistrations(): void {
    this.eventSubscriber = this.eventManager.subscribe('insuranceRegistrationListModification', () => this.reset());
  }

  delete(insuranceRegistration: IInsuranceRegistration): void {
    const modalRef = this.modalService.open(InsuranceRegistrationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.insuranceRegistration = insuranceRegistration;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  deleteRegisteredPerson(id: number): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.insuranceRegistrationService.delete(id).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.loadAll();
          },
          () => this.requestFailed()
        );
      }
    });
  }
  requestFailed(): void {
    swalOnRequestError();
  }

  redirectToClaimUrl(): void {
    // window.location.href = this.insuranceConfiguration.insuranceClaimLink!;
    (window as any).open(this.insuranceConfiguration.insuranceClaimLink!, '_blank');
  }

  protected paginateInsuranceRegistrations(data: IInsuranceRegistrationAdmin[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.insuranceRegistrations.push(data[i]);
      }
    }
  }

  onChangeInsuranceCardId(): void {
    this.inValidInsuranceId = false;
    const cardId = this.insuranceId.value;
    if (cardId !== null && cardId !== undefined && cardId !== '') {
      this.insuranceRegistrationService.isCardIdUnique(cardId).subscribe(res => {
        if (res.body! === true) {
          this.inValidInsuranceId = false;
        } else {
          this.inValidInsuranceId = true;
        }
      });
    }
  }

  approve(content: any, selectedInsuranceRegistration: IInsuranceRegistration): void {
    this.selectedInsuranceRegistration = selectedInsuranceRegistration;
    this.modalService.open(content).result.then(
      result => {
        this.onApproveInsuranceRegistration();
      },
      reason => {
        this.insuranceId.reset();
      }
    );
  }

  reject(content: any, selectedInsuranceRegistration: IInsuranceRegistration): void {
    this.selectedInsuranceRegistration = selectedInsuranceRegistration;
    this.modalService.open(content).result.then(
      result => {
        this.onRejectInsuranceRegistration();
      },
      reason => {
        this.reason.reset();
      }
    );
  }

  cancel(content: any, selectedInsuranceRegistration: IInsuranceRegistration): void {
    this.selectedInsuranceRegistration = selectedInsuranceRegistration;
    this.modalService.open(content).result.then(
      result => {
        this.onCancelInsuranceRegistration();
      },
      reason => {
        this.reason.reset();
      }
    );
  }

  onApproveInsuranceRegistration(): void {
    swalConfirmationCommon().then(result => {
      if (result.isConfirmed) {
        this.insuranceRegistrationService.approveInsuranceRegistration(this.createInsuranceApprovalDto()).subscribe(
          () => this.onApprovedSuccess(),
          () => this.onSaveError()
        );
      }
    });
  }

  onRejectInsuranceRegistration(): void {
    swalConfirmationCommon().then(result => {
      if (result.isConfirmed) {
        this.insuranceRegistrationService.rejectInsuranceRegistration(this.createInsuranceApprovalDto()).subscribe(
          () => this.swalOnRejectedSuccess(),
          () => this.onSaveError()
        );
      }
    });
  }

  onCancelInsuranceRegistration(): void {
    swalConfirmationCommon().then(result => {
      if (result.isConfirmed) {
        this.insuranceRegistrationService.cancelInsuranceRegistration(this.createInsuranceApprovalDto()).subscribe(
          () => this.swalOnCancelledSuccess(),
          () => this.onSaveError()
        );
      }
    });
  }

  protected onApprovedSuccess(): void {
    Swal.fire({
      icon: SWAL_APPROVED_ICON,
      text: 'Approved',
      timer: SWAL_APPROVE_REJECT_TIMER,
      showConfirmButton: false,
    });
    this.insuranceId.reset();
    this.reason.reset();
    this.reset();
  }

  protected swalOnRejectedSuccess(): void {
    Swal.fire({
      icon: SWAL_REJECTED_ICON,
      text: SWAL_REJECTED,
      timer: SWAL_APPROVE_REJECT_TIMER,
      showConfirmButton: false,
    });
    this.insuranceId.reset();
    this.reason.reset();
    this.reset();
  }

  protected swalOnCancelledSuccess(): void {
    Swal.fire({
      icon: SWAL_REJECTED_ICON,
      text: 'Cancelled',
      timer: SWAL_APPROVE_REJECT_TIMER,
      showConfirmButton: false,
    });
    this.insuranceId.reset();
    this.reason.reset();
    this.reset();
  }

  protected onSaveError(): void {
    Swal.fire({
      icon: SWAL_RESPONSE_ERROR_ICON,
      title: SWAL_RESPONSE_ERROR_TITLE,
      text: SWAL_RESPONSE_ERROR_TEXT,
    });
  }

  createInsuranceApprovalDto(): IInsuranceApprovalDto {
    return {
      ...new InsuranceApprovalDto(),
      insuranceCardId: this.insuranceId.value ?? null,
      registrationId: this.selectedInsuranceRegistration.id,
      status: this.selectedInsuranceRegistration.insuranceStatus,
      reason: this.reason.value ?? null,
    };
  }

  getInsuranceRelationName(relationName: string): string {
    if (relationName === InsuranceRelation.SELF) {
      return 'Self';
    } else if (relationName === InsuranceRelation.SPOUSE) {
      return 'Spouse';
    } else if (relationName === InsuranceRelation.CHILD_1) {
      return 'Child 1';
    } else if (relationName === InsuranceRelation.CHILD_2) {
      return 'Child 2';
    } else {
      return 'Child 3';
    }
  }

  shouldEnableClaimButtonColumn(): boolean {
    if (this.selectedStatus === 'ALL' || this.selectedStatus === 'APPROVED') {
      return true;
    } else {
      return false;
    }
  }

  shouldEnableCancelButtonColumn(): boolean {
    if (this.selectedStatus === 'APPROVED' || this.selectedStatus === 'PENDING') {
      return true;
    } else {
      return false;
    }
  }

  shouldEnableEditViewDeleteColumn(): boolean {
    if (this.selectedStatus !== 'EXCLUDED') {
      return true;
    } else {
      return false;
    }
  }

  shouldEnableApprovalButtonsColumn(): boolean {
    if (this.selectedStatus === 'PENDING') {
      return true;
    } else {
      return false;
    }
  }

  exportInclusionList(): void {
    swalOnLoading('loading..');
    const fileName = 'Insurance_Registration_Inclusion_List.xlsx';

    this.insuranceRegistrationService
      .exportInclusionList({
        searchText: this.searchText,
        year: this.selectedYear,
        month: this.selectedMonth,
      })
      .subscribe(
        x => {
          const newBlob = new Blob([x], { type: 'application/octet-stream' });

          // IE doesn't allow using a blob object directly as link href
          // instead it is necessary to use msSaveOrOpenBlob
          if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
            (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
            return;
          }

          // For other browsers:
          // Create a link pointing to the ObjectURL containing the blob.
          const data = window.URL.createObjectURL(newBlob);

          const link = document.createElement('a');
          link.href = data;
          link.download = fileName;
          // this is necessary as link.click() does not work on the latest firefox
          link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
          // tslint:disable-next-line:typedef
          setTimeout(function () {
            // For Firefox it is necessary to delay revoking the ObjectURL
            window.URL.revokeObjectURL(data);
            link.remove();
          }, 100);
          swalClose();
        },
        error => {
          swalClose();
          swalOnRequestError();
        }
      );
  }

  exportApprovedList(): void {
    swalOnLoading('loading..');
    const fileName = 'Insurance_Registration_Approved_List.xlsx';

    this.insuranceRegistrationService
      .exportApprovedList({
        searchText: this.searchText,
        year: this.selectedYear,
        month: this.selectedMonth,
      })
      .subscribe(
        x => {
          const newBlob = new Blob([x], { type: 'application/octet-stream' });

          // IE doesn't allow using a blob object directly as link href
          // instead it is necessary to use msSaveOrOpenBlob
          if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
            (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
            return;
          }

          // For other browsers:
          // Create a link pointing to the ObjectURL containing the blob.
          const data = window.URL.createObjectURL(newBlob);

          const link = document.createElement('a');
          link.href = data;
          link.download = fileName;
          // this is necessary as link.click() does not work on the latest firefox
          link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
          // tslint:disable-next-line:typedef
          setTimeout(function () {
            // For Firefox it is necessary to delay revoking the ObjectURL
            window.URL.revokeObjectURL(data);
            link.remove();
          }, 100);
          swalClose();
        },
        error => {
          swalClose();
          swalOnRequestError();
        }
      );
  }

  exportExcludedList(): void {
    swalOnLoading('loading..');

    let fileName: string;

    if (this.isCanceled && !this.isSeperated) {
      fileName = 'Insurance_Registration_Cancelled_List.xlsx';
    } else if (!this.isCanceled && this.isSeperated) {
      fileName = 'Insurance_Registration_Seperated_List.xlsx';
    } else {
      fileName = 'Insurance_Registration_Excluded_List.xlsx';
    }

    this.insuranceRegistrationService
      .exportExcludedList({
        searchText: this.searchText,
        year: this.selectedYear,
        month: this.selectedMonth,
        isCancelled: this.isCanceled,
        isSeperated: this.isSeperated,
      })
      .subscribe(
        x => {
          const newBlob = new Blob([x], { type: 'application/octet-stream' });

          // IE doesn't allow using a blob object directly as link href
          // instead it is necessary to use msSaveOrOpenBlob
          if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
            (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
            return;
          }

          // For other browsers:
          // Create a link pointing to the ObjectURL containing the blob.
          const data = window.URL.createObjectURL(newBlob);

          const link = document.createElement('a');
          link.href = data;
          link.download = fileName;
          // this is necessary as link.click() does not work on the latest firefox
          link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
          // tslint:disable-next-line:typedef
          setTimeout(function () {
            // For Firefox it is necessary to delay revoking the ObjectURL
            window.URL.revokeObjectURL(data);
            link.remove();
          }, 100);
          swalClose();
        },
        error => {
          swalClose();
          swalOnRequestError();
        }
      );
  }

  onChangeYear(event: any): void {
    this.selectedYear = Number.parseInt(event.target.value, 10);

    if (this.selectedYear === 0) {
      this.selectedMonth = 0;
    }

    this.reset();
  }

  onChangeMonth(event: any): void {
    this.selectedMonth = Number.parseInt(event.target.value, 10);
    this.reset();
  }
}
