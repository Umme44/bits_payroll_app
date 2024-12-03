import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { TransferDeleteComponent } from './transfer-delete.component';
import { EmploymentActionsService } from '../employment-actions.service';
import dayjs from 'dayjs/esm';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Data, ParamMap, ActivatedRoute, Router } from '@angular/router';
import { IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  selector: 'jhi-employment-history',
  templateUrl: './transfer-list.component.html',
})
export class TransferListComponent implements OnInit, OnDestroy {
  employmentHistories?: IEmploymentHistory[];
  eventSubscriber?: Subscription;

  startRange?: dayjs.Dayjs | null = null;
  endRange?: dayjs.Dayjs | null = null;
  startDateDp: any;
  endDateDp: any;
  isInvalid = false;
  employeeId?: any | null = null;

  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  editForm = this.fb.group({
    startDate: new FormControl('', []),
    endDate: new FormControl('', []),
    employeeId: this.fb.group({
      employeeId: new FormControl('', [Validators.required]),
    }),
  });

  constructor(
    private fb: FormBuilder,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected employmentActionsService: EmploymentActionsService,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.employmentActionsService
      .queryTransfer({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IEmploymentHistory[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInEmploymentHistories();
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

  trackId(index: number, item: IEmploymentHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  checkDate(): void {
    const doj = this.editForm.get(['startDate'])!.value;
    const dor = this.editForm.get(['endDate'])!.value;

    if (doj && dor && doj > dor) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  registerChangeInEmploymentHistories(): void {
    this.eventSubscriber = this.eventManager.subscribe('employmentHistoryListModification', () => this.loadPage());
  }

  delete(employmentHistory: IEmploymentHistory): void {
    const modalRef = this.modalService.open(TransferDeleteComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.employmentHistory = employmentHistory;
  }

  loadSearchedLeaveApplication(): void {
    this.employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    this.startRange = this.editForm.get(['startDate'])!.value;
    this.endRange = this.editForm.get(['endDate'])!.value;

    if (this.employeeId && this.startRange && this.endRange) {
      this.employmentActionsService
        .queryTransferByIdAndDate(this.employeeId, this.startRange, this.endRange)
        .subscribe((res: HttpResponse<IEmploymentHistory[]>) => (this.employmentHistories = res.body || []));
    } else if ((this.employeeId === null || this.employeeId === undefined || this.employeeId === '') && this.startRange && this.endRange) {
      this.employmentActionsService
        .queryTransferwithdate(this.startRange, this.endRange)
        .subscribe((res: HttpResponse<IEmploymentHistory[]>) => (this.employmentHistories = res.body || []));
    } else if (
      this.employeeId &&
      (this.editForm.get(['startDate'])!.value === '' ||
        this.editForm.get(['endDate'])!.value === '' ||
        this.startRange === undefined ||
        this.endRange === undefined)
    ) {
      this.employmentActionsService
        .queryTransferwithemployeeId(this.employeeId)
        .subscribe((res: HttpResponse<IEmploymentHistory[]>) => (this.employmentHistories = res.body || []));
    } else return;

    this.registerChangeInEmploymentHistories();
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IEmploymentHistory[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/employment-actions/transfer'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.employmentHistories = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }
}
