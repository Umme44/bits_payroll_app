import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { Subscription, combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';
import { IAitPayment } from '../ait-payment.model';
import dayjs from 'dayjs/esm';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, AitPaymentService } from '../service/ait-payment.service';
import { AitPaymentDeleteDialogComponent } from '../delete/ait-payment-delete-dialog.component';
import { EventManager } from '../../../core/util/event-manager.service';
import { IEmployeeResignation } from '../../../shared/legacy/legacy-model/employee-resignation.model';
import { DATE_FORMAT } from '../../../config/input.constants';

@Component({
  selector: 'jhi-ait-payment',
  templateUrl: './ait-payment.component.html',
})
export class AitPaymentComponent implements OnInit, OnDestroy {
  aitPayments?: IAitPayment[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  isInvalid = false;

  searchParamsForm = this.fb.group({
    searchText: [''],
    startDate: [''],
    endDate: [''],
  });

  constructor(
    protected aitPaymentService: AitPaymentService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.aitPaymentService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchParamsForm.get('searchText')!.value,
      })
      .subscribe(
        (res: HttpResponse<IAitPayment[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInAitPayments();
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

  trackId(index: number, item: IAitPayment): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAitPayments(): void {
    this.eventSubscriber = this.eventManager.subscribe('aitPaymentListModification', () => this.loadPage());
  }

  delete(aitPayment: IAitPayment): void {
    const modalRef = this.modalService.open(AitPaymentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.aitPayment = aitPayment;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IAitPayment[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/ait-payment'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.aitPayments = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  checkDate(): void {
    const doj = this.searchParamsForm.get(['startDate'])!.value;
    const dor = this.searchParamsForm.get(['endDate'])!.value;

    if (doj !== undefined && dor !== undefined && doj > dor) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  searchOnList(): void {
    const pageToLoad = 1;
    const startDate1 = this.searchParamsForm.get('startDate')!.value
      ? dayjs(this.searchParamsForm.get('startDate')!.value).format('YYYY-MM-DD')
      : '';
    const endDate1: string = this.searchParamsForm.get('endDate')!.value
      ? dayjs(this.searchParamsForm.get('endDate')!.value).format('YYYY-MM-DD')
      : '';

    this.aitPaymentService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchParamsForm.get('searchText')!.value,
        startDate: startDate1,
        endDate: endDate1,
      })
      .subscribe(
        (res: HttpResponse<IEmployeeResignation[]>) => this.onSuccess(res.body, res.headers, pageToLoad, true),
        () => this.onError()
      );
  }

  // delete(aitPayment: IAitPayment): void {
  //   const modalRef = this.modalService.open(AitPaymentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.aitPayment = aitPayment;
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

  // load(): void {
  //   this.loadFromBackendWithRouteInformations().subscribe({
  //     next: (res: EntityArrayResponseType) => {
  //       this.onResponseSuccess(res);
  //     },
  //   });
  // }

  // trackId = (_index: number, item: IAitPayment): number => this.aitPaymentService.getAitPaymentIdentifier(item);
  // navigateToWithComponentValues(): void {
  //   this.handleNavigation(this.page, this.predicate, this.ascending);
  // }
  //
  // navigateToPage(page = this.page): void {
  //   this.handleNavigation(page, this.predicate, this.ascending);
  // }
  //
  // protected loadFromBackendWithRouteInformations(): Observable<EntityArrayResponseType> {
  //   return combineLatest([this.activatedRoute.queryParamMap, this.activatedRoute.data]).pipe(
  //     tap(([params, data]) => this.fillComponentAttributeFromRoute(params, data)),
  //     switchMap(() => this.queryBackend(this.page, this.predicate, this.ascending))
  //   );
  // }
  //
  // protected fillComponentAttributeFromRoute(params: ParamMap, data: Data): void {
  //   const page = params.get(PAGE_HEADER);
  //   this.page = +(page ?? 1);
  //   const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
  //   this.predicate = sort[0];
  //   this.ascending = sort[1] === ASC;
  // }
  //
  // protected onResponseSuccess(response: EntityArrayResponseType): void {
  //   this.fillComponentAttributesFromResponseHeader(response.headers);
  //   const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
  //   this.aitPayments = dataFromBody;
  // }
  //
  // protected fillComponentAttributesFromResponseBody(data: IAitPayment[] | null): IAitPayment[] {
  //   return data ?? [];
  // }
  //
  // protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
  //   this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  // }
  //
  // protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
  //   this.isLoading = true;
  //   const pageToLoad: number = page ?? 1;
  //   const queryObject = {
  //     page: pageToLoad - 1,
  //     size: this.itemsPerPage,
  //     sort: this.getSortQueryParam(predicate, ascending),
  //   };
  //   return this.aitPaymentService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  // }
  //
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
  //
  // protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
  //   const ascendingQueryParam = ascending ? ASC : DESC;
  //   if (predicate === '') {
  //     return [];
  //   } else {
  //     return [predicate + ',' + ascendingQueryParam];
  //   }
  // }
  protected readonly dayjs = dayjs;
}
