import { Component, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { Subscription, combineLatest, filter, Observable, switchMap, tap } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder } from '@angular/forms';
import { IProRataFestivalBonus } from '../pro-rata-festival-bonus.model';
import dayjs from 'dayjs/esm';
import { ITEMS_PER_PAGE, PAGE_HEADER, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { ASC, DESC, SORT, ITEM_DELETED_EVENT, DEFAULT_SORT_DATA } from 'app/config/navigation.constants';
import { EntityArrayResponseType, ProRataFestivalBonusService } from '../service/pro-rata-festival-bonus.service';
import { ProRataFestivalBonusDeleteDialogComponent } from '../delete/pro-rata-festival-bonus-delete-dialog.component';
import { EventManager } from '../../../core/util/event-manager.service';
import { IEmployeeResignation } from '../../employee-resignation/employee-resignation.model';

@Component({
  selector: 'jhi-pro-rata-festival-bonus',
  templateUrl: './pro-rata-festival-bonus.component.html',
})
export class ProRataFestivalBonusComponent implements OnInit {
  proRataFestivalBonuses?: IProRataFestivalBonus[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  isInvalid = false;

  searchParamsForm = this.fb.group({
    searchText: [''],
    startDate: [''],
    endDate: [''],
  });

  constructor(
    protected proRataFestivalBonusService: ProRataFestivalBonusService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.proRataFestivalBonusService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchParamsForm.get('searchText')!.value,
      })
      .subscribe(
        (res: HttpResponse<IProRataFestivalBonus[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInProRataFestivalBonuses();
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

  trackId(index: number, item: IProRataFestivalBonus): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInProRataFestivalBonuses(): void {
    this.eventSubscriber = this.eventManager.subscribe('proRataFestivalBonusListModification', () => this.loadPage());
  }

  delete(proRataFestivalBonus: IProRataFestivalBonus): void {
    const modalRef = this.modalService.open(ProRataFestivalBonusDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proRataFestivalBonus = proRataFestivalBonus;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IProRataFestivalBonus[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/pro-rata-festival-bonus'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
          searchText: this.searchParamsForm.get('searchText')!.value,
        },
      });
    }
    this.proRataFestivalBonuses = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  checkDate(): void {
    const doj = this.searchParamsForm.get(['startDate'])!.value;
    const dor = this.searchParamsForm.get(['endDate'])!.value;

    if (doj !== undefined && dor !== undefined && doj > dor) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  searchOnList(): void {
    const pageToLoad = 1;
    const startDate1 = this.searchParamsForm.get('startDate')!.value
      ? dayjs(this.searchParamsForm.get('startDate')!.value).format('YYYY-MM-DD')
      : '';
    const endDate1: string = this.searchParamsForm.get('endDate')!.value
      ? dayjs(this.searchParamsForm.get('endDate')!.value).format('YYYY-MM-DD')
      : '';

    this.proRataFestivalBonusService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        searchText: this.searchParamsForm.get('searchText')!.value,
        startDate: startDate1,
        endDate: endDate1,
      })
      .subscribe(
        (res: HttpResponse<IEmployeeResignation[]>) => this.onSuccess(res.body, res.headers, pageToLoad, true),
        () => this.onError()
      );
  }

  protected readonly Date = Date;
  protected readonly dayjs = dayjs;
}
