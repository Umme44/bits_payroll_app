import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute, Data, ParamMap, Router } from '@angular/router';
import { combineLatest, Observable, Subscription } from 'rxjs';
import dayjs from 'dayjs/esm';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { UserMovementEntryService } from './user-movement-entry.service';

import { ITEMS_PER_PAGE } from '../../shared/constants/pagination.constants';

import Swal from 'sweetalert2';
import { SWAL_CANCEL_BTN_TEXT, SWAL_CONFIRM_BTN_TEXT, SWAL_DELETE_CONFIRMATION } from '../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../shared/constants/color.code.constant';
import {
  swalOnAppliedSuccess,
  swalOnDeleteSuccess,
  swalOnRequestError,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnUpdatedSuccess, swalPatternError,
} from '../../shared/swal-common/swal-common';
import { IDateRangeDTO } from '../../shared/model/DateRangeDTO';
import {
  AttendanceTimeSheetService,
  HAS_APPROVED_LEAVE_MSG,
  HAS_MOVEMENT_ENTRY_MSG,
  HAS_PENDING_LEAVE_APPLICATION_MSG,
  HAS_PENDING_MANUAL_ATTENDANCE_MSG,
  HAS_PENDING_MOVEMENT_ENTRY_MSG,
  HAS_PRESENT_STATUS_MSG,
} from '../../attendance-management-system/ats/attendance-time-sheet/attendance-time-sheet.service';
import { IAttendanceTimeSheet } from '../../shared/model/attendance-time-sheet.model';
import { IMovementEntry, MovementEntry } from '../../shared/legacy/legacy-model/movement-entry.model';
import { DATE_TIME_FORMAT } from '../../config/input.constants';
import {CustomValidator} from "../../validators/custom-validator";

@Component({
  selector: 'jhi-user-movement-entry',
  templateUrl: './user-movement-entry.component.html',
  styleUrls: ['./user-movement-entry.component.scss'],
})
export class UserMovementEntryComponent implements OnInit, OnDestroy {
  isSaving = false;
  startDateDp: any;
  endDateDp: any;
  createdAtDp: any;
  updatedAtDp: any;
  sanctionAtDp: any;

  movementEntries: IMovementEntry[] = [];
  attendanceTimeSheet!: IAttendanceTimeSheet;
  eventSubscriber?: Subscription;
  totalItems = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page!: number;
  predicate!: string;
  ascending!: boolean;
  ngbPaginationPage = 1;

  editForm = this.fb.group({
    id: [],
    startDate: [ '' as any , {
      validators: [Validators.required]
    }],
    startTime: [],
    startNote: [''],
    endDate: ['' as any, {
      validators: [Validators.required]
    }],
    endTime: [],
    endNote: [''],
    note: ['', {
      validators: [CustomValidator.naturalTextValidator(),Validators.required]
    }],
    type: [],
    status: [],
    createdAt: [],
    updatedAt: [],
    sanctionAt: [],
    employeeId: [],
    createdById: [],
    updatedById: [],
    sanctionById: [],
  });

  isDateInvalid!: boolean;
  isConflictWithAttendance!: Boolean;
  isConflictWithMovementEntry!: Boolean;
  hasAnyConflict!: boolean;
  conflictMessage = '';
  //limitCrossingOf30Days = false;
  links: any;

  constructor(
    protected userMovementEntryService: UserMovementEntryService,
    protected attendanceTimeSheetService: AttendanceTimeSheetService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected router: Router,
    protected modalService: NgbModal
  ) {
    this.movementEntries = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInMovementEntries();

    this.activatedRoute.data.subscribe(({ movementEntry }) => {
      if (movementEntry !== undefined) {
        const today = dayjs().startOf('day');
        movementEntry.startTime = today;
        movementEntry.endTime = today;
      }
      this.updateForm(movementEntry);

      // get auto date from route
      if (sessionStorage.getItem('movementEntryApplyDate') !== null && sessionStorage.getItem('movementEntryApplyDate') !== undefined) {
        this.setMovementDateFromRoute();
      }
    });
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      // this.eventManager.destroy(this.eventSubscriber);
    }
    sessionStorage.removeItem('movementEntryApplyDate');
  }

  setMovementDateFromRoute(): void {
    const day = dayjs(sessionStorage.getItem('movementEntryApplyDate'));
    this.editForm.controls['startDate'].setValue(day);
    this.editForm.controls['endDate'].setValue(day);
    /* check validation */
    this.checkDate();
  }

  updateForm(movementEntry: IMovementEntry): void {
    this.clearEditForm();
    this.editForm.patchValue({
      id: movementEntry.id,
      startDate: movementEntry.startDate,
      startTime: movementEntry.startTime ? movementEntry.startTime.format(DATE_TIME_FORMAT) : null,
      startNote: movementEntry.startNote,
      endDate: movementEntry.endDate,
      endTime: movementEntry.endTime ? movementEntry.endTime.format(DATE_TIME_FORMAT) : null,
      endNote: movementEntry.endNote,
      note: movementEntry.note,
      type: movementEntry.type,
      status: movementEntry.status,
      createdAt: movementEntry.createdAt,
      updatedAt: movementEntry.updatedAt,
      sanctionAt: movementEntry.sanctionAt,
      employeeId: movementEntry.employeeId,
      createdById: movementEntry.createdById,
      updatedById: movementEntry.updatedById,
      sanctionById: movementEntry.sanctionById,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const movementEntry = this.createFromForm();
    if (movementEntry.id === null || movementEntry.id === undefined) {
      this.subscribeToSaveResponse(this.userMovementEntryService.create(movementEntry));
    } else {
      this.subscribeToSaveResponse(this.userMovementEntryService.update(movementEntry));
    }
  }

  private createFromForm(): IMovementEntry {
    return {
      ...new MovementEntry(),
      id: this.editForm.get(['id'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      startTime: this.editForm.get(['startTime'])!.value ? dayjs(this.editForm.get(['startTime'])!.value, DATE_TIME_FORMAT) : undefined,
      startNote: this.editForm.get(['startNote'])!.value ?? 'startNote',
      endDate: this.editForm.get(['endDate'])!.value,
      endTime: this.editForm.get(['endTime'])!.value ? dayjs(this.editForm.get(['endTime'])!.value, DATE_TIME_FORMAT) : undefined,
      endNote: this.editForm.get(['endNote'])!.value ?? 'endNote',
      note: this.editForm.get(['note'])!.value,
      type: this.editForm.get(['type'])!.value,
      status: this.editForm.get(['status'])!.value,
      createdAt: this.editForm.get(['createdAt'])!.value,
      updatedAt: this.editForm.get(['updatedAt'])!.value,
      sanctionAt: this.editForm.get(['sanctionAt'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
      updatedById: this.editForm.get(['updatedById'])!.value,
      sanctionById: this.editForm.get(['sanctionById'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMovementEntry>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.editForm.reset();
    if (!this.editForm.get('id')!.value) {
      swalOnAppliedSuccess();
    } else {
      swalOnUpdatedSuccess();
    }
    this.reset();
  }

  protected onSaveError(err: any): void {
    this.isSaving = false;
    swalPatternError()
  }

  loadPage(page: number, dontNavigate?: boolean): void {
    this.page = page;
    this.loadAll();
  }

  loadAll(): void {
    this.userMovementEntryService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IMovementEntry[]>) => this.paginateUserMovementEntries(res.body, res.headers));
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

  trackId(index: number, item: IMovementEntry): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  protected paginateUserMovementEntries(data: IMovementEntry[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    // this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.movementEntries.push(data[i]);
      }
    }
  }

  reset(): void {
    this.page = 0;
    this.movementEntries = [];
    this.loadAll();
  }

  registerChangeInMovementEntries(): void {
    // this.eventSubscriber = this.eventManager.subscribe('movementEntryListModification', () => this.reset());
  }

  delete(movementEntry: IMovementEntry): void {
    Swal.fire({
      text: SWAL_DELETE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.userMovementEntryService.delete(movementEntry.id!).subscribe(
          res => {
            swalOnDeleteSuccess();
            this.clearEditForm();
            this.reset();
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

  protected onSuccess(data: IMovementEntry[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/movement-entry/apply'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? 'asc' : 'desc'),
        },
      });
    }
    this.movementEntries = data || [];
    this.ngbPaginationPage = this.page;
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  checkDate(): void {
    this.conflictMessage = '';
    this.hasAnyConflict = false;

    const startDate = this.editForm.get('startDate')!.value;
    const endDate = this.editForm.get('endDate')!.value;

    this.isConflictWithAttendance = false;
    if (startDate && endDate && startDate > endDate) {
      this.isDateInvalid = true;
    } else if (startDate && endDate) {
      this.isDateInvalid = false;

      /* validate, if user is editing same entry */
      if (this.editForm.get('id')!.value) {
        this.userMovementEntryService
          .anyMovementEntryConflictForUpdating(startDate, endDate, { movementEntryId: this.editForm.get('id')!.value })
          .subscribe(movementEntryConflict => {
            if (movementEntryConflict.body! === true) {
              this.conflictMessage = 'You have Movement Application during this date';
              this.hasAnyConflict = true;
            } else {
              // find Attendance, Leave between start and end date
              this.findConflictWithOthers(startDate, endDate);
            }
          });
      } else {
        // find Attendance, Leave between start and end date
        this.findConflictWithOthers(startDate, endDate);
      }
    }
  }

  findConflictWithOthers(startDate: any, endDate: any): void {
    const dateRange: IDateRangeDTO = { startDate, endDate };

    this.attendanceTimeSheetService.findApplicationsByDateRange(dateRange).subscribe(res => {
      this.attendanceTimeSheet = res.body!;
      this.conflictMessage = this.showConflictMessage(this.attendanceTimeSheet);
      if (this.conflictMessage.length > 0) {
        this.hasAnyConflict = true;
      } else {
        this.hasAnyConflict = false;
      }
    });
  }

  textSlicing(startNote: any): string {
    if (startNote) {
      return startNote.slice(0, 100) + '...';
    }
    return '';
  }

  clearEditForm(): void {
    this.editForm.reset();
    this.isConflictWithAttendance = false;
    this.isConflictWithMovementEntry = false;
    this.isDateInvalid = false;
    this.hasAnyConflict = false;
    this.conflictMessage = '';
  }

  showConflictMessage(attendanceTimeSheet: IAttendanceTimeSheet): string {
    if (attendanceTimeSheet.hasPendingManualAttendance) {
      return HAS_PENDING_MANUAL_ATTENDANCE_MSG;
    } else if (attendanceTimeSheet.hasPendingMovementEntry && !this.editForm.get('id')!.value) {
      return HAS_PENDING_MOVEMENT_ENTRY_MSG;
    } else if (attendanceTimeSheet.hasPendingLeaveApplication) {
      return HAS_PENDING_LEAVE_APPLICATION_MSG;
    } else if (attendanceTimeSheet.hasPresentStatus) {
      return HAS_PRESENT_STATUS_MSG;
    } else if (attendanceTimeSheet.hasMovementStatus) {
      return HAS_MOVEMENT_ENTRY_MSG;
    } else if (attendanceTimeSheet.hasLeaveStatus) {
      return HAS_APPROVED_LEAVE_MSG;
    } else {
      return '';
    }
  }
}
