import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, ParamMap, Router, Data } from '@angular/router';
import { Subscription, combineLatest } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import {ILeaveBalance} from "../leave-balance.model";
import {IEmployee} from "../../employee/employee.model";
import {LeaveBalanceService} from "../service/leave-balance.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {EmployeeService} from "../../employee/service/employee.service";
import {
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DELETE_CONFIRMATION
} from "../../../shared/swal-common/swal.properties.constant";
import {DANGER_COLOR, PRIMARY_COLOR} from "../../../config/color.code.constant";
import {swalOnDeleteSuccess, swalOnRequestError} from "../../../shared/swal-common/swal-common";
import {LeaveBalance} from "../leave-balance.module";

@Component({
  selector: 'jhi-leave-balance',
  templateUrl: './leave-balance.component.html',
})
export class LeaveBalanceComponent implements OnInit, OnDestroy {
  leaveBalances?: ILeaveBalance[];

  eventSubscriber?: Subscription;
  employees: IEmployee[] = [];
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  years: number[];

  searchParamsForm = this.fb.group({
    year: [null],
    leaveType: [null],
    employeeId: this.fb.group({
      employeeId: new FormControl('', []),
    }),
  });

  constructor(
    protected leaveBalanceService: LeaveBalanceService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected employeeService: EmployeeService,
    protected fb: FormBuilder
  ) {
    const currentYear: number = new Date().getFullYear();
    this.years = [currentYear, currentYear - 1, currentYear - 2, currentYear - 3, currentYear - 4, currentYear - 5];
  }

  get employeeIdForm(): FormGroup {
    return this.searchParamsForm.get('employeeId') as FormGroup;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.leaveBalanceService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<ILeaveBalance[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInLeaveBalances();
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

  trackId(index: number, item: ILeaveBalance): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLeaveBalances(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveBalanceListModification', () => this.loadPage());
  }

  delete(leaveBalance: ILeaveBalance): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.leaveBalanceService.delete(leaveBalance.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            setTimeout(() => {
              this.loadPage();
            }, 1000);
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

  protected onSuccess(data: ILeaveBalance[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/leave-balance'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.leaveBalances = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  private createFromForm(): ILeaveBalance {
    return {
      ...new LeaveBalance(),
      year: this.searchParamsForm.get(['year'])!.value,
      leaveType: this.searchParamsForm.get(['leaveType'])!.value,
      employeeId: this.employeeIdForm.get(['employeeId'])!.value,
    };
  }

  search(): void {
    // search api
    const pageToLoad: number = this.page || 1;
    const options = {
      page: pageToLoad - 1,
      size: this.itemsPerPage,
      sort: this.sort(),
    };
    const nomineeFilter = this.createFromForm();
    this.leaveBalanceService.filter(nomineeFilter, options).subscribe(res => {
      this.leaveBalances = res.body!;
    });
  }

  reset(): void {
    /* if value change in any field, then reset */
    if (
      this.searchParamsForm.get('year')!.value ||
      this.searchParamsForm.get('leaveType')!.value ||
      this.searchParamsForm.get('employeeId')!.get('employeeId')!.value
    ) {
      this.searchParamsForm.reset();
      this.loadPage();
    }
  }
}
