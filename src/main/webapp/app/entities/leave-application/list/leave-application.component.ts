import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {LeaveApplicationService} from "../service/leave-application.service";
import {DataUtils} from "../../../core/util/data-util.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {ILeaveApplication} from "../leave-application.model";
import {swalOnDeleteConfirmation} from "../../../shared/swal-common/swal-common";
import {DATE_FORMAT} from "../../../config/input.constants";

@Component({
  selector: 'jhi-leave-application',
  templateUrl: './leave-application.component.html',
})
export class LeaveApplicationComponent implements OnInit, OnDestroy {
  leaveApplications: ILeaveApplication[] = [];
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  startDateDp: any;
  endDateDp: any;
  isInvalid = false;
  employeeId?: any | null = null;

  editForm = this.fb.group({
    startDate: new FormControl('', []),
    endDate: new FormControl('', []),
    employeeId: this.fb.group({
      employeeId: new FormControl('', []),
    }),
    leaveType: new FormControl('ALL', []),
  });

  constructor(
    protected leaveApplicationService: LeaveApplicationService,
    protected dataUtils: DataUtils,
    private fb: FormBuilder,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInLeaveApplications();
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

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    const startDate1 = this.editForm.get('startDate')!.value ? dayjs(this.editForm.get('startDate')!.value).format('YYYY-MM-DD') : '';
    const endDate1 = this.editForm.get('endDate')!.value ? dayjs(this.editForm.get('endDate')!.value).format('YYYY-MM-DD') : '';
    const leaveType1 = this.editForm.get(['leaveType'])!.value;
    const pageToLoad: number = page || this.page || 1;

    this.leaveApplicationService
      .queryWithEmployeeAndDatesAndLeaveType({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        employeeId: this.employeeId ?? '',
        startDate: startDate1,
        endDate: endDate1,
        leaveType: !leaveType1 || leaveType1 === 'ALL' ? null : leaveType1,
      })
      .subscribe(
        (res: HttpResponse<ILeaveApplication[]>) => this.onSuccess(res.body, res.headers, pageToLoad, true),
        () => this.onError()
      );
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ILeaveApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLeaveApplications(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveApplicationListModification', () => this.loadPage());
  }

  delete(leaveApplication: ILeaveApplication): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.leaveApplicationService.delete(leaveApplication.id!).subscribe(() => {
          this.eventManager.broadcast('leaveApplicationListModification');
        });
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

  checkDate(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;

    if (startDate && endDate && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
      this.search();
    }
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  protected onSuccess(data: ILeaveApplication[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/leave-application'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.leaveApplications = data || [];
    this.ngbPaginationPage = this.page;
  }

  search(): void {
    const leaveType1 = this.editForm.get('leaveType')!.value;
    const pageToLoad = 1;

    const startDate = this.editForm.get('startDate')!.value;
    const endDate = this.editForm.get('endDate')!.value;

    if ((!startDate && endDate) || (startDate && !endDate)) {
      return;
    }

    this.leaveApplicationService
      .queryWithEmployeeAndDatesAndLeaveType({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        employeeId: this.employeeIdForm.get(['employeeId'])!.value ?? null,
        startDate: startDate ? dayjs(startDate).format(DATE_FORMAT) : undefined,
        endDate: endDate ? dayjs(endDate).format(DATE_FORMAT) : undefined,
        leaveType: !leaveType1 || leaveType1 === 'ALL' ? null : leaveType1,
      })
      .subscribe(
        (res: HttpResponse<ILeaveApplication[]>) => this.onSuccess(res.body, res.headers, pageToLoad, true),
        () => this.onError()
      );
  }

  reset(): void {
    this.editForm.reset();
    this.handleNavigation();
  }
}
