import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbDateStruct, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';

import { ITEMS_PER_PAGE } from '../../../shared/constants/pagination.constants';
import { RecruitmentRequisitionFormService } from '../recruitment-requisition-form.service';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError } from '../../../shared/swal-common/swal-common';
import { IRrfFilterModel } from '../recruitment-requisition-form-filter.model';
import { RequisitionStatus } from '../../../shared/model/enumerations/requisition-status.model';
import { IRecruitmentRequisitionForm } from '../../../shared/legacy/legacy-model/recruitment-requisition-form.model';
import { IDepartment } from '../../../shared/legacy/legacy-model/department.model';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { DepartmentService } from '../../../shared/legacy/legacy-service/department.service';
import { DATE_FORMAT } from '../../../config/input.constants';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import {
  RecruitmentRequisitionBudgetService
} from '../../../entities/recruitment-requisition-budget/service/recruitment-requisition-budget.service';
import {
  IRecruitmentRequisitionBudget
} from '../../../entities/recruitment-requisition-budget/recruitment-requisition-budget.model';

@Component({
  selector: 'jhi-recruitment-requisition-form',
  templateUrl: './recruitment-requisition-form-raise-on-behalf.component.html',
})
export class RecruitmentRequisitionFormRaiseOnBehalfComponent implements OnInit, OnDestroy {
  recruitmentRequisitionForms: IRecruitmentRequisitionForm[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  links: any;
  departments!: IDepartment[] | [];
  employees!: IEmployee[];
  startDateErrMsg!: boolean;
  endDateErrMsg!: boolean;

  minDateForDP: NgbDateStruct;
  maxDateForDP: NgbDateStruct;

  editForm = this.fb.group({
    requesterId: [null],
    departmentId: [null],
    requisitionStatus: [null],
    startDate: [null],
    endDate: [null],
  });

  recruitmentRequisitionBudgets: IRecruitmentRequisitionBudget[] = [];

  constructor(
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected employeeService: EmployeeService,
    protected departmentService: DepartmentService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private fb: FormBuilder,
    protected recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService
  ) {
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'rrfNumber';
    this.ascending = true;

    const today = new Date();
    this.minDateForDP = {
      year: today.getFullYear() - 1,
      month: today.getMonth() + 1,
      day: today.getDate(),
    };

    this.maxDateForDP = {
      year: today.getFullYear(),
      month: today.getMonth() + 1,
      day: today.getDate(),
    };
  }

  loadAll(): void {
    this.recruitmentRequisitionFormService.queryOnBehalfCommon(this.getRequestObject()).subscribe(
      (res: HttpResponse<IRecruitmentRequisitionForm[]>) => this.paginateRRF(res.body, res.headers),
      () => this.onError()
    );
  }

  getRequestObject(): IRrfFilterModel {
    const startDateDP = this.editForm.get(['startDate'])!.value;
    const endDateDP = this.editForm.get(['endDate'])!.value;
    const requesterId = this.editForm.get(['requesterId'])!.value;
    const departmentId = this.editForm.get(['departmentId'])!.value;
    const requisitionStatus = this.editForm.get(['requisitionStatus'])!.value;

    const obj: IRrfFilterModel = {
      page: this.page,
      size: this.itemsPerPage,
      sort: this.sort(),
    };

    if (requesterId !== null && requesterId !== undefined) {
      obj['requesterId'] = requesterId;
    }
    if (departmentId !== null && departmentId !== undefined && departmentId !== 'null') {
      obj['departmentId'] = departmentId;
    }
    if (requisitionStatus !== null && requisitionStatus !== undefined && requisitionStatus !== 'null') {
      obj['requisitionStatus'] = requisitionStatus;
    }
    if (startDateDP !== null && startDateDP !== undefined && endDateDP !== null && endDateDP !== undefined) {
      obj['startDate'] = startDateDP.format(DATE_FORMAT);
      obj['endDate'] = endDateDP.format(DATE_FORMAT);
    }
    // if(endDateDP !== null && endDateDP !== undefined){
    //   obj['endDate'] = endDateDP.format(DATE_FORMAT);
    // }

    return obj;
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadRRFBudgets();
    this.loadAllDepartments();
    this.loadAllEmployeeMinimal();
    this.loadAll();
  }

  loadAllEmployeeMinimal(): void {
    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => {
      this.employees = res.body || [];
      this.employees = this.employees.map(item => {
        return {
          id: item.id,
          pin: item.pin,
          name: item.fullName,
          designation: item.designationName,
          fullName: item.pin + '-' + item.fullName + '-' + item.designationName,
        };
      });
    });
  }

  loadAllDepartments(): void {
    this.departmentService.commonQuery().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
  }

  protected handleNavigation(): void {
    combineLatest(this.activatedRoute.data, this.activatedRoute.queryParamMap, (data: Data, params: ParamMap) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get('sort') ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === 'asc';
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber);
      }
    }).subscribe();
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

  registerChangeInRecruitmentRequisitionForms(): void {
    this.eventSubscriber = this.eventManager.subscribe('recruitmentRequisitionFormListModification', () => this.reset());
  }

  delete(recruitmentRequisitionForm: IRecruitmentRequisitionForm): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.recruitmentRequisitionFormService.deleteOnBehalfCommon(recruitmentRequisitionForm.id!).subscribe(
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
    if (this.predicate !== 'rrfNumber') {
      result.push('rrfNumber');
    }
    return result;
  }

  protected onSuccess(data: IRecruitmentRequisitionForm[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/recruitment-requisition-form'], {
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

  navigatePrintView(id: number): void {
    sessionStorage.setItem('rrfPrintViewMidHeader', '/recruitment-requisition-form/raise-on-behalf');
    this.router.navigate(['/recruitment-requisition-form', 'raise-on-behalf', id, 'view']);
  }

  loadRrfUsingFilters(): void {
    const startDateDP = this.editForm.get(['startDate'])!.value;
    const endDateDP = this.editForm.get(['endDate'])!.value;

    this.startDateErrMsg = false;
    this.endDateErrMsg = false;

    if ((startDateDP === null || startDateDP === undefined) && endDateDP !== null && endDateDP !== undefined) {
      this.startDateErrMsg = true;
      this.endDateErrMsg = false;
    } else if (startDateDP !== null && startDateDP !== undefined && (endDateDP === null || endDateDP === undefined)) {
      this.startDateErrMsg = false;
      this.endDateErrMsg = true;
    }

    this.reset();
  }

  resetFilter(): void {
    this.startDateErrMsg = false;
    this.endDateErrMsg = false;

    this.editForm.get(['requesterId'])!.reset();
    this.editForm.get(['departmentId'])!.reset();
    this.editForm.get(['requisitionStatus'])!.reset();
    this.editForm.get(['startDate'])!.reset();
    this.editForm.get(['endDate'])!.reset();
    this.reset();
  }

  loadRRFBudgets(): void {
    this.recruitmentRequisitionBudgetService.findByLoggedInUserId().subscribe((res) => {
      if (res.body !== null) {
        this.recruitmentRequisitionBudgets = res.body.filter(item => item.year === new Date().getFullYear());
      } else this.recruitmentRequisitionBudgets = [];
    });
  }
}
