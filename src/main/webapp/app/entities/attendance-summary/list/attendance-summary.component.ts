import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';
import Swal from 'sweetalert2';
import dayjs from 'dayjs/esm';

import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { IAttendanceSummary } from '../attendance-summary.model';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { AttendanceSummaryService } from '../service/attendance-summary.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { EmployeeService } from '../../employee/service/employee.service';
import {
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DELETE_CONFIRMATION,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';
import { swalOnDeleteSuccess, swalOnRequestError } from '../../../shared/swal-common/swal-common';
import { SalaryLockService } from '../../../shared/legacy/legacy-service/salary-lock-service';
import { AttendanceSummaryFilterDto } from '../filter.model';
import { AttendanceSummarySearchService } from '../service/attendance-summary-search.service';

@Component({
  selector: 'jhi-attendance-summary',
  templateUrl: './attendance-summary.component.html',
})
export class AttendanceSummaryComponent implements OnInit, OnDestroy {
  attendanceSummaries?: IAttendanceSummary[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  isSalaryLocked!: boolean;

  isViewByYearMonth = false;
  filterDto = new AttendanceSummaryFilterDto();
  year = 0;
  month = 0;

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

  searchParamsForm = this.fb.group({
    month: [0],
    year: [0],
    searchText: [''],
  });

  constructor(
    protected attendanceSummaryService: AttendanceSummaryService,
    protected attendanceSummarySearchService: AttendanceSummarySearchService,
    protected salaryLockService: SalaryLockService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute,
    protected employeeService: EmployeeService,
    protected router: Router,
    protected fb: FormBuilder
  ) {
    this.years = [
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
    ];
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    if (this.year !== 0 && this.month !== 0) {
      this.isViewByYearMonth = true;
      // this.attendanceSummaryService;
      this.attendanceSummaryService
        .queryWithParams(this.year, this.month, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          searchText: this.searchParamsForm.get('searchText')!.value,
        })
        .subscribe(
          (res: HttpResponse<IAttendanceSummary[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
    } else {
      this.isViewByYearMonth = false;
      this.attendanceSummaryService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          searchText: this.searchParamsForm.get('searchText')!.value,
          month: this.searchParamsForm.get('month')!.value,
          year: this.searchParamsForm.get('year')!.value,
        })
        .subscribe(
          (res: HttpResponse<IAttendanceSummary[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
    }
  }

  ngOnInit(): void {
    this.year = this.activatedRoute.snapshot.params['year'] === undefined ? 0 : this.activatedRoute.snapshot.params['year'];
    this.month = this.activatedRoute.snapshot.params['month'] === undefined ? 0 : this.activatedRoute.snapshot.params['month'];

    this.handleNavigation();
    this.registerChangeInAttendanceSummaries();
    this.checkThisMonthSalaryIsLocked();
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

  reloadDataAsSearchFilter(): void {
    this.attendanceSummarySearchService.query(this.filterDto).subscribe((resp: HttpResponse<IAttendanceSummary[]>) => {
      this.attendanceSummaries = resp.body || [];
    });
    this.registerChangeInAttendanceSummaries();
  }

  trackId(index: number, item: IAttendanceSummary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAttendanceSummaries(): void {
    this.eventSubscriber = this.eventManager.subscribe('attendanceSummaryListModification', () => this.loadPage());
  }

  delete(attendanceSummary: IAttendanceSummary): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.attendanceSummaryService.delete(attendanceSummary.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            setTimeout(() => {
              this.loadPage();
            }, 1000);
          },
          () => {
            swalOnRequestError();
          }
        );
      }
    });
  }

  toDate(year: number, month: number): dayjs.Dayjs {
    return dayjs(new Date(Number(year), Number(month) - 1));
    // return dayjs();
  }

  onSearchTextChange(): void {
    this.reloadDataAsSearchFilter();
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  search(searchText: any): void {
    this.searchParamsForm.get('searchText')!.setValue(searchText);
    this.loadPage(1);
  }

  protected onSuccess(data: IAttendanceSummary[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      if (this.isViewByYearMonth) {
        this.router.navigate(['/attendance-summary', this.year, this.month], {
          queryParams: {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
            searchText: this.searchParamsForm.get('searchText')!.value,
          },
        });
      } else {
        this.router.navigate(['/attendance-summary'], {
          queryParams: {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
            searchText: this.searchParamsForm.get('searchText')!.value,
          },
        });
      }
    }
    this.attendanceSummaries = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  checkThisMonthSalaryIsLocked(): void {
    // check selected month salary is locked
    if (this.isViewByYearMonth) {
      this.salaryLockService.isSalaryLocked(this.month.toString(), this.year.toString()).subscribe(isLocked => {
        if (isLocked.body && isLocked.body === true) {
          this.isSalaryLocked = true;
        }
      });
    }
  }
}
