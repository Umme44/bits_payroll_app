import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import dayjs from 'dayjs/esm';
import { UserLeaveApplicationService } from 'app/payroll-management-system/user-leave-application/user-leave-application.service';
import { CustomValidator } from '../../validators/custom-validator';
import { LeaveType } from '../../shared/model/enumerations/leave-type.model';
import { LeaveSummaryEndUserViewService } from '../../attendance-management-system/leave-summary-end-user-view/leave-summary-end-user-view.component.service';

import {
  AttendanceTimeSheetService,
  HAS_APPROVED_LEAVE_MSG,
  HAS_MOVEMENT_ENTRY_MSG,
  HAS_PENDING_LEAVE_APPLICATION_MSG,
  HAS_PENDING_MANUAL_ATTENDANCE_MSG,
  HAS_PENDING_MOVEMENT_ENTRY_MSG,
  HAS_PRESENT_STATUS_MSG,
} from 'app/attendance-management-system/ats/attendance-time-sheet/attendance-time-sheet.service';
import { IDateRangeDTO } from 'app/shared/model/DateRangeDTO';
import { IAttendanceTimeSheet } from 'app/shared/model/attendance-time-sheet.model';
import {
  swalForErrorWithMessage,
  swalForWarningWithMessage,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalSuccessWithMessage,
} from 'app/shared/swal-common/swal-common';
import { swalConfirmationCommon } from 'app/shared/swal-common/swal-confirmation.common';
import { EmployeeCommonService } from 'app/common/employee-address-book/employee-common.service';
import { ConfigService } from '../../shared/legacy/legacy-service/config.service';
import { DATE_FORMAT } from '../../config/input.constants';
import { EventManager } from '../../core/util/event-manager.service';
import { DefinedKeys } from '../../config/defined-keys.constant';
import { ILeaveApplication, LeaveApplication } from '../../shared/legacy/legacy-model/leave-application.model';
import { LeaveApplicationService } from '../../shared/legacy/legacy-service/leave-application.service';
import { ILeaveBalanceEndUserView } from '../../shared/legacy/legacy-model/leave-balance-end-user-view.model';
import { IEmployee } from '../../entities/employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-user-leave-application',
  templateUrl: './user-leave-application.component.html',
  styleUrls: ['user-leave-application.component.scss'],
})
export class UserLeaveApplicationComponent implements OnInit, OnDestroy {
  currentEmployee!: IEmployee;
  isSaving = false;
  startDateDp: any;
  endDateDp: any;

  startRange?: dayjs.Dayjs;
  endRange?: dayjs.Dayjs;
  durationInDays: Number = 0;
  durationWithoutCalc: Number = 0;
  isDateInvalid = false;
  isYearInvalid = false;
  isConflict = false;
  isDaysEqualZero = false;

  leaveBalanceEndUserView?: ILeaveBalanceEndUserView[];
  attendanceTimeSheet?: IAttendanceTimeSheet;
  hasAnyConflict!: boolean;
  conflictMessage = '';
  eventSubscriber?: Subscription;

  annualLeave = 0;
  casualLeave = 0;
  compensatoryLeave = 0;
  pandemicLeave = 0;
  paternityLeave = 0;
  maternityLeave = 0;

  editForm = this.fb.group({
    id: [],
    applicationDate: [],
    leaveType: new FormControl('', [Validators.required]),
    description: new FormControl('', [Validators.maxLength(255)]),
    startDate: new FormControl('', [Validators.required]),
    endDate: new FormControl('', [Validators.required]),
    isLineManagerApproved: [],
    isHRApproved: [],
    isRejected: [],
    rejectionComment: new FormControl('', [Validators.maxLength(255)]),
    isHalfDay: [],
    durationInDay: new FormControl('', [Validators.min(1)]),
    phoneNumberOnLeave: new FormControl('', [Validators.required, Validators.pattern('^(?:\\+88|88)?(01[3-9]\\d{8})$')]),
    addressOnLeave: new FormControl(''),
    reason: new FormControl('', [Validators.required, Validators.maxLength(255), CustomValidator.notOnlyWhitespace, CustomValidator.naturalTextValidator() ]),
    employeeId: [],
  });

  constructor(
    protected leaveSummaryEndUserViewService: LeaveSummaryEndUserViewService,
    protected eventManager: EventManager,
    protected leaveApplicationService: LeaveApplicationService,
    protected userLeaveApplicationService: UserLeaveApplicationService,
    protected attendanceTimeSheetService: AttendanceTimeSheetService,
    protected employeeCommonService: EmployeeCommonService,
    protected activatedRoute: ActivatedRoute,
    private router: Router,
    private fb: FormBuilder,
    private configService: ConfigService
  ) {}

  ngOnInit(): void {
    // if leave application is disabled rollback
    this.configService.findByKeyCommon(DefinedKeys.is_leave_application_enabled_for_user_end).subscribe(res => {
      if (res.body!.value === 'FALSE') {
        swalForErrorWithMessage('Leave application is temporary disabled. Please try again later.');
        window.history.back();
      }
    });

    this.editForm.controls['durationInDay'].disable();

    const leaveApplicationId = this.activatedRoute.snapshot.params['id'];
    if (leaveApplicationId !== null && leaveApplicationId !== undefined) {
      this.userLeaveApplicationService.findUserLeaveApplication(leaveApplicationId).subscribe(res => {
        this.updateForm(res.body!);
        this.durationInDays = res.body!.durationInDay!;
      });
    } else {
      this.employeeCommonService.employeeInfoForLeaveApply().subscribe(res => {
        this.currentEmployee = res.body!;
        if (!this.currentEmployee.gender) swalForErrorWithMessage('Employee gender is undefined', 5000);
        this.editForm.controls['phoneNumberOnLeave'].setValue(res.body!.officialContactNo);
      });
    }
    this.loadMyLeaveBalance();

    if (
      sessionStorage.getItem('userLeaveApplicationApplyDate') !== null &&
      sessionStorage.getItem('userLeaveApplicationApplyDate') !== undefined
    ) {
      this.setLeaveApplicationDateFromRoute();
    }
  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('userLeaveApplicationApplyDate');
  }

  setLeaveApplicationDateFromRoute(): void {
    const day = dayjs(sessionStorage.getItem('userLeaveApplicationApplyDate'));
    this.editForm.controls['startDate'].setValue(day as any);
    this.editForm.controls['endDate'].setValue(day as any);
    /* check validation */
    this.loadDuration();
  }

  updateForm(leaveApplication: ILeaveApplication): void {
    this.editForm.patchValue({
      id: leaveApplication.id,
      applicationDate: leaveApplication.applicationDate,
      leaveType: leaveApplication.leaveType,
      description: leaveApplication.description,
      startDate: leaveApplication.startDate as any,
      endDate: leaveApplication.endDate as any,
      isLineManagerApproved: leaveApplication.isLineManagerApproved,
      isHRApproved: leaveApplication.isHRApproved,
      isRejected: leaveApplication.isRejected,
      rejectionComment: leaveApplication.rejectionComment,
      isHalfDay: leaveApplication.isHalfDay,
      durationInDay: leaveApplication.durationInDay as any,
      phoneNumberOnLeave: leaveApplication.phoneNumberOnLeave,
      addressOnLeave: leaveApplication.addressOnLeave,
      reason: leaveApplication.reason,
      employeeId: leaveApplication.employeeId,
    });
  }

  previousState(): void {
    this.router.navigate(['/user-leave-application-status-and-history']);
  }

  save(): void {
    const leaveApplication = this.createFromForm();
    // pre-validate casual leave
    if (leaveApplication.leaveType === LeaveType.MENTIONABLE_CASUAL_LEAVE) {
      this.userLeaveApplicationService
        .calculateUserCasualLeaveRemaining(
          dayjs(leaveApplication.startDate).format(DATE_FORMAT),
          dayjs(leaveApplication.endDate).format(DATE_FORMAT),
          leaveApplication.id
        )
        .subscribe(res => {
          if (res.body! === false) {
            swalForWarningWithMessage('You can not apply more than 02 casual leave within a month.', 2000);
          } else {
            swalConfirmationCommon().then(result => {
              if (result.isConfirmed) {
                this.isSaving = true;
                if (leaveApplication.id !== undefined && leaveApplication.id !== null) {
                  this.subscribeToSaveResponse(this.leaveApplicationService.userLeaveApplicationUpdate(leaveApplication));
                } else {
                  this.subscribeToSaveResponse(this.leaveApplicationService.userLeaveApplication(leaveApplication));
                }
              }
            });
          }
        });
    } else {
      swalConfirmationCommon().then(result => {
        if (result.isConfirmed) {
          this.isSaving = true;
          if (leaveApplication.id !== undefined && leaveApplication.id !== null) {
            this.subscribeToSaveResponse(this.leaveApplicationService.userLeaveApplicationUpdate(leaveApplication));
          } else {
            this.subscribeToSaveResponse(this.leaveApplicationService.userLeaveApplication(leaveApplication));
          }
        }
      });
    }
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  loadDuration(): void {
    this.startRange = this.editForm.get(['startDate'])!.value;
    this.endRange = this.editForm.get(['endDate'])!.value;
    this.isConflict = false;
    this.isDateInvalid = false;
    this.isYearInvalid = false;
    this.isDaysEqualZero = false;
    this.hasAnyConflict = false;
    this.conflictMessage = '';

    if (
      this.editForm.get(['startDate'])!.value !== null &&
      this.editForm.get(['endDate'])!.value !== null &&
      this.editForm.get(['startDate'])!.value !== '' &&
      this.editForm.get(['endDate'])!.value !== ''
    ) {
      if (this.startRange !== undefined && this.endRange !== undefined) {
        const startYear = this.startRange.year();
        const endYear = this.endRange.year();
        if (startYear !== endYear) {
          this.isYearInvalid = true;
          return;
        }

        this.durationWithoutCalc = -this.startRange.diff(this.endRange, 'days') + 1;
        if (this.durationWithoutCalc < 1) {
          this.isDateInvalid = true;
          return;
        }

        const startDate: string = this.startRange.format('YYYY-MM-DD');
        const endDate: string = this.endRange.format('YYYY-MM-DD');

        if (startDate !== null && endDate !== null) {
          const leaveApplication = this.createFromForm();

          /* setting leave type, for avoiding leave type parsing error */
          leaveApplication.leaveType = LeaveType.OTHER;
          this.userLeaveApplicationService.calculateUserLeaveDuration(leaveApplication).subscribe(resp => {
            this.durationInDays = resp.body!;
            this.editForm.controls['durationInDay'].setValue(this.durationInDays as any);

            if (this.durationInDays < 0) {
              this.isConflict = true;
              this.durationInDays = 0;
              this.editForm.controls['durationInDay'].setValue(this.durationInDays as any);
              this.isDateInvalid = true;
              return;
            } else if (this.durationInDays === 0) {
              this.isDaysEqualZero = true;
              this.editForm.controls['durationInDay'].setValue(this.durationInDays as any);
              return;
            } else {
              this.findAnyConflictWithOthers();
            }
          });
        }
      }
    }
  }

  findAnyConflictWithOthers(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;
    const dateRange: IDateRangeDTO = { startDate, endDate };
    this.attendanceTimeSheetService.findApplicationsByDateRange(dateRange).subscribe(res => {
      this.attendanceTimeSheet = res.body!;
      this.conflictMessage = this.showConflictMessage(this.attendanceTimeSheet);
      if (this.conflictMessage.length > 0) {
        this.hasAnyConflict = true;
        this.editForm.controls['durationInDay'].setValue(0 as any);
      } else {
        this.hasAnyConflict = false;
      }
    });
  }

  findConflictWithCasualLeave(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;
    const leaveType = this.editForm.get(['leaveType'])!.value;
    const startDateMoment = dayjs(startDate);
    const endDateMoment = dayjs(endDate);
    const duration = endDateMoment.diff(startDateMoment, 'days');

    if (leaveType === 'MENTIONABLE_CASUAL_LEAVE') {
      this.hasAnyConflict = true;
      this.conflictMessage = 'You can not apply more than two casual leave within a month.';
      swalForWarningWithMessage('You can not apply more than two casual leave within a month.', 2000);
    }
  }

  showConflictMessage(attendanceTimeSheet: IAttendanceTimeSheet): string {
    if (attendanceTimeSheet.hasPendingManualAttendance) {
      return HAS_PENDING_MANUAL_ATTENDANCE_MSG;
    } else if (attendanceTimeSheet.hasPendingMovementEntry) {
      return HAS_PENDING_MOVEMENT_ENTRY_MSG;
    } else if (attendanceTimeSheet.hasPendingLeaveApplication && !this.editForm.get('id')!.value) {
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    swalSuccessWithMessage('Applied');
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(err: any): void {
    this.isSaving = false;
    swalOnRequestErrorWithBackEndErrorTitle(err.error.title, 5000);
  }

  private createFromForm(): ILeaveApplication {
    return {
      ...new LeaveApplication(),
      id: this.editForm.get(['id'])!.value,
      applicationDate: this.editForm.get(['applicationDate'])!.value,
      leaveType: this.editForm.get(['leaveType'])!.value,
      description: this.editForm.get(['description'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      isLineManagerApproved: this.editForm.get(['isLineManagerApproved'])!.value,
      isHRApproved: this.editForm.get(['isHRApproved'])!.value,
      isRejected: this.editForm.get(['isRejected'])!.value,
      rejectionComment: this.editForm.get(['rejectionComment'])!.value,
      isHalfDay: this.editForm.get(['isHalfDay'])!.value,
      durationInDay: this.editForm.get(['durationInDay'])!.value,
      phoneNumberOnLeave: this.editForm.get(['phoneNumberOnLeave'])!.value,
      addressOnLeave: this.editForm.get(['addressOnLeave'])!.value,
      reason: this.editForm.get(['reason'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
    };
  }

  loadMyLeaveBalance(): void {
    this.leaveSummaryEndUserViewService.loadByYear(0).subscribe((resp: HttpResponse<ILeaveBalanceEndUserView[]>) => {
      this.leaveBalanceEndUserView = resp.body || [];
      this.annualLeave =
        this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.MENTIONABLE_ANNUAL_LEAVE)[0]?.daysRemaining || 0;
      this.casualLeave =
        this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.MENTIONABLE_CASUAL_LEAVE)[0]?.daysRemaining || 0;
      this.compensatoryLeave =
        this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.NON_MENTIONABLE_COMPENSATORY_LEAVE)[0]?.daysRemaining || 0;
      this.pandemicLeave =
        this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.NON_MENTIONABLE_PANDEMIC_LEAVE)[0]?.daysRemaining || 0;
      this.paternityLeave =
        this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.NON_MENTIONABLE_PATERNITY_LEAVE)[0]?.daysRemaining || 0;
      this.maternityLeave =
        this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.NON_MENTIONABLE_MATERNITY_LEAVE)[0]?.daysRemaining || 0;
    });
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    if (!this.editForm.get('id')!.value) {
      this.editForm.reset();
    }
  }
}
