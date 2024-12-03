import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';

import { IMobileBill } from '../mobile-bill.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { EntityArrayResponseType, MobileBillService } from '../service/mobile-bill.service';
import { MobileBillDeleteDialogComponent } from '../delete/mobile-bill-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { IEmployee } from '../../employee-custom/employee-custom.model';
import { FormBuilder } from '@angular/forms';
import { EmployeeCustomService } from '../../employee-custom/service/employee-custom.service';
import { SalaryLockService } from '../../salary-generator-master/salary-lock/salary-lock-service';

@Component({
  selector: 'jhi-mobile-bill',
  templateUrl: './mobile-bill.component.html',
})
export class MobileBillComponent implements OnInit {
  mobileBills?: IMobileBill[];
  employees: IEmployee[] = [];
  isViewByYearMonth?: boolean;
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  totalItems = 0;

  year = 0;
  month = 0;
  searchText = '';
  isSalaryLocked!: boolean;

  ngbPaginationPage = 1;

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
    searchText: [],
  });

  constructor(
    protected mobileBillService: MobileBillService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal,
    protected fb: FormBuilder,
    protected employeeService: EmployeeCustomService,
    protected salaryLockService: SalaryLockService
  ) {}

  reset(): void {
    this.page = 1;
    this.mobileBills = [];
    this.load();
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.page = page;
    this.load(page, dontNavigate);
  }

  trackId = (_index: number, item: IMobileBill): number => this.mobileBillService.getMobileBillIdentifier(item);

  ngOnInit(): void {
    this.year = this.activatedRoute.snapshot.params['year'];
    this.month = this.activatedRoute.snapshot.params['month'];

    this.load();
    this.handleNavigation();
    this.checkThisMonthSalaryIsLocked();
  }

  delete(mobileBill: IMobileBill): void {
    const modalRef = this.modalService.open(MobileBillDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.mobileBill = mobileBill;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      });
  }

  load(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    if (this.year !== undefined && this.month !== undefined) {
      this.isViewByYearMonth = true;
      this.mobileBillService
        .queryWithParams(this.year, this.month, {
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          searchText: this.searchText,
        })
        .subscribe(
          (res: HttpResponse<IMobileBill[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
    } else {
      this.isViewByYearMonth = false;
      this.mobileBillService
        .query({
          page: pageToLoad - 1,
          size: this.itemsPerPage,
          sort: this.sort(),
          searchText: this.searchText,
          month: this.searchParamsForm.get('month')!.value,
          year: this.searchParamsForm.get('year')!.value,
        })
        .subscribe(
          (res: HttpResponse<IMobileBill[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
          () => this.onError()
        );
    }

    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    // this.loadFromBackendWithRouteInformations().subscribe({
    //   next: (res: EntityArrayResponseType) => {
    //     this.onResponseSuccess(res);
    //   },
    // });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IMobileBill[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      if (this.isViewByYearMonth) {
        this.router.navigate(['/mobile-bill', this.year, this.month], {
          queryParams: {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
            searchText: this.searchText,
          },
        });
      } else {
        this.router.navigate(['/mobile-bill'], {
          queryParams: {
            page: this.page,
            size: this.itemsPerPage,
            sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
            searchText: this.searchText,
          },
        });
      }
    }
    this.mobileBills = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  getEmployeeById(id: number): IEmployee {
    return this.employees.find(x => x.id === id)!;
  }

  toDate(year: number, month: number): dayjs.Dayjs {
    return dayjs(new Date(Number(year), Number(month) - 1));
  }

  checkThisMonthSalaryIsLocked(): void {
    //check selected month salary is locked
    if (this.isViewByYearMonth) {
      this.salaryLockService.isSalaryLocked(this.month.toString(), this.year.toString()).subscribe(isLocked => {
        if (isLocked.body && isLocked.body === true) {
          this.isSalaryLocked = true;
        }
      });
    }
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation(this.page, this.predicate, this.ascending);
  }

  navigateToPage(page = this.page): void {
    this.handleNavigation(page, this.predicate, this.ascending);
  }

  protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
    return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
      tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
      switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
    );
  }

  protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.mobileBills = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IMobileBill[] | null): IMobileBill[] {
    const mobileBillsNew = this.mobileBills ?? [];
    if (data) {
      for (const d of data) {
        if (mobileBillsNew.map(op => op.id).indexOf(d.id) === -1) {
          mobileBillsNew.push(d);
        }
      }
    }
    return mobileBillsNew;
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    const linkHeader = headers.get('link');
    if (linkHeader) {
      this.links = this.parseLinks.parse(linkHeader);
    } else {
      this.links = {
        last: 0,
      };
    }
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.mobileBillService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
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

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
}
