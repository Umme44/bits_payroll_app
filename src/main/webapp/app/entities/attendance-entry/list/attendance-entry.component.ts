import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse, HttpHeaders } from '@angular/common/http';
import { Subscription, combineLatest } from 'rxjs';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import Swal from 'sweetalert2';
import dayjs from 'dayjs/esm';
import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { Router, ActivatedRoute, Data, ParamMap } from '@angular/router';
import {IAttendanceEntry} from "../attendance-entry.model";
import {IEmployee} from "../../employee-custom/employee-custom.model";
import {AttendanceEntryService} from "../service/attendance-entry.service";
import {EventManager} from "../../../core/util/event-manager.service";
import {EmployeeService} from "../../employee/service/employee.service";
import {SWAL_CANCEL_BTN_TEXT, SWAL_CONFIRM_BTN_TEXT, SWAL_DELETE_CONFIRMATION} from "../../../shared/swal-common/swal.properties.constant";
import {DANGER_COLOR, PRIMARY_COLOR} from "../../../config/color.code.constant";
import {swalOnDeleteSuccess, swalOnRequestError} from "../../../shared/swal-common/swal-common";

@Component({
  selector: 'jhi-attendance-entry',
  templateUrl: './attendance-entry.component.html',
})
export class AttendanceEntryComponent implements OnInit, OnDestroy {
  attendanceEntries?: IAttendanceEntry[];
  eventSubscriber?: Subscription;
  employees: IEmployee[] = [];
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;
  isInvalid = false;
  today = new Date();
  monthFromToday!: Date;

  searchParamsForm = this.fb.group({
    startDate: new FormControl('', []),
    endDate: new FormControl('', []),
    employeeId: this.fb.group({
      employeeId: new FormControl('', []),
    }),
    leaveType: new FormControl('', []),
  });

  editForm = this.fb.group({
    startDate: [new Date()],
    endDate: [],
  });

  constructor(
    protected attendanceEntryService: AttendanceEntryService,
    protected activatedRoute: ActivatedRoute,
    protected router: Router,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected employeeService: EmployeeService,
    private fb: FormBuilder
  ) {
    this.today = new Date();
    this.monthFromToday = new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000);
  }

  get employeeIdForm(): FormGroup {
    return this.searchParamsForm.get('employeeId') as FormGroup;
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;

    this.attendanceEntryService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        (res: HttpResponse<IAttendanceEntry[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.registerChangeInAttendanceEntries();

    //this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));
    this.setInitialDateInDatePicker();
  }
  setInitialDateInDatePicker(): void {
    this.searchParamsForm.controls['endDate'].setValue(this.today.toDateString());
    this.searchParamsForm.controls['startDate'].setValue(this.monthFromToday.toDateString());
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

  trackId(index: number, item: IAttendanceEntry): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInAttendanceEntries(): void {
    this.eventSubscriber = this.eventManager.subscribe('attendanceEntryListModification', () => this.loadPage());
  }

  delete(attendanceEntry: IAttendanceEntry): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.attendanceEntryService.delete(attendanceEntry.id!).subscribe(
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

  getEmployeeById(id: number): IEmployee {
    return this.employees.find(x => x.id === id)!;
  }
  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected onSuccess(data: IAttendanceEntry[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/attendance-entry'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.attendanceEntries = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  loadAfterSearching(page?: number, dontNavigate?: boolean): void {
    const pageToLoad: number = page || this.page || 1;
    const startDate1: string = this.searchParamsForm.get('startDate')!.value
      ? dayjs(this.searchParamsForm.get('startDate')!.value).format('YYYY-MM-DD')
      : '';
    const endDate1: string = this.searchParamsForm.get('endDate')!.value
      ? dayjs(this.searchParamsForm.get('endDate')!.value).format('YYYY-MM-DD')
      : '';

    this.attendanceEntryService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        employeeId: this.searchParamsForm.get('employeeId')!.value.employeeId ?? '',
        startDate: startDate1,
        endDate: endDate1,
      })
      .subscribe(
        (res: HttpResponse<IAttendanceEntry[]>) => this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate),
        () => this.onError()
      );
  }

  checkDate(): void {
    const startDate = this.searchParamsForm.get(['startDate'])!.value;
    const endDate = this.searchParamsForm.get(['endDate'])!.value;

    if (startDate !== undefined && endDate !== undefined && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }
}
