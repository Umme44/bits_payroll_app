import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';
import { ManualAttendanceEntryCommonService } from './manual-attendance-entry-common.service';
import { IAttendanceTimeSheet } from '../../shared/model/attendance-time-sheet.model';
import { DATE_FORMAT, TIME_FORMAT } from '../../config/input.constants';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { ParseLinks } from '../../core/util/parse-links.service';
import { ITEMS_PER_PAGE } from '../../config/pagination.constants';

import { IDateRangeDTO } from '../../shared/model/DateRangeDTO';
import {
  AttendanceTimeSheetService,
  HAS_APPROVED_LEAVE_MSG,
  HAS_MOVEMENT_ENTRY_MSG,
  HAS_PENDING_LEAVE_APPLICATION_MSG,
  HAS_PENDING_MOVEMENT_ENTRY_MSG,
} from '../ats/attendance-time-sheet/attendance-time-sheet.service';
import {
  swalOnAppliedSuccess,
  swalOnDeleteConfirmation,
  swalOnDeleteSuccess,
  swalOnRequestError,
} from '../../shared/swal-common/swal-common';
import { IManualAttendanceEntry, ManualAttendanceEntry } from '../../shared/legacy/legacy-model/manual-attendance-entry.model';
import { AttendanceEntryService } from '../../shared/legacy/legacy-service/attendance-entry.service';
import { IAttendanceEntry } from '../../shared/legacy/legacy-model/attendance-entry.model';

@Component({
  selector: 'jhi-manual-attendance-entry-update',
  templateUrl: './manual-attendance-entry-update.component.html',
})
export class ManualAttendanceEntryUpdateComponent implements OnInit, OnDestroy {
  isSaving = false;
  isEditing = false;
  dateDp: any;
  isInvalid = false;
  isCurrentDateSelected = false;

  manualAttendanceEntry!: ManualAttendanceEntry;
  manualAttendanceEntries: IManualAttendanceEntry[] = [];

  hasAnyConflict!: boolean;
  conflictMessage!: string;
  attendanceTimeSheet!: IAttendanceTimeSheet;

  editForm = this.fb.group({
    id: [],
    date: new FormControl(null, [Validators.required]),
    inTime: new FormControl(null, [Validators.required]),
    inNote: new FormControl('', [Validators.maxLength(255)]),
    outTime: new FormControl(null, [Validators.required]),
    outNote: new FormControl('', [Validators.maxLength(255)]),
    note: new FormControl('', [Validators.required, Validators.maxLength(250)]),
    status: [],
    isLineManagerApproved: [],
    isHRApproved: [],
    isRejected: [],
    rejectionComment: [],
    employeeId: [],
    startDate: [],
    endDate: [],
  });

  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected manualAttendanceEntryService: ManualAttendanceEntryCommonService,
    protected attendanceEntryService: AttendanceEntryService,
    public attendanceTimeSheetService: AttendanceTimeSheetService,
    public activatedRoute: ActivatedRoute,
    public router: Router,
    private fb: FormBuilder,
    protected parseLinks: ParseLinks
  ) {
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'date';
    this.ascending = false;
  }

  ngOnInit(): void {
    this.loadManualAttendanceEntries();
    if (
      sessionStorage.getItem('manualAttendanceEntryApplyDate') !== null &&
      sessionStorage.getItem('manualAttendanceEntryApplyDate') !== undefined
    ) {
      this.loadAttendanceDataFromDate();
    }

    // for today, out time is not required
    if (this.editForm.get('date')!.value !== null && this.editForm.get('date')!.value !== undefined) {
      const fromDate = this.editForm.get('date')!.value;
      const today = dayjs().startOf('day');
      const startDateDurationFromToday = dayjs.duration(today.diff(fromDate)).asDays();
      if (startDateDurationFromToday === 0) {
        this.editForm.get(['outTime'])!.clearValidators();
        this.editForm.get(['outTime'])!.updateValueAndValidity();
      }
    }
  }

  loadAttendanceDataFromDate(): void {
    const day = dayjs(sessionStorage.getItem('manualAttendanceEntryApplyDate'));
    this.editForm.controls['date'].setValue(day);
    const today = dayjs().startOf('day').format(DATE_FORMAT);
    const currentDate = day.format(DATE_FORMAT);
    if (currentDate === today) {
      this.isCurrentDateSelected = true;
    }
    this.attendanceEntryService.findInOrOutTimeByDate(day).subscribe((res: HttpResponse<IAttendanceEntry>) => {
      if (res.body!.inTime) {
        this.isEditing = true;
        this.editForm.patchValue({
          inTime: res.body!.inTime ? res.body!.inTime.format(TIME_FORMAT) : null,
        });
      }

      if (res.body!.outTime) {
        this.isEditing = true;
        this.editForm.patchValue({
          outTime: res.body!.outTime ? res.body!.outTime.format(TIME_FORMAT) : null,
        });
      }
    });
  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('manualAttendanceEntryApplyDate');
  }

  updateForm(manualAttendanceEntry: IManualAttendanceEntry, day?: any): void {
    this.editForm.patchValue({
      id: manualAttendanceEntry.id,
      date: manualAttendanceEntry.date ?? day,
      inTime: manualAttendanceEntry.inTime ? manualAttendanceEntry.inTime.format(TIME_FORMAT) : null,
      inNote: manualAttendanceEntry.inNote,
      outTime: manualAttendanceEntry.outTime ? manualAttendanceEntry.outTime.format(TIME_FORMAT) : null,
      outNote: manualAttendanceEntry.outNote,
      note: manualAttendanceEntry.note,
      isLineManagerApproved: manualAttendanceEntry.isLineManagerApproved,
      isHRApproved: manualAttendanceEntry.isHRApproved,
      isRejected: manualAttendanceEntry.isRejected,
      rejectionComment: manualAttendanceEntry.rejectionComment,
      employeeId: manualAttendanceEntry.employeeId,
    });
  }

  convertToMomentForUpdate(entry: IManualAttendanceEntry): void {
    entry.date = dayjs(entry.date);
    entry.inTime = dayjs(entry.inTime);

    if (this.editForm.get('date')!.value !== null && this.editForm.get('date')!.value !== undefined) {
      const today = dayjs().startOf('day');
      const startDateDurationFromToday = dayjs.duration(today.diff(entry.date)).asDays();
      if (startDateDurationFromToday === 0) {
        this.isCurrentDateSelected = true;
        this.editForm.get(['outTime'])!.clearValidators();
        this.editForm.get(['outTime'])!.updateValueAndValidity();
      } else {
        this.isCurrentDateSelected = false;
        this.editForm.get(['outTime'])!.setValidators(Validators.required);
      }
    } else if (this.editForm.get('date')!.value === null) {
      const today = dayjs().startOf('day');
      const startDateDurationFromToday = dayjs.duration(today.diff(entry.date)).asDays();
      if (startDateDurationFromToday === 0) {
        this.isCurrentDateSelected = true;
        this.editForm.get(['outTime'])!.clearValidators();
        this.editForm.get(['outTime'])!.updateValueAndValidity();
      } else {
        this.isCurrentDateSelected = false;
        this.editForm.get(['outTime'])!.setValidators(Validators.required);
      }
    }

    if (entry.outTime === undefined || entry.outTime === null) {
      entry.outTime = undefined;
    } else {
      entry.outTime = dayjs(entry.outTime);
    }

    /* reset conflict validation */
    this.resetConflictMessage();
    this.findAnyConflict(entry.date);

    this.updateForm(entry);
  }

  loadManualAttendanceEntries(page?: number): void {
    if (page) {
      this.page = page;
    }

    this.manualAttendanceEntryService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe(
        res => {
          this.paginateManualAttendanceEntry(res.body, res.headers);
        },
        () => swalOnRequestError()
      );
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'date') {
      result.push('date');
    }
    return result;
  }

  protected paginateManualAttendanceEntry(data: IManualAttendanceEntry[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.manualAttendanceEntries.push(data[i]);
      }
    }
  }

  reset(): void {
    this.manualAttendanceEntries = [];
    this.loadManualAttendanceEntries(0);
  }

  previousState(): void {
    window.history.back();
  }

  submit(): void {
    this.save();
  }

  save(): void {
    this.isSaving = true;
    const manualAttendanceEntry = this.createFromForm();
    if (!manualAttendanceEntry.id) {
      this.subscribeToSaveApplyResponse(this.manualAttendanceEntryService.create(manualAttendanceEntry));
    } else {
      this.subscribeToSaveApplyResponse(this.manualAttendanceEntryService.update(manualAttendanceEntry));
    }
  }

  private createFromForm(): IManualAttendanceEntry {
    return {
      ...new ManualAttendanceEntry(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      inTime: this.editForm.get(['inTime'])!.value ? dayjs(this.editForm.get(['inTime'])!.value, TIME_FORMAT) : undefined,
      inNote: this.editForm.get(['inNote'])!.value,
      outTime: this.editForm.get(['outTime'])!.value ? dayjs(this.editForm.get(['outTime'])!.value, TIME_FORMAT) : undefined,
      outNote: this.editForm.get(['outNote'])!.value,
      note: this.editForm.get(['note'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
    };
  }

  protected subscribeToSaveApplyResponse(result: Observable<HttpResponse<IManualAttendanceEntry>>): void {
    result.subscribe(
      () => this.onSavingApplySuccess(),
      () => this.onSaveError()
    );
  }

  protected onSavingApplySuccess(): void {
    this.isSaving = false;
    swalOnAppliedSuccess();

    this.reset();
    this.editForm.reset();
  }

  protected onSaveError(): void {
    swalOnRequestError();
    this.isSaving = false;
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  checkTime(): void {
    const inTime = this.editForm.get(['inTime'])!.value;
    const outTime = this.editForm.get(['outTime'])!.value;

    if (inTime && outTime && inTime > outTime) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  trackId(index: number, item: IManualAttendanceEntry): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  textSlicing(note: any): string {
    if (note !== null && note !== undefined) {
      note = note.toString() + '';
      return note.length >= 100 ? note.slice(0, 100) + '...' : note;
    } else {
      return '';
    }
  }

  delete(entry: IManualAttendanceEntry): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.manualAttendanceEntryService.delete(entry.id!).subscribe(
          () => {
            swalOnDeleteSuccess();
            this.reset();
          },
          () => swalOnRequestError()
        );
      }
    });
  }

  findAnyConflict(dateFromParam?: any): void {
    const date = dateFromParam ?? this.editForm.get('date')!.value;
    if (date) {
      // find Attendance, Leave and Movement Entry applications between start and end date
      const dateRange: IDateRangeDTO = { startDate: date, endDate: date };

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
  }

  showConflictMessage(attendanceTimeSheet: IAttendanceTimeSheet): string {
    if (attendanceTimeSheet.hasPendingMovementEntry) {
      return HAS_PENDING_MOVEMENT_ENTRY_MSG;
    } else if (attendanceTimeSheet.hasPendingLeaveApplication) {
      return HAS_PENDING_LEAVE_APPLICATION_MSG;
    } /*else if (attendanceTimeSheet.hasPresentStatus && !this.editForm.get('id')!.value &&
      attendanceTimeSheet.inTime !== undefined && attendanceTimeSheet.inTime !== null
      && attendanceTimeSheet.outTime !== undefined && attendanceTimeSheet.outTime !==null) {
      return HAS_PRESENT_STATUS_MSG;
    } */ else if (attendanceTimeSheet.hasMovementStatus) {
      return HAS_MOVEMENT_ENTRY_MSG;
    } else if (attendanceTimeSheet.hasLeaveStatus) {
      return HAS_APPROVED_LEAVE_MSG;
    } else {
      return '';
    }
  }

  resetConflictMessage(): void {
    this.hasAnyConflict = false;
    this.conflictMessage = '';
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if (!this.editForm.get('id')!.value) {
      this.editForm.reset();
    }
  }
}
