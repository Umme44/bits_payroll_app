import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, Subscription, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfLoanRepayment } from '../pf-loan-repayment.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, PfLoanRepaymentService } from '../service/pf-loan-repayment.service';
import { PfLoanRepaymentDeleteDialogComponent } from '../delete/pf-loan-repayment-delete-dialog.component';
import { IEmployee } from '../../employee/employee.model';
import { EmployeeService } from '../../employee/service/employee.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  selector: 'jhi-pf-loan-repayment',
  templateUrl: './pf-loan-repayment.component.html',
})
export class PfLoanRepaymentComponent implements OnInit {
  pfLoanRepayments?: IPfLoanRepayment[];
  employees: IEmployee[] = [];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  eventSubscriber?: Subscription;
  isViewByYearMonth?: boolean;

  constructor(
    protected pfLoanRepaymentService: PfLoanRepaymentService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
    protected employeeService: EmployeeService,
    protected eventManager: EventManager
  ) {}

  trackId = (_index: number, item: IPfLoanRepayment): number => this.pfLoanRepaymentService.getPfLoanRepaymentIdentifier(item);

  ngOnInit(): void {
    this.handleNavigation();
    this.load();
    this.registerChangeInPfLoanRepayments();
  }

  delete(pfLoanRepayment: IPfLoanRepayment): void {
    const modalRef = this.modalService.open(PfLoanRepaymentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pfLoanRepayment = pfLoanRepayment;
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

  load(): void {
    const pageToLoad: number = this.page || 1;

    const year = this.activatedRoute.snapshot.params['year'];
    const month = this.activatedRoute.snapshot.params['month'];

    if (year !== undefined && month !== undefined) {
      this.isViewByYearMonth = true;
      this.pfLoanRepaymentService
        .queryWithParams(year, month)
        .subscribe((res: HttpResponse<IPfLoanRepayment[]>) => (this.pfLoanRepayments = res.body || []));
    } else {
      this.isViewByYearMonth = false;
      this.pfLoanRepaymentService.query().subscribe((res: HttpResponse<IPfLoanRepayment[]>) => (this.pfLoanRepayments = res.body || []));
    }

    this.pfLoanRepaymentService.query().subscribe((res: HttpResponse<IPfLoanRepayment[]>) => (this.pfLoanRepayments = res.body || []));
    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
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
    const page = params.get(PAGE_HEADER);
    this.page = +(page ?? 1);
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.pfLoanRepayments = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IPfLoanRepayment[] | null): IPfLoanRepayment[] {
    return data ?? [];
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.totalItems = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected queryBackend(page?: number, predicate?: string, ascending?: boolean): Observable<EntityArrayResponseType> {
    this.isLoading = true;
    const pageToLoad: number = page ?? 1;
    const queryObject = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.pfLoanRepaymentService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
  }

  protected handleNavigation(page = this.page, predicate?: string, ascending?: boolean): void {
    const queryParamsObj = {
      page,
      size: this.itemsPerPage,
      sort: this.getSortQueryParam(predicate, ascending),
    };

    this.router.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  protected getSortQueryParam(predicate = this.predicate, ascending = this.ascending): string[] {
    const ascendingQueryParam = ascending ? ASC : DESC;
    if (predicate === '') {
      return [];
    } else {
      return [predicate + ',' + ascendingQueryParam];
    }
  }
  registerChangeInPfLoanRepayments(): void {
    this.eventSubscriber = this.eventManager.subscribe('pfLoanRepaymentListModification', () => this.load());
  }

  getEmployeeById(id: number): IEmployee {
    return this.employees.find(x => x.id === id)!;
  }
}
