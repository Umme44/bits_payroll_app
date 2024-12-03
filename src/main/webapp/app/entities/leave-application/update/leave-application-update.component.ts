import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable, Subscription } from 'rxjs';

import { LeaveApplicationFormGroup, LeaveApplicationFormService } from './leave-application-form.service';
import { ILeaveApplication } from '../leave-application.model';
import { LeaveApplicationService } from '../service/leave-application.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

import dayjs from 'dayjs/esm';
import { IDateRangeDTO } from '../../../shared/model/DateRangeDTO';
import { ILeaveBalanceEndUserView } from '../../../shared/legacy/legacy-model/leave-balance-end-user-view.model';
import { IAttendanceTimeSheet } from '../../../shared/model/attendance-time-sheet.model';
import {
  HAS_APPROVED_LEAVE_MSG_ADMIN,
  HAS_MOVEMENT_ENTRY_MSG_ADMIN,
  HAS_PENDING_LEAVE_APPLICATION_MSG_ADMIN,
  HAS_PENDING_MANUAL_ATTENDANCE_MSG_ADMIN,
  HAS_PENDING_MOVEMENT_ENTRY_MSG_ADMIN,
  HAS_PRESENT_STATUS_MSG_ADMIN,
} from '../../../shared/service/attendance-time-sheet.service';
import { EmployeeMinimalListType } from '../../../shared/model/enumerations/employee-minimal-list-type.model';
import { swalSuccessWithMessage } from '../../../shared/swal-common/swal-common';
import { LeaveSummaryEndUserViewService } from '../../../attendance-management-system/leave-summary-end-user-view/leave-summary-end-user-view.component.service';
import { LeaveType } from '../../../shared/model/enumerations/leave-type.model';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-leave-application-update',
  templateUrl: './leave-application-update.component.html',
})
export class LeaveApplicationUpdateComponent implements OnInit {
  leaveApplication: ILeaveApplication | null = null;
  //leaveTypeValues = Object.keys(LeaveType);

  employeesSharedCollection: IEmployee[] = [];
  usersSharedCollection: IUser[] = [];

  isSaving = false;
  employees: IEmployee[] = [];
  startDateDp: any;
  endDateDp: any;

  startRange?: dayjs.Dayjs | null;
  endRange?: dayjs.Dayjs | null;
  durationInDays: number = 0;
  durationWithoutCalc: number = 0;
  isDateInvalid = false;
  isYearInvalid = false;
  durationInDay = false;
  selectedEmployee: IEmployee | null = null;

  isConflict = false;
  isDaysEqualZero = false;

  leaveBalanceEndUserView: ILeaveBalanceEndUserView[] = [];
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
  other = 0;

  employeeSelectListType = EmployeeMinimalListType.ACTIVE;

  leaveApplicationYear = new Date().getFullYear();

  editForm: LeaveApplicationFormGroup = this.leaveApplicationFormService.createLeaveApplicationFormGroup();

  constructor(
    protected leaveSummaryEndUserViewService: LeaveSummaryEndUserViewService,
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected leaveApplicationService: LeaveApplicationService,
    protected leaveApplicationFormService: LeaveApplicationFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.editForm.controls['durationInDay'].disable();
    this.activatedRoute.data.subscribe(({ leaveApplication }) => {
      this.leaveApplication = leaveApplication;

      if (leaveApplication.id !== null) {
        this.loadEmployee();
        this.loadDuration();
        this.loadDataByEmployeeAndDate();
      }
      this.updateForm(leaveApplication);

      this.loadRelationshipsOptions();
    });
  }

  loadDuration(): void {
    const employeeId = this.editForm.get(['employeeId'])!.value;
    this.startRange = this.editForm.get(['startDate'])!.value;
    this.endRange = this.editForm.get(['endDate'])!.value;
    this.isConflict = false;
    this.isDateInvalid = false;
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
        if (!employeeId || employeeId === '') {
          return;
        }

        const startYear = this.startRange.year();
        const endYear = this.endRange.year();
        if (startYear !== endYear) {
          this.isYearInvalid = true;
          return;
        } else {
          this.isYearInvalid = false;
        }

        this.durationWithoutCalc = -this.startRange.diff(this.endRange, 'days') + 1;
        if (this.durationWithoutCalc < 1) {
          this.isDateInvalid = true;
          return;
        }

        const leaveApplication = this.leaveApplicationFormService.getLeaveApplication(this.editForm);
        this.leaveApplicationService.calculateLeaveDuration(leaveApplication).subscribe(resp => {
          this.durationInDays = resp.body!;
          this.editForm.controls['durationInDay'].setValue(this.durationInDays);

          if (this.durationInDays < 0) {
            this.isConflict = true;
            this.durationInDays = 0;
            this.editForm.controls['durationInDay'].setValue(this.durationInDays);
            this.isDateInvalid = true;
            return;
          } else if (this.durationInDays === 0) {
            this.isDaysEqualZero = true;
            this.editForm.controls['durationInDay'].setValue(this.durationInDays);
            return;
          } else {
            this.findAnyConflictWithOthers();
          }
        });
      }
    }
  }

  findAnyConflictWithOthers(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;
    const employeeId = this.editForm.get(['employeeId'])!.value;

    const dateRange: IDateRangeDTO = { startDate, endDate };
    this.leaveApplicationService.findApplicationsByDateRange(employeeId, dateRange).subscribe(res => {
      this.attendanceTimeSheet = res.body!;
      this.conflictMessage = this.showConflictMessage(this.attendanceTimeSheet);
      if (this.conflictMessage.length > 0) {
        this.hasAnyConflict = true;
        this.editForm.controls['durationInDay'].setValue(0);
      } else {
        this.hasAnyConflict = false;
      }
    });
  }

  showConflictMessage(attendanceTimeSheet: IAttendanceTimeSheet): string {
    if (attendanceTimeSheet.hasPendingManualAttendance) {
      return HAS_PENDING_MANUAL_ATTENDANCE_MSG_ADMIN;
    } else if (attendanceTimeSheet.hasPendingMovementEntry) {
      return HAS_PENDING_MOVEMENT_ENTRY_MSG_ADMIN;
    } else if (attendanceTimeSheet.hasPendingLeaveApplication && !this.editForm.get('id')!.value) {
      return HAS_PENDING_LEAVE_APPLICATION_MSG_ADMIN;
    } else if (attendanceTimeSheet.hasPresentStatus) {
      return HAS_PRESENT_STATUS_MSG_ADMIN;
    } else if (attendanceTimeSheet.hasMovementStatus) {
      return HAS_MOVEMENT_ENTRY_MSG_ADMIN;
    } else if (attendanceTimeSheet.hasLeaveStatus) {
      return HAS_APPROVED_LEAVE_MSG_ADMIN;
    } else {
      return '';
    }
  }

  loadEmployee(): void {
    const employeeId = this.editForm.get(['employeeId'])!.value;
    this.employeeService.find(employeeId).subscribe(res => {
      const employee = res.body!;
      this.selectedEmployee = employee;
      this.editForm.patchValue({
        phoneNumberOnLeave: employee.personalContactNo,
      });
    });
  }

  loadDataByEmployeeAndDate(): void {
    const employeeId = this.editForm.get('employeeId')!.value;
    const startDateValue = this.editForm.get('startDate')!.value;
    if (employeeId && startDateValue) {
      this.leaveApplicationYear = startDateValue.toDate().getFullYear();
      this.leaveSummaryEndUserViewService
        .loadByYearAndEmployeeId(this.leaveApplicationYear, employeeId)
        .subscribe((resp: HttpResponse<ILeaveBalanceEndUserView[]>) => {
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
          this.other = this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.OTHER)[0]?.daysRemaining || 0;
        });
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('bitsHrPayrollApp.error', {
            ...err,
            key: 'error.file.' + err.key,
          })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const leaveApplication = this.leaveApplicationFormService.getLeaveApplication(this.editForm);
    if (leaveApplication.id !== null) {
      this.subscribeToSaveResponse(this.leaveApplicationService.update(leaveApplication));
    } else {
      this.subscribeToSaveResponse(this.leaveApplicationService.create(leaveApplication as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ILeaveApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.editForm.reset();
    this.leaveBalanceEndUserView = [];
    swalSuccessWithMessage('Applied and Approved');
  }

  protected onSaveError(err: any): void {
    this.isSaving = false;
    this.showErrorForInvalidInput();
    //swalOnRequestErrorWithBackEndErrorTitle(err.error.title);
  }

  showErrorForInvalidInput() {
    Swal.fire({
      icon: 'error',
      text: 'Invalid input',
      timer: 3500,
      showConfirmButton: false,
    });
  }

  protected updateForm(leaveApplication: ILeaveApplication): void {
    this.leaveApplication = leaveApplication;
    this.leaveApplicationFormService.resetForm(this.editForm, leaveApplication);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   leaveApplication.employee
    // );
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   leaveApplication.sanctionedBy
    // );
  }

  protected loadRelationshipsOptions(): void {
    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.leaveApplication?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
    //
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.leaveApplication?.sanctionedBy)))
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  changeEmployee(employee: any): void {
    if (employee) {
      this.selectedEmployee = employee;
      this.editForm.get('employeeId')!.setValue(employee.id);
      this.clearInputFields();
      this.loadDataByEmployeeAndDate();
      this.loadEmployee();
    }
  }

  clearInputFields(): void {
    this.editForm.get(['description'])!.reset();
    this.editForm.get(['startDate'])!.reset();
    this.editForm.get(['endDate'])!.reset();
    this.editForm.get(['durationInDay'])!.reset();
    this.editForm.get(['phoneNumberOnLeave'])!.reset();
    this.editForm.get(['addressOnLeave'])!.reset();
    this.editForm.get(['reason'])!.reset();
  }
}
