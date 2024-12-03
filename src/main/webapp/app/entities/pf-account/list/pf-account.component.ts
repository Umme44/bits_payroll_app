import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {IPfAccount} from "../pf-account.model";
import {PfAccountService} from "../service/pf-account.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestErrorWithBackEndErrorTitle
} from "../../../shared/swal-common/swal-common";


@Component({
  selector: 'jhi-pf-account',
  templateUrl: './pf-account.component.html',
})
export class PfAccountComponent implements OnInit, OnDestroy {
  pfAccounts?: IPfAccount[];
  pfAccountsNgSelect: IPfAccount[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  pfAccountPin = '';

  constructor(
    protected pfAccountService: PfAccountService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.pfAccountService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IPfAccount[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInPfAccounts();

    this.pfAccountService.getAllPfAccountsList().subscribe(res => {
      this.pfAccountsNgSelect = res.body!;
      /*pf account selection field with ng-select*/
      this.pfAccountsNgSelect = this.pfAccountsNgSelect.map(pfAccount => {
        return {
          id: pfAccount.id,
          pin: pfAccount.pin,
          name: pfAccount.accHolderName,
          designation: pfAccount.designationName,
          accHolderName: pfAccount.pin + ' - ' + pfAccount.accHolderName + ' - ' + pfAccount.designationName,
        };
      });
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

  trackId(index: number, item: IPfAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInPfAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('pfAccountListModification', () => this.loadPage());
  }

  delete(pfAccount: IPfAccount): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.pfAccountService.delete(pfAccount.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.loadPage();
          },
          err => {
            if (err.status === 500) {
              swalOnRequestErrorWithBackEndErrorTitle('Unable to delete PF account. This account has one or more PF collection.');
            }
          }
        );
      }
    });
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IPfAccount[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/pf-account'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.pfAccounts = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  changePfAccount(pin: any): void {
    if (pin !== undefined && pin !== '' && pin !== null) {
      this.pfAccountService.query({ pin }).subscribe(response => {
        this.pfAccounts = response.body!;
      });
    } else if (pin === null) {
      this.loadPage(0);
    }
  }

  getEllipsis(name: string): String {
    return name.slice(0, 25) + '...';
  }
}
