import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';


import { IPfLoanRepayment } from '../pf-loan-repayment.model';
import { PfLoanRepaymentService } from '../service/pf-loan-repayment.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { PfLoanRepaymentDeleteDialogComponent } from '../delete/pf-loan-repayment-delete-dialog.component';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';

@Component({
  selector: 'jhi-monthly-pf-loan-repayment',
  templateUrl: './monthly-pf-loan-repayment.component.html',
})
export class MonthlyPfLoanRepaymentComponent implements OnInit, OnDestroy {
  pfLoanRepayments?: IPfLoanRepayment[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  isViewByYearMonth?: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected pfLoanRepaymentService: PfLoanRepaymentService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  loadPage(page?: number, dontNavigate?: boolean): void {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const pageToLoad: number = page || this.page || 1;

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
  }

  ngOnInit(): void {
    this.handleNavigation();
    // this.loadAll();
    this.loadPage();
    this.registerChangeInPfLoanRepayments();
  }

  // loadAll(): void {
  //   const year = this.activatedRoute.snapshot.params['year'];
  //   const month = this.activatedRoute.snapshot.params['month'];
  //
  //   if (year !== undefined && month !== undefined)
  //     this.pfLoanRepaymentService
  //       .queryWithParams(year, month)
  //       .subscribe((res: HttpResponse<IPfLoanRepayment[]>) => (this.pfLoanRepayments = res.body || []));
  //   else this.pfLoanRepaymentService.query().subscribe((res: HttpResponse<IPfLoanRepayment[]>) => (this.pfLoanRepayments = res.body || []));
  //
  //   this.pfLoanRepaymentService.query().subscribe((res: HttpResponse<IPfLoanRepayment[]>) => (this.pfLoanRepayments = res.body || []));

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPfLoanRepayment): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPfLoanRepayments(): void {
    this.eventSubscriber = this.eventManager.subscribe('pfLoanRepaymentListModification', () => this.loadPage());
  }

  delete(pfLoanRepayment: IPfLoanRepayment): void {
    const modalRef = this.modalService.open(PfLoanRepaymentDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pfLoanRepayment = pfLoanRepayment;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  // }
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

  protected onSuccess(data: IPfLoanRepayment[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/pf-loan-repayment'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.pfLoanRepayments = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  navigateToWithComponentValues(): void {
    this.handleNavigation();
  }
}
