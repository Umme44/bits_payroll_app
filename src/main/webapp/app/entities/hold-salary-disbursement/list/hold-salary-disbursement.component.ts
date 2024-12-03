import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHoldSalaryDisbursement } from '../hold-salary-disbursement.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, HoldSalaryDisbursementService } from '../service/hold-salary-disbursement.service';
import { HoldSalaryDisbursementDeleteDialogComponent } from '../delete/hold-salary-disbursement-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { textNormalize } from 'app/shared/common-util-methods/common-util-methods';

@Component({
  selector: 'jhi-hold-salary-disbursement',
  templateUrl: './hold-salary-disbursement.component.html',
})
export class HoldSalaryDisbursementComponent implements OnInit {
  searchText = '';

  holdSalaryDisbursements?: IHoldSalaryDisbursement[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  constructor(
    protected holdSalaryDisbursementService: HoldSalaryDisbursementService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal
  ) {}

  reset(): void {
    this.page = 1;
    this.holdSalaryDisbursements = [];
    this.load();
  }

  loadPage(page: number): void {
    this.page = page;
    this.load();
  }

  trackId = (_index: number, item: IHoldSalaryDisbursement): number =>
    this.holdSalaryDisbursementService.getHoldSalaryDisbursementIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(holdSalaryDisbursement: IHoldSalaryDisbursement): void {
    const modalRef = this.modalService.open(HoldSalaryDisbursementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.holdSalaryDisbursement = holdSalaryDisbursement;
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
    const sort = (params.get(SORT) ?? data[DEFAULT_SORT_DATA]).split(',');
    this.predicate = sort[0];
    this.ascending = sort[1] === ASC;
  }

  protected onResponseSuccess(response: EntityArrayResponseType): void {
    this.fillComponentAttributesFromResponseHeader(response.headers);
    const dataFromBody = this.fillComponentAttributesFromResponseBody(response.body);
    this.holdSalaryDisbursements = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IHoldSalaryDisbursement[] | null): IHoldSalaryDisbursement[] {
    const holdSalaryDisbursementsNew = this.holdSalaryDisbursements ?? [];
    if (data) {
      for (const d of data) {
        if (holdSalaryDisbursementsNew.map(op => op.id).indexOf(d.id) === -1) {
          holdSalaryDisbursementsNew.push(d);
        }
      }
    }
    return holdSalaryDisbursementsNew;
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
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
      searchText: this.searchText,
    };
    return this.holdSalaryDisbursementService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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

  search($event: any): void {
    this.searchText = $event;
    this.reset();
  }

  textNormalize(month: any): string {
    if (month) {
      return textNormalize(month.toString());
    }
    return '';
  }
}
