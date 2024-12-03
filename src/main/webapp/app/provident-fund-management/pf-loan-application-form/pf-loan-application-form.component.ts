import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { PfLoanApplicationFormService } from './pf-loan-application-form.service';
import { PfLoanApplicationFormDeleteDialogComponent } from './pf-loan-application-form-delete-dialog.component';
import Swal from 'sweetalert2';
import { EventManager } from '../../core/util/event-manager.service';
import { IPfLoanApplication } from '../../shared/legacy/legacy-model/pf-loan-application.model';

@Component({
  selector: 'jhi-pf-loan-application',
  templateUrl: './pf-loan-application-form.component.html',
})
export class PfLoanApplicationFormComponent implements OnInit, OnDestroy {
  pfLoanApplications?: IPfLoanApplication[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected pfLoanApplicationFormService: PfLoanApplicationFormService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.pfLoanApplicationFormService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPfLoanApplication[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.pfLoanApplicationFormService.hasAnyPfAccountForThisUser().subscribe(response => {
      if (response === true) {
        this.handleNavigation();
        this.registerChangeInPfLoanApplications();
      } else {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: 'No Pf Account For You! Please Contact with HR',
        });
        this.router.navigate(['/dashboard']);
      }
    });
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

  trackId(index: number, item: IPfLoanApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPfLoanApplications(): void {
    this.eventSubscriber = this.eventManager.subscribe('pfLoanApplicationListModification', () => this.loadPage());
  }

  delete(pfLoanApplication: IPfLoanApplication): void {
    const modalRef = this.modalService.open(PfLoanApplicationFormDeleteDialogComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    modalRef.componentInstance.pfLoanApplication = pfLoanApplication;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IPfLoanApplication[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/pf/pf-loan-application-form'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.pfLoanApplications = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
