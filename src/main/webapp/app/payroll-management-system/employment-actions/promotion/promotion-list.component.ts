import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl } from '@angular/forms';
import { EmploymentActionsService } from '../employment-actions.service';
import dayjs from 'dayjs/esm';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError } from '../../../shared/swal-common/swal-common';
import { IEmploymentHistory } from '../../../shared/legacy/legacy-model/employment-history.model';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { EventManager } from '../../../core/util/event-manager.service';
import { DATE_FORMAT } from '../../../config/input.constants';
import { IEmployee } from '../../../entities/employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-employment-history',
  templateUrl: './promotion-list.component.html',
})
export class PromotionListComponent implements OnInit, OnDestroy {
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
    startDate: new FormControl(),
    endDate: new FormControl(),
    employeeId: new FormControl(),
  });

  constructor(
    private fb: FormBuilder,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected employmentActionsService: EmploymentActionsService,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.employmentActionsService
      .queryPromotions({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        employeeId: this.editForm.get('employeeId')!.value,
        startDate: this.editForm.get('startDate')!.value ? dayjs(this.editForm.get('startDate')!.value).format(DATE_FORMAT) : undefined,
        endDate: this.editForm.get('endDate')!.value ? dayjs(this.editForm.get('endDate')!.value).format(DATE_FORMAT) : undefined,
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

  checkDate(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;
    this.isInvalid = false;

    // auto fill-up missing date - for avoiding extra date checking
    if (startDate && !endDate) {
      this.editForm.get('endDate')!.setValue(startDate);
    } else if (!startDate && endDate) {
      this.editForm.get('endDate')!.setValue(endDate);
    }

    // validate endDate is before startDate
    if (startDate && endDate && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  trackId(index: number, item: IEmploymentHistory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInEmploymentHistories(): void {
    this.eventSubscriber = this.eventManager.subscribe('employmentHistoryListModification', () => this.loadPage());
  }

  delete(employmentHistory: IEmploymentHistory): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.employmentActionsService.deletePromotions(employmentHistory.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.eventManager.broadcast('employmentHistoryListModification');
          },
          () => {
            swalOnRequestError();
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

  protected onSuccess(data: IEmploymentHistory[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/employment-actions/promotion'], {
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

  onChangeEmployee(employee: IEmployee): void {
    if (employee && employee.id) {
      this.editForm.get('employeeId')!.setValue(employee.id);
    } else {
      this.editForm.get('employeeId')!.setValue(null);
    }
    this.loadPage(1, false);
  }

  reset(): void {
    this.editForm.reset();
    this.isInvalid = false;
    this.loadPage(1, false);
  }
}
