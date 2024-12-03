import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {ILeaveAllocation} from "../leave-allocation.model";
import {LeaveAllocationService} from "../service/leave-allocation.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {LeaveAllocationDeleteDialogComponent} from "../delete/leave-allocation-delete-dialog.component";
import {IHolidays} from "../../holidays/holidays.model";
import {
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestError
} from "../../../shared/swal-common/swal-common";

@Component({
  selector: 'jhi-leave-allocation',
  templateUrl: './leave-allocation.component.html',
})
export class LeaveAllocationComponent implements OnInit, OnDestroy {
  leaveAllocations?: ILeaveAllocation[];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  constructor(
    protected leaveAllocationService: LeaveAllocationService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.leaveAllocationService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ILeaveAllocation[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInLeaveAllocations();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILeaveAllocation): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLeaveAllocations(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveAllocationListModification', () => this.loadPage());
  }

  delete(allocation: ILeaveAllocation): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.leaveAllocationService
          .delete(allocation?.id!).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.loadAll();
          },
          () => swalOnRequestError()
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

  protected onSuccess(data: ILeaveAllocation[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/leave-allocation'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.leaveAllocations = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  loadAll(): void {
    this.leaveAllocationService.query().subscribe((res: HttpResponse<ILeaveAllocation[]>) => (this.leaveAllocations = res.body || []));
  }
}
