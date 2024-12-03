import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import dayjs from 'dayjs/esm';

import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { RecruitmentRequisitionFormService } from '../recruitment-requisition-form.service';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError } from '../../../shared/swal-common/swal-common';

import { RequisitionStatus } from '../../../shared/model/enumerations/requisition-status.model';
import { IRecruitmentRequisitionForm } from '../../../shared/legacy/legacy-model/recruitment-requisition-form.model';
import { EmployeeService } from '../../../entities/employee/service/employee.service';
import {
  IRecruitmentRequisitionBudget
} from '../../../entities/recruitment-requisition-budget/recruitment-requisition-budget.model';
import { IRrfFilterModel } from '../recruitment-requisition-form-filter.model';
import { DATE_FORMAT } from '../../../config/input.constants';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import {
  RecruitmentRequisitionBudgetService
} from '../../../entities/recruitment-requisition-budget/service/recruitment-requisition-budget.service';
import { FormBuilder } from '@angular/forms';

@Component({
  selector: 'jhi-user-recruitment-requisition-form',
  templateUrl: './user-recruitment-requisition-form.component.html',
})
export class UserRecruitmentRequisitionFormComponent implements OnInit, OnDestroy {
  recruitmentRequisitionForms: IRecruitmentRequisitionForm[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  links: any;

  recruitmentRequisitionBudgets?: IRecruitmentRequisitionBudget[] = [];

  editForm = this.fb.group({
    requisitionStatus: [null],
    startDate: [null],
    endDate: [null],
  });
  startDateErrMsg!: boolean;
  endDateErrMsg!: boolean;

  constructor(
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    protected recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService,
    protected fb: FormBuilder
  ) {
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.recruitmentRequisitionFormService.queryCommon(this.getRequestObject()).subscribe(
      (res: HttpResponse<IRecruitmentRequisitionForm[]>) => this.paginateRRF(res.body, res.headers),
      () => this.onError()
    );
    this.loadRRFBudgets();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IRecruitmentRequisitionForm): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  delete(recruitmentRequisitionForm: IRecruitmentRequisitionForm): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.recruitmentRequisitionFormService.deleteCommon(recruitmentRequisitionForm.id!).subscribe(
          () => {
            this.reset();
            this.loadRRFBudgets();
            swalOnDeleteSuccess();
          },
          () => swalOnRequestError()
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

  protected onSuccess(data: IRecruitmentRequisitionForm[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/recruitment-requisition-form/user'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.recruitmentRequisitionForms = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  paginateRRF(data: IRecruitmentRequisitionForm[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.recruitmentRequisitionForms.push(data[i]);
      }
    }
  }

  reset(): void {
    this.page = 0;
    this.recruitmentRequisitionForms = [];
    this.loadPage(this.page);
  }

  getStatus(recruitmentRequisitionForm: IRecruitmentRequisitionForm): string {
    if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.PENDING) {
      return 'Pending';
    } else if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.LM_APPROVED) {
      return 'LM Approved';
    } else if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.HOD_APPROVED) {
      return 'HoD Approved';
    } else if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.CTO_APPROVED) {
      return 'CTO Approved';
    } else if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.HOHR_VETTED) {
      return 'HoHR Vetted';
    } else if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.CEO_APPROVED) {
      return 'CEO Approved';
    } else if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.PARTIALLY_CLOSED) {
      return 'Partially Closed';
    } else if (recruitmentRequisitionForm.requisitionStatus === RequisitionStatus.CLOSED) {
      return 'Closed';
    } else return 'Not Approved';
  }

  getApprovedDate(recruitmentRequisitionForm: IRecruitmentRequisitionForm): dayjs.Dayjs {
    if (recruitmentRequisitionForm.recommendationDate04 !== null || recruitmentRequisitionForm.recommendationDate04 !== undefined) {
      return recruitmentRequisitionForm.recommendationDate04!;
    } else if (recruitmentRequisitionForm.recommendationDate03 !== null || recruitmentRequisitionForm.recommendationDate03 !== undefined) {
      return recruitmentRequisitionForm.recommendationDate03!;
    } else if (recruitmentRequisitionForm.recommendationDate02 !== null || recruitmentRequisitionForm.recommendationDate02 !== undefined) {
      return recruitmentRequisitionForm.recommendationDate02!;
    } else if (recruitmentRequisitionForm.recommendationDate01 !== null || recruitmentRequisitionForm.recommendationDate01 !== undefined) {
      return recruitmentRequisitionForm.recommendationDate01!;
    } else {
      return dayjs();
    }
  }

  navigatePrintView(id: number): void {
    sessionStorage.setItem('rrfPrintViewMidHeader', '/recruitment-requisition-form/user');
    this.router.navigate(['/recruitment-requisition-form/user', id, 'view']);
  }

  getRequestObject(): IRrfFilterModel {
    const startDateDP = this.editForm.get(['startDate'])!.value;
    const endDateDP = this.editForm.get(['endDate'])!.value;
    const requisitionStatus = this.editForm.get(['requisitionStatus'])!.value;

    const obj: IRrfFilterModel = {
      page: this.page,
      size: this.itemsPerPage,
      sort: this.sort(),
    };

    if (requisitionStatus !== null && requisitionStatus !== undefined && requisitionStatus !== 'null') {
      obj['requisitionStatus'] = requisitionStatus;
    }
    if (startDateDP !== null && startDateDP !== undefined && endDateDP !== null && endDateDP !== undefined) {
      obj['startDate'] = startDateDP.format(DATE_FORMAT);
      obj['endDate'] = endDateDP.format(DATE_FORMAT);
    }
    return obj;
  }

  loadRrfUsingFilters(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;

    this.startDateErrMsg = false;
    this.endDateErrMsg = false;

    if ((startDate === null || startDate === undefined) && endDate !== null && endDate !== undefined) {
      this.startDateErrMsg = true;
      this.endDateErrMsg = false;
    } else if (startDate !== null && startDate !== undefined && (endDate === null || endDate === undefined)) {
      this.startDateErrMsg = false;
      this.endDateErrMsg = true;
    }

    if ((startDate === null || startDate === undefined) && (endDate === null || endDate === undefined)) {
      this.reset();
    } else if (startDate !== null && startDate !== undefined && endDate !== null && endDate !== undefined) {
      this.reset();
    }
  }

  resetFilter(): void {
    this.startDateErrMsg = false;
    this.endDateErrMsg = false;

    this.editForm.get(['requisitionStatus'])!.reset();
    this.editForm.get(['startDate'])!.reset();
    this.editForm.get(['endDate'])!.reset();
    this.reset();
  }

  loadRRFBudgets(): void {
    this.recruitmentRequisitionBudgetService.findByLoggedInUserId().subscribe((res: HttpResponse<IRecruitmentRequisitionBudget[]>) => {
      if (res.body !== null) {
        this.recruitmentRequisitionBudgets = res.body.filter(item => item.year === new Date().getFullYear());
      } else this.recruitmentRequisitionBudgets = [];
    });
  }
}
