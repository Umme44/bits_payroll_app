import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { EventManager } from 'app/core/util/event-manager.service';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { combineLatest, Subscription } from 'rxjs';
import { PfNomineeFormService } from '../pf-nominee-form.service';
import Swal from 'sweetalert2';
import { PfLoanApplicationFormService } from '../../pf-loan-application-form/pf-loan-application-form.service';
import { IPfNominee } from '../../../shared/legacy/legacy-model/pf-nominee.model';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';

@Component({
  selector: 'jhi-pf-nominee-form',
  templateUrl: './pf-nominee-form.component.html',
})
export class PfNomineeFormComponent implements OnInit, OnDestroy {
  pfNominees?: IPfNominee[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected pfNomineeFormService: PfNomineeFormService,
    protected pfLoanApplicationFormService: PfLoanApplicationFormService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.pfNomineeFormService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPfNominee[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.pfLoanApplicationFormService.hasAnyPfAccountForThisUser().subscribe(response => {
      if (response === true) {
        this.handleNavigation();
        this.registerChangeInPfNominees();
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

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IPfNominee): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPfNominees(): void {
    this.eventSubscriber = this.eventManager.subscribe('pfNomineeListModification', () => this.loadPage());
  }

  delete(pfNominee: IPfNominee): void {}

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
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

  protected onSuccess(data: IPfNominee[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/pf/pf-nominee-form'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.pfNominees = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'No Pf Account For You! Please Contact with HR',
    });
    this.ngbPaginationPage = this.page ?? 1;
  }

  addressSlice(address: string): String {
    return address.slice(0, 25) + '...';
  }

  print(): void {
    window.print();
  }
}
