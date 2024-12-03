import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { Subscription } from 'rxjs';

import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { IItemInformation } from '../item-information.model';
import { IDepartment } from '../../department/department.model';
import { ItemInformationService } from '../service/item-information.service';
import { DataUtils } from '../../../core/util/data-util.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { DepartmentService } from '../../department/service/department.service';
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestErrorWithBackEndErrorTitle,
} from '../../../shared/swal-common/swal-common';

@Component({
  selector: 'jhi-item-information',
  templateUrl: './item-information.component.html',
})
export class ItemInformationComponent implements OnInit, OnDestroy {
  itemInformations: IItemInformation[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;
  departmentFormControl = new FormControl(null);
  departments: IDepartment[] = [];

  constructor(
    protected itemInformationService: ItemInformationService,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private departmentService: DepartmentService
  ) {
    this.itemInformations = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.itemInformationService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
        departmentId: this.departmentFormControl.value,
      })
      .subscribe((res: HttpResponse<IItemInformation[]>) => this.paginateItemInformations(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.itemInformations = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInItemInformations();
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IItemInformation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    return this.dataUtils.openFile(contentType, base64String);
  }

  registerChangeInItemInformations(): void {
    this.eventSubscriber = this.eventManager.subscribe('itemInformationListModification', () => this.reset());
  }

  delete(itemInformation: IItemInformation): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.itemInformationService.delete(itemInformation.id!).subscribe(
          _ => {
            swalOnDeleteSuccess();
            this.reset();
          },
          _ => swalOnRequestErrorWithBackEndErrorTitle('Failed to delete! It has already used in requisition(s).')
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

  protected paginateItemInformations(data: IItemInformation[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.itemInformations.push(data[i]);
      }
    }
  }
}

/*
import { Component, OnInit } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IItemInformation } from '../item-information.model';

import { ITEMS_PER_PAGE } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ItemInformationService } from '../service/item-information.service';
import { ItemInformationDeleteDialogComponent } from '../delete/item-information-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';
import { ParseLinks } from 'app/core/util/parse-links.service';

@Component({
  selector: 'jhi-item-information',
  templateUrl: './item-information.component.html',
})
export class ItemInformationComponent implements OnInit {
  itemInformations?: IItemInformation[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  links: { [key: string]: number } = {
    last: 0,
  };
  page = 1;

  constructor(
    protected itemInformationService: ItemInformationService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected parseLinks: ParseLinks,
    protected dataUtils: DataUtils,
    protected modalService: NgbModal
  ) {}

  reset(): void {
    this.page = 1;
    this.itemInformations = [];
    this.load();
  }

  loadPage(page: number): void {
    this.page = page;
    this.load();
  }

  trackId = (_index: number, item: IItemInformation): number => this.itemInformationService.getItemInformationIdentifier(item);

  ngOnInit(): void {
    this.load();
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(itemInformation: IItemInformation): void {
    const modalRef = this.modalService.open(ItemInformationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.itemInformation = itemInformation;
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
    this.itemInformations = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IItemInformation[] | null): IItemInformation[] {
    const itemInformationsNew = this.itemInformations ?? [];
    if (data) {
      for (const d of data) {
        if (itemInformationsNew.map(op => op.id).indexOf(d.id) === -1) {
          itemInformationsNew.push(d);
        }
      }
    }
    return itemInformationsNew;
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
    };
    return this.itemInformationService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
}
*/
