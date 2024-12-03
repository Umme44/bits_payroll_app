import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfArrear } from '../pf-arrear.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, PfArrearService } from '../service/pf-arrear.service';
import { PfArrearDeleteDialogComponent } from '../delete/pf-arrear-delete-dialog.component';
import { ParseLinks } from 'app/core/util/parse-links.service';
import { QuickFilterDTO } from '../../../shared/model/quick-filter-dto';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-pf-arrear',
  templateUrl: './pf-arrear.component.html',
})
export class PfArrearComponent implements OnInit {
  pfArrears?: IPfArrear[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;
  quickFilterDto = new QuickFilterDTO();

  constructor(
    protected pfArrearService: PfArrearService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected parseLinks: ParseLinks,
    protected modalService: NgbModal
  ) {}

  reset(): void {
    /* this.page = 1;
    this.pfArrears = [];
    this.load(); */
    this.page = 0;
    this.pfArrears = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.load();
  }

  trackId = (_index: number, item: IPfArrear): number => this.pfArrearService.getPfArrearIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  delete(pfArrear: IPfArrear): void {
    const modalRef = this.modalService.open(PfArrearDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pfArrear = pfArrear;
    // unsubscribe not needed because closed completes on modal close
    /*  modalRef.closed
      .pipe(
        filter(reason => reason === ITEM_DELETED_EVENT),
        switchMap(() => this.loadFromBackendWithRouteInformations())
      )
      .subscribe({
        next: (res: EntityArrayResponseType) => {
          this.onResponseSuccess(res);
        },
      }); */
    modalRef.closed.subscribe((res: HttpResponse<IPfArrear[]>) => {
      // this.onSuccess(res.body, res.headers, this.page, false),
      this.pfArrears = [];
      this.loadPage(this.page);
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
    this.pfArrears = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IPfArrear[] | null): IPfArrear[] {
    const pfArrearsNew = this.pfArrears ?? [];
    if (data) {
      for (const d of data) {
        if (pfArrearsNew.map(op => op.id).indexOf(d.id) === -1) {
          pfArrearsNew.push(d);
        }
      }
    }
    return pfArrearsNew;
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
    return this.pfArrearService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
    this.quickFilterDto.searchText = $event;
    this.reset();
  }

  loadAll(): void {
    this.pfArrearService
      .queryByFiltering(this.quickFilterDto, {
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IPfArrear[]>) => this.paginatePfArrears(res.body, res.headers));
  }
  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginatePfArrears(data: IPfArrear[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.pfArrears.push(data[i]);
      }
    }
  }
}
