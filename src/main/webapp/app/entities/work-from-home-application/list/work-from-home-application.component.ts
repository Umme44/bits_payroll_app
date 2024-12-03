import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IWorkFromHomeApplication} from "../work-from-home-application.model";
import dayjs from 'dayjs/esm';
import {WorkFromHomeApplicationService} from "../service/work-from-home-application.service";
import {DataUtils} from "../../../core/util/data-util.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {
  swalForWarningWithMessage,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestError
} from "../../../shared/swal-common/swal-common";

@Component({
  selector: 'jhi-work-from-home-application',
  templateUrl: './work-from-home-application.component.html',
})
export class WorkFromHomeApplicationComponent implements OnInit, OnDestroy {
  workFromHomeApplications: IWorkFromHomeApplication[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  today = new Date();
  monthFromToday!: Date;

  startRange?: dayjs.Dayjs | null = null;
  endRange?: dayjs.Dayjs | null = null;
  startDateDp: any;
  endDateDp: any;
  isInvalid = false;
  employeeId?: any | null = null;

  startDateValidation = false;
  endDateValidation = false;

  editForm = this.fb.group({
    startDate: new FormControl('', []),
    endDate: new FormControl('', []),
    employeeId: this.fb.group({
      employeeId: new FormControl('', []),
    }),
    status: new FormControl('', []),
  });

  constructor(
    protected workFromHomeApplicationService: WorkFromHomeApplicationService,
    protected dataUtils: DataUtils,
    private fb: FormBuilder,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {
    this.monthFromToday = new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000);
    this.predicate = 'startDate';
    this.ascending = false;
  }

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const startDate = this.editForm.get('startDate')!.value;
    const endDate = this.editForm.get('endDate')!.value;

    if (startDate === null) {
      this.startDateValidation = false;
      if (endDate === '' || endDate === null) {
        this.endDateValidation = false;
      } else {
        this.startDateValidation = true;
        this.endDateValidation = false;
      }
    } else if (startDate !== '') {
      this.startDateValidation = false;
      if (endDate === '' || endDate === null) {
        this.endDateValidation = true;
      } else {
        this.endDateValidation = false;
      }
    }

    this.employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    this.startRange = this.editForm.get(['startDate'])!.value;
    this.endRange = this.editForm.get(['endDate'])!.value;
    const startDate1: string = this.editForm.get('startDate')!.value
      ? dayjs(this.editForm.get('startDate')!.value).format('YYYY-MM-DD')
      : '';
    const endDate1: string = this.editForm.get('endDate')!.value ? dayjs(this.editForm.get('endDate')!.value).format('YYYY-MM-DD') : '';
    const status1 = this.editForm.get(['status'])!.value;
    const pageToLoad: number = this.page || 1;

    if (this.startDateValidation === false && this.endDateValidation === false) {
      this.workFromHomeApplicationService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          employeeId: this.employeeId ?? '',
          startDate: startDate1,
          endDate: endDate1,
          status: status1,
        })
        .subscribe(
          (res: HttpResponse<IWorkFromHomeApplication[]>) => this.onSuccess(res.body, res.headers, pageToLoad, true),
          () => this.onError()
        );
    }
  }

  ngOnInit(): void {
    this.registerChangeInLeaveApplications();
    this.loadPage();
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
        this.loadPage(pageNumber, true);
      }
    }).subscribe();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IWorkFromHomeApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInLeaveApplications(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveApplicationListModification', () => this.search());
  }

  delete(workFromHomeApplication: IWorkFromHomeApplication): void {
    if (workFromHomeApplication.id) {
      swalOnDeleteConfirmation().then(result => {
        if (result.isConfirmed) {
          this.workFromHomeApplicationService.delete(workFromHomeApplication.id!).subscribe(
            () => {
              swalOnDeleteSuccess();
              this.search();
            },
            () => swalOnRequestError()
          );
        }
      });
    } else {
      swalForWarningWithMessage('Id is null!');
    }
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'startDate') {
      result.push('startDate');
    }
    return result;
  }

  checkDate(): void {
    const startDate1 = this.editForm.get('startDate')!.value;
    const endDate2 = this.editForm.get('endDate')!.value;
    this.isInvalid = false;

    if (startDate1 === null && endDate2 === null) {
      this.search();
    }
    if (startDate1 === null) {
      this.startDateValidation = false;
      if (endDate2 === '' || endDate2 === null) {
        this.endDateValidation = false;
      } else {
        this.startDateValidation = true;
        this.endDateValidation = false;
        return;
      }
    } else if (startDate1 !== '') {
      this.startDateValidation = false;
      if (endDate2 === '' || endDate2 === null) {
        this.endDateValidation = true;
        return;
      } else {
        this.endDateValidation = false;
      }
    }

    const startDate = this.editForm.get(['startDate'])!.value.format('YYYY-MM-DD');
    const endDate = this.editForm.get(['endDate'])!.value.format('YYYY-MM-DD');

    if (startDate && endDate && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
      this.page = 0;
      this.search();
    }
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  protected onSuccess(data: IWorkFromHomeApplication[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/work-from-home-application'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.workFromHomeApplications = data || [];
    this.ngbPaginationPage = this.page;
  }

  changeEmployeeId(event: any): void {
    this.workFromHomeApplications = [];
    this.search();
  }

  search(): void {
    const startDate = this.editForm.get('startDate')!.value;
    const endDate = this.editForm.get('endDate')!.value;

    if (startDate === null) {
      this.startDateValidation = false;
      if (endDate === '' || endDate === null) {
        this.endDateValidation = false;
      } else {
        this.startDateValidation = true;
        this.endDateValidation = false;
      }
    } else if (startDate !== '') {
      this.startDateValidation = false;
      if (endDate === '' || endDate === null) {
        this.endDateValidation = true;
      } else {
        this.endDateValidation = false;
      }
    }

    this.employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    this.startRange = this.editForm.get(['startDate'])!.value;
    this.endRange = this.editForm.get(['endDate'])!.value;
    const startDate1: string = this.editForm.get('startDate')!.value
      ? dayjs(this.editForm.get('startDate')!.value).format('YYYY-MM-DD')
      : '';
    const endDate1: string = this.editForm.get('endDate')!.value ? dayjs(this.editForm.get('endDate')!.value).format('YYYY-MM-DD') : '';
    const status1 = this.editForm.get(['status'])!.value;

    if (this.startDateValidation === false && this.endDateValidation === false) {
      this.workFromHomeApplications = [];
      this.page = 0;
      this.loadPage();
    }
  }
}
