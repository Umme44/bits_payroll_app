import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { IFinalSettlement } from '../final-settlement.model';
import Swal from 'sweetalert2';

import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DEFAULT_SORT_DATA, DESC, ITEM_DELETED_EVENT, SORT } from 'app/config/navigation.constants';
import { EntityArrayResponseType, FinalSettlementService } from '../service/final-settlement.service';
import { FinalSettlementDeleteDialogComponent } from '../delete/final-settlement-delete-dialog.component';
import {
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DEFAULT_TIMER,
  SWAL_SUCCESS_ICON,
  SWAL_SURE,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';

@Component({
  selector: 'jhi-final-settlement',
  templateUrl: './final-settlement.component.html',
})
export class FinalSettlementComponent implements OnInit {
  finalSettlements?: IFinalSettlement[];
  isLoading = false;

  predicate = 'id';
  ascending = true;

  itemsPerPage = ITEMS_PER_PAGE;
  totalItems = 0;
  page = 1;

  ngbPaginationPage = 1;

  constructor(
    protected finalSettlementService: FinalSettlementService,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    protected modalService: NgbModal
  ) {}

  trackId = (_index: number, item: IFinalSettlement): number => this.finalSettlementService.getFinalSettlementIdentifier(item);

  ngOnInit(): void {
    this.loadPage(0);
  }

  generateFinalSettlement(employeeId: number): void {
    Swal.fire({
      text: SWAL_SURE,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.finalSettlementService.generateAndSaveFinalSettlement(employeeId).subscribe(response => {
          const finalSettlement = response.body;
          if (finalSettlement) {
            Swal.fire({
              icon: SWAL_SUCCESS_ICON,
              html: 'Final Settlement data regeneration success! <br/> Populating data...',
              showConfirmButton: false,
              timer: SWAL_DEFAULT_TIMER,
            });
            this.router.navigate([`/final-settlement/${finalSettlement.id}/view`]);
          }
        });
      }
    });
  }

  delete(finalSettlement: IFinalSettlement): void {
    const modalRef = this.modalService.open(FinalSettlementDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.finalSettlement = finalSettlement;
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

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.finalSettlementService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
      })
      .subscribe(
        (res: HttpResponse<IFinalSettlement[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  protected onSuccess(data: IFinalSettlement[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/final-settlement'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.finalSettlements = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
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
    this.finalSettlements = dataFromBody;
  }

  protected fillComponentAttributesFromResponseBody(data: IFinalSettlement[] | null): IFinalSettlement[] {
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
      eagerload: true,
      sort: this.getSortQueryParam(predicate, ascending),
    };
    return this.finalSettlementService.query(queryObject).pipe(tap(() => (this.isLoading = false)));
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
