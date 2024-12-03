import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPfCollection } from '../pf-collection.model';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, PfCollectionService } from '../service/pf-collection.service';
import { PfCollectionDeleteDialogComponent } from '../delete/pf-collection-delete-dialog.component';
import { IPfAccount } from '../../pf-account/pf-account.model';
import {
  swalClose,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnLoading, swalOnRequestErrorWithBackEndErrorTitle
} from "../../../shared/swal-common/swal-common";

import {PfAccountService} from "../../pf-account/service/pf-account.service";
import { Subscription } from 'rxjs';
import {EventManager} from "../../../core/util/event-manager.service";

@Component({
  selector: 'jhi-pf-collection',
  templateUrl: './pf-collection.component.html',
})
export class PfCollectionComponent implements OnInit {
  pfCollections?: IPfCollection[];
  isLoading = false;
  /* pfAccountsNgSelect: IPfAccount[] = []; */
  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;
  ngbPaginationPage = 1;

/*   filter = {
    pfAccountId: null,
    year: null,
    month: null,
    pfCollectionType: null,
    page: 0,
    size: this.itemsPerPage,
    sort: this.sort(),
  }; */
/*   years: any = [
    { Value2: 2, Text2: '2023' },
    { Value2: 3, Text2: '2022' },
    { Value2: 4, Text2: '2021' },
    { Value2: 5, Text2: '2020' },
    { Value2: 6, Text2: '2019' },
    { Value2: 7, Text2: '2018' },
    { Value2: 8, Text2: '2017' },
    { Value2: 9, Text2: '2016' },
    { Value2: 10, Text2: '2015' },
    { Value2: 10, Text2: '2014' },
  ]; */

/*   months = [
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
  ]; */

  eventSubscriber?: Subscription;
  pfAccountsNgSelect: IPfAccount[] = [];

  filter = {
    pfAccountId: null,
    year: null,
    month: null,
    pfCollectionType: null,
    page: 0,
    size: this.itemsPerPage,
    sort: this.sort(),
  };

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

/*   years = [
    { Value: 2023, Text: '2023' },
    { Value: 2022, Text: '2022' },
    { Value: 2021, Text: '2021' },
    { Value: 2020, Text: '2020' },
    { Value: 2019, Text: '2019' },
    { Value: 2018, Text: '2018' },
    { Value: 2017, Text: '2017' },
    { Value: 2016, Text: '2016' },
    { Value: 2015, Text: '2015' },
    { Value: 2014, Text: '2014' },
    { Value: 2013, Text: '2013' },
    { Value: 2012, Text: '2012' },
  ]; */

  constructor(
    protected pfCollectionService: PfCollectionService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal,
    protected pfAccountService: PfAccountService,
    protected eventManager: EventManager,
  ) {}
  id = 0;
  trackId = (_index: number, item: IPfCollection): number => this.pfCollectionService.getPfCollectionIdentifier(item);

  ngOnInit(): void {
    this.loadPfAccounts();
    this.load();
    this.handleNavigation();
    this.registerChangeInPfCollections();
  }
/*   sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  } */

/*  delete(pfCollection: IPfCollection): void {
    const modalRef = this.modalService.open(PfCollectionDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.pfCollection = pfCollection;
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
  }*/

  delete(pfCollection: IPfCollection): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.pfAccountService.delete(pfCollection.id!).subscribe(
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

  load(): void {
    this.loadFromBackendWithRouteInformations().subscribe({
      next: (res: EntityArrayResponseType) => {
        this.onResponseSuccess(res);
      },
    });
  }
/*   protected onSuccess(data: IPfCollection[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/pf-collection'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.pfCollections = data || [];
    this.ngbPaginationPage = this.page;
    // swalClose();
  } */
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
    this.pfCollections = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IPfCollection[] | null): IPfCollection[] {
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
    return this.pfCollectionService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
/*   protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;
    this.filter.page = pageToLoad - 1;

    // swalOnLoading('Loading Collections');
    this.pfCollectionService.query(this.filter).subscribe(
      (res: HttpResponse<IPfCollection[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
      () => this.onError()
    );
  }
  changePfAccount(pfAccountId: any): void {
    if (pfAccountId) {
      if (pfAccountId === -1) {
        this.filter.pfAccountId = null;
      } else {
        this.filter.pfAccountId = pfAccountId;
      }
      this.loadPage(1, false);
    }
  }

  loadAllOfAYear(value: any): void {
    if (value !== -1) {
      this.filter.year = null;
    } else {
      this.filter.year = value;
    }
    this.loadPage(1, false);
  }

  loadAllByAMonth(event: any): void {
    const value = event.target.value;
    if (Number(value)) {
      this.filter.month = value;
    } else {
      this.filter.month = null;
    }
    this.loadPage(1, false);
  } */

  // filterByCollectionTypes(collectionType: any): void {
  //   if (collectionType === 'all') {
  //     this.filter.pfCollectionType = null;
  //   } else {
  //     this.filter.pfCollectionType = collectionType;
  //   }
  //   this.loadPage(1, false);
  // }
  filterByCollectionTypes(collectionType: any): void {
    if (collectionType === 'all') {
      this.filter.pfCollectionType = null;
    } else {
      this.filter.pfCollectionType = collectionType;
    }
    this.loadPage(1, false);
  }

  changePfAccount(pfAccountId: any): void {
    if (pfAccountId) {
      if (pfAccountId === -1) {
        this.filter.pfAccountId = null;
      } else {
        this.filter.pfAccountId = pfAccountId;
      }
      this.loadPage(1, false);
    }
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;
    this.filter.page = pageToLoad - 1;

    swalOnLoading('Loading Collections');
    this.pfCollectionService.query(this.filter).subscribe(
      (res: HttpResponse<IPfCollection[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
      () => this.onError()
    );
  }


  protected onSuccess(data: IPfCollection[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/pf-collection'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.pfCollections = data || [];
    this.ngbPaginationPage = this.page;
    swalClose();
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  loadAllByAMonth(event: any): void {
    const value = event.target.value;
    if (Number(value)) {
      this.filter.month = value;
    } else {
      this.filter.month = null;
    }
    this.loadPage(1, false);
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }
  loadPfAccounts(): void {
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
      this.pfAccountsNgSelect.unshift({
        id: -1,
        accHolderName: 'All',
      });
    });
  }

  loadAllByAYear(event: any): void {
    const value = event.target.value;
    if (Number(value)) {
      this.filter.year = value;
    } else {
      this.filter.year = null;
    }
    this.loadPage(1, false);
  }
  loadAllOfAYear(value: any): void {
    if (value === -1) {
      this.filter.year = null;
    } else {
      this.filter.year = value;
    }
    this.loadPage(1, false);
  }

  registerChangeInPfCollections(): void {
    this.eventSubscriber = this.eventManager.subscribe('pfCollectionListModification', () => this.loadPage());
  }

  toMonth(month: any): string {
    //year is optional and every year has 12 months
    //here january is 0 based
    return new Date(2000, month - 1).toLocaleString('default', { month: 'long' });
  }
}
