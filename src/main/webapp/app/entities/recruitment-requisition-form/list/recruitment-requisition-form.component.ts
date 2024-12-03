import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import Swal from 'sweetalert2';

import { IRecruitmentRequisitionForm } from '../recruitment-requisition-form.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, RecruitmentRequisitionFormService } from '../service/recruitment-requisition-form.service';
import { RecruitmentRequisitionFormDeleteDialogComponent } from '../delete/recruitment-requisition-form-delete-dialog.component';
import { DepartmentService } from '../../department/service/department.service';
import { EmployeeService } from '../../employee/service/employee.service';
import {
  RecruitmentRequisitionBudgetService
} from '../../recruitment-requisition-budget/service/recruitment-requisition-budget.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import {
  swalClose,
  swalOnDeleteConfirmation, swalOnDeleteSuccess,
  swalOnRequestError, swalOnRequestErrorWithBackEndErrorTitle,
  swalSuccessWithMessage
} from '../../../shared/swal-common/swal-common';
import { swalConfirmationWithMessage } from '../../../shared/swal-common/swal-confirmation.common';
import { IDepartment } from '../../department/department.model';
import { EmployeeCustomService } from '../../employee-custom/service/employee-custom.service';
import { DATE_FORMAT } from '../../../config/input.constants';
import { RequisitionStatus } from '../../enumerations/requisition-status.model';
import {
  IRecruitmentRequisitionBudget
} from '../../recruitment-requisition-budget/recruitment-requisition-budget.model';
import {
  IRrfFilterModel
} from '../../../payroll-management-system/recruitment-requisition-form/recruitment-requisition-form-filter.model';
import { IEmployee } from '../../employee/employee.model';

@Component({
  selector: 'jhi-recruitment-requisition-form',
  templateUrl: './recruitment-requisition-form.component.html',
})
export class RecruitmentRequisitionFormComponent implements OnInit, OnDestroy {
  recruitmentRequisitionForms: IRecruitmentRequisitionForm[] = [];
  recruitmentRequisitionBudgets?: IRecruitmentRequisitionBudget[] = [];
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

  editForm = this.fb.group({
    requesterId: [null],
    departmentId: [null],
    requisitionStatus: [null],
    startDate: [null],
    endDate: [null],
  });
  partiallyCloseRRFForm = this.fb.group({
    totalOnboard: [null, [Validators.required]],
  });

  totalOnboard = new FormControl(null, [Validators.required]);
  invalidOnboardedMembers = false;
  onboardedMembersExceededNumberOfVacancies = false;
  selectedRRF!: IRecruitmentRequisitionForm;

  constructor(
    protected recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected departmentService: DepartmentService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private fb: FormBuilder,
    private employeeService: EmployeeService,
    private recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService
  ) {
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'rrfNumber';
    this.ascending = true;
  }

  loadAll(): void {
    this.recruitmentRequisitionFormService.query(this.getRequestObject()).subscribe(
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

  loadAllDepartments(): void {
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
  }

  loadAllEmployeeMinimal(): void {
    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => {
      this.employees = res.body || [];
      // this.employees = this.employees.map(item => {
      //   return {
      //     id: item.id,
      //     pin: item.pin,
      //     name: item.fullName,
      //     designation: item.designationName,
      //     fullName: item.pin + '-' + item.fullName + '-' + item.designationName,
      //   };
      // });
    });
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
    /* const pageToLoad: number = page || this.page || 1;

    this.recruitmentRequisitionFormService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IRecruitmentRequisitionForm[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );*/
  }

  ngOnInit(): void {
    /*this.handleNavigation();
    this.registerChangeInRecruitmentRequisitionForms();*/
    this.loadRRFBudgets();
    this.loadAllEmployeeMinimal();
    this.loadAllDepartments();
    this.loadAll();
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
        this.recruitmentRequisitionFormService.delete(recruitmentRequisitionForm.id!).subscribe(
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

  isReadOnly(recruitmentRequisitionForm: IRecruitmentRequisitionForm): boolean {
    return !(
      recruitmentRequisitionForm.recommendationDate01 === undefined &&
      recruitmentRequisitionForm.recommendationDate02 === undefined &&
      recruitmentRequisitionForm.recommendationDate03 === undefined &&
      recruitmentRequisitionForm.recommendationDate04 === undefined
    );
  }

  navigatePrintView(id: number): void {
    sessionStorage.setItem('rrfPrintViewMidHeader', '/recruitment-requisition-form');
    this.router.navigate(['/recruitment-requisition-form', id, 'view']);
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

    this.editForm.get(['requesterId'])!.reset();
    this.editForm.get(['departmentId'])!.reset();
    this.editForm.get(['requisitionStatus'])!.reset();
    this.editForm.get(['startDate'])!.reset();
    this.editForm.get(['endDate'])!.reset();
    this.reset();
  }

  closeRRF(rrf: IRecruitmentRequisitionForm, content: any): void {
    // let rrrf:IRecruitmentRequisitionForm = this.editForm.value;
    // let content
    this.selectedRRF = rrf;
    Swal.fire({
      text: 'Please choose how you want to close the RRF.',
      confirmButtonColor: '#00a99d',
      denyButtonColor: '#49627a',
      showDenyButton: true,
      showCancelButton: true,
      confirmButtonText: 'Close : Complete',
      denyButtonText: `Close : Partial`,
      customClass: {
        confirmButton: 'btn btn-sm',
        denyButton: 'btn btn-sm',
      },
    }).then(result => {
      if (result.isConfirmed) {
        this.makeRrfFullyClosed();
      } else if (result.isDenied) {
        this.makeRrfPartiallyClosed(content);
      }
    });
  }

  makeRrfFullyClosed(): void {
    swalConfirmationWithMessage('Are you sure? Selected RRF will be closed.').then(result => {
      if (result.isConfirmed) {
        this.recruitmentRequisitionFormService.makeRrfFullyClosed(this.selectedRRF.id!).subscribe(
          res => {
            swalSuccessWithMessage('RRF Closed Successfully');
            this.reset();
          },
          err => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  makeRrfPartiallyClosed(content: any): void {
    this.modalService.open(content).result.then(
      result => {
        this.recruitmentRequisitionFormService.makeRrfPartiallyClosed(this.selectedRRF.id!, this.totalOnboard.value).subscribe(
          res => {
            swalSuccessWithMessage('RRF Closed Partially');
            this.reset();
          },
          err => {
            swalOnRequestError();
          }
        );
      },
      reason => {}
    );
  }

  onChangeTotalOnboard(): void {
    this.invalidOnboardedMembers = false;
    this.onboardedMembersExceededNumberOfVacancies = false;

    const totalOnboard = this.totalOnboard.value;
    if (Number.parseInt(totalOnboard, 10) < 0) {
      this.invalidOnboardedMembers = true;
    }
    if (Number.parseInt(totalOnboard, 10) > this.selectedRRF.numberOfVacancies!) {
      this.onboardedMembersExceededNumberOfVacancies = true;
    }
  }

  downloadReport(): void {
    const fileName = 'recruitment-requisition-report.xlsx';
    this.recruitmentRequisitionFormService.rrfExportDownload().subscribe(
      x => {
        swalClose();
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/octet-stream' });

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        /*  if (window.navigator && window.navigator.msSaveOrOpenBlob) {
           window.navigator.msSaveOrOpenBlob(newBlob, fileName);
           return;
         } */

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
      },
      // eslint-disable-next-line @typescript-eslint/no-misused-promises
      async errObj => {
        const message = JSON.parse(await errObj.error.text());
        swalOnRequestErrorWithBackEndErrorTitle(message.title, 5000);
      }
    );
  }

  loadRRFBudgets(): void {
    this.recruitmentRequisitionBudgetService.findByLoggedInUserId().subscribe((res: HttpResponse<IRecruitmentRequisitionBudget[]>) => {
      if (res.body !== null) {
        this.recruitmentRequisitionBudgets = res.body.filter(item => item.year === new Date().getFullYear());
      } else this.recruitmentRequisitionBudgets = [];
    });
  }
}
