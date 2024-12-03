import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { Subscription, combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IArrearSalary } from '../arrear-salary.model';
import { FormBuilder } from '@angular/forms';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ArrearSalaryService } from '../service/arrear-salary.service';
import { ArrearSalaryDeleteDialogComponent } from '../delete/arrear-salary-delete-dialog.component';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  selector: 'jhi-arrear-salary',
  templateUrl: './arrear-salary.component.html',
})
export class ArrearSalaryComponent implements OnInit, OnDestroy {
  arrearSalaries?: IArrearSalary[];
  isLoading = false;
  eventSubscriber?: Subscription;
  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

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
    searchText: [''],
    month: [0],
    year: [0],
  });

  constructor(
    protected arrearSalaryService: ArrearSalaryService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
    protected eventManager: EventManager,
    private fb: FormBuilder
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

    this.arrearSalaryService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchParamsForm.get('searchText')!.value,
        month: this.searchParamsForm.get('month')!.value,
        year: this.searchParamsForm.get('year')!.value,
      })
      .subscribe(
        (res: HttpResponse<IArrearSalary[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  // trackId = (_index: number, item: IArrearSalary): number => this.arrearSalaryService.getArrearSalaryIdentifier(item);

  ngOnInit(): void {
    // this.load();
    this.handleNavigation();
    this.registerChangeInArrearSalaries();
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

  trackId(index: number, item: IArrearSalary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInArrearSalaries(): void {
    this.eventSubscriber = this.eventManager.subscribe('arrearSalaryListModification', () => this.loadPage());
  }

  // delete(arrearSalary: IArrearSalary): void {
  //   const modalRef = this.modalService.open(ArrearSalaryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.arrearSalary = arrearSalary;
  //   // unsubscribe not needed because closed completes on modal close
  //   modalRef.closed
  //     .pipe(
  //       filter(reason => reason === ITEM_DELETED_EVENT),
  //       switchMap(() => this.loadFromBackendWithRouteInformations())
  //     )
  //     .subscribe({
  //       next: (res: EntityArrayResponseType) => {
  //         this.onResponseSuccess(res);
  //       },
  //     });
  // }
  delete(arrearSalary: IArrearSalary): void {
    const modalRef = this.modalService.open(ArrearSalaryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.arrearSalary = arrearSalary;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IArrearSalary[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/arrear-salary'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
          searchText: this.searchParamsForm.get('searchText')!.value,
        },
      });
    }
    this.arrearSalaries = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  // navigateToWithComponentValues(): void {
  //   this.handleNavigation();
  // }
  //
  // navigateToPage(page = this.page): void {
  //   this.handleNavigation();
  // }

  // protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
  //   return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
  //     tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
  //     switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
  //   );
  // }

  // protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
  //   const page = params.get(PAGE_HEADER);
  //   this.page = +(page ?? 1);
  //   const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
  //   this.predicate = sort[0];
  //   this.ascending = sort[1] === ASC;
  // }

  // protected onResponseSuccess(response: EntityArrayResponseType): void {
  //   this.fillComponentAttributesFromResponseHeader(response.headers);
  //   const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
  //   this.arrearSalaries = dataFromBody;
  // }
  //
  // protected fillComponentAttributesFromResponseBody(data: IArrearSalary[] | null): IArrearSalary[] {
  //   return data ?? [];
  // }
  //
  // protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
  //   this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  // }

  // protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
  //   this.isLoading = true;
  //   const pageToLoad: number = page ?? 1;
  //   const queryObject = {
  //     page: pageToLoad - 1,
  //     size: this.itemsPerPage,
  //     sort: this.getSortQueryParam(predicate, ascending),
  //   };
  //   return this.arrearSalaryService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  // }

  // protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
  //   const queryParamsObj = {
  //     page,
  //     size: this.itemsPerPage,
  //     sort: this.getSortQueryParam(predicate, ascending),
  //   };
  //
  //   this.router.navigate(['./'], {
  //     relativeTo: this.activatedRoute,
  //     queryParams: queryParamsObj,
  //   });
  // }

  // load(): void {
  //   this.loadFromBackendWithRouteInformations().subscribe({
  //     next: (res: EntityArrayResponseType) => {
  //       this.onResponseSuccess(res);
  //     },
  //   });
  // }

  // protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
  //   const ascendingQueryParam = ascending ? ASC : DESC;
  //   if (predicate === '') {
  //     return [];
  //   } else {
  //     return [predicate + ',' + ascendingQueryParam];
  //   }
  // }
}
