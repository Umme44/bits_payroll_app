import { Component, OnInit } from '@angular/core';
import { IAttendanceTimeSheet } from '../../../shared/model/attendance-time-sheet.model';
import dayjs from 'dayjs/esm';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventManager } from 'app/core/util/event-manager.service';
import { Subscription } from 'rxjs';
import { ITimeRangeAndEmployeeId, TimeRangeAndEmployeeId } from './time-range-and-employeeId.model';
import { AttendanceTimeSheetAdminService } from './attendance-time-sheet-admin.service';
import { ActivatedRoute } from '@angular/router';
import { swalConfirmationWithMessage } from 'app/shared/swal-common/swal-confirmation.common';
import { IEmployeeMinimal } from 'app/shared/model/employee-minimal.model';
import { ATSAdminManualAttendanceEntryDialogComponent } from 'app/attendance-management-system/ats/attendance-time-sheet-admin/ats-admin-manual-attendance-entry-update-dialog.component';
import { MovementType } from 'app/shared/model/enumerations/movement-type.model';
import { Status } from 'app/shared/model/enumerations/status.model';
import { AtsAdminLeaveApplicationDialogComponent } from 'app/attendance-management-system/ats/attendance-time-sheet-admin/ats-admin-leave-application-dialog.component';
import { AttendanceService } from '../../../shared/legacy/legacy-service/attendance.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { ManualAttendanceEntryService } from '../../../shared/legacy/legacy-service/manual-attendance-entry.service';
import { MovementEntryService } from '../../../shared/legacy/legacy-service/movement-entry.service';
import { LeaveApplicationService } from '../../../shared/legacy/legacy-service/leave-application.service';
import { DATE_FORMAT } from '../../../config/input.constants';
import { IMovementEntry, MovementEntry } from '../../../shared/legacy/legacy-model/movement-entry.model';
import {
  swalClose,
  swalForWarningWithMessage,
  swalOnLoading,
  swalOnRequestError,
  swalSuccessWithMessage,
} from 'app/shared/swal-common/swal-common';

@Component({
  selector: 'jhi-attendance-time-sheet-admin',
  templateUrl: './attendance-time-sheet-admin.component.html',
  styleUrls: ['./attendance-time-sheet-admin.component.scss'],
})
export class AttendanceTimeSheetAdminComponent implements OnInit {
  attendanceTimeSheets: IAttendanceTimeSheet[] = [];
  filteredAttendanceTimeSheets: IAttendanceTimeSheet[] = [];
  eventSubscriber?: Subscription;
  employeeMinimal?: IEmployeeMinimal;
  minute?: any;
  today!: any;
  monthFromToday!: any;

  employeeId!: number;

  totalDays = 0;
  lateDays = 0;
  nonFulfilledOfficeHours = 0;
  presentDays = 0;
  absentDays = 0;
  leaveDays = 0;
  govtHolidays = 0;
  weeklyOffday = 0;
  notCompiledDays = 0;

  autoPunchDays = 0;

  attendanceStatus: string[] = [
    'ABSENT',
    'PRESENT',
    'LATE',
    'MOVEMENT',
    'NON_FULFILLED_OFFICE_HOURS',
    'WEEKLY_OFFDAY',
    'GOVT_HOLIDAY',
    'PRESENT_GOVT_HOLIDAY',
    'PRESENT_WEEKLY_OFFDAY',
    'MENTIONABLE_ANNUAL_LEAVE',
    'MENTIONABLE_CASUAL_LEAVE',
    'NON_MENTIONABLE_COMPENSATORY_LEAVE',
    'NON_MENTIONABLE_PANDEMIC_LEAVE',
    'NON_MENTIONABLE_PATERNITY_LEAVE',
    'NON_MENTIONABLE_MATERNITY_LEAVE',
    'LEAVE_WITHOUT_PAY',
  ];
  selectedMemberId!: number;

  isInvalid = false;

  editForm = this.fb.group({
    startDate: [new Date()],
    endDate: [],
    status: ['ALL'],
    employeeId: this.fb.group({
      employeeId: [null, Validators.required],
    }),
  });

  constructor(
    protected movementEntryService: MovementEntryService,
    protected attendanceTimeSheetAdminService: AttendanceTimeSheetAdminService,
    protected manualAttendanceEntryService: ManualAttendanceEntryService,
    protected eventManager: EventManager,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected modalService: NgbModal,
    private fb: FormBuilder,
    protected leaveApplicationService: LeaveApplicationService,
    private attendanceService: AttendanceService
  ) {
    this.today = new Date();
    this.monthFromToday = new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000);
  }

  ngOnInit(): void {
    this.registerChangeInAttendanceEntries();
    this.setInitialDateInDatePicker();
  }

  setInitialDateInDatePicker(): void {
    this.editForm.controls['endDate'].setValue(dayjs(this.today));
    this.editForm.controls['startDate'].setValue(dayjs(this.monthFromToday) as any);
  }

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  processAtsData(attendanceTimeSheets: IAttendanceTimeSheet[]): void {
    this.totalDays = attendanceTimeSheets.length;
    this.lateDays = attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'LATE').length;

    this.nonFulfilledOfficeHours = attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'NON_FULFILLED_OFFICE_HOURS').length;
    // late days are also considered as present

    this.autoPunchDays = attendanceTimeSheets.filter(obj => obj.isAutoPunchOut === true).length;

    this.presentDays =
      attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'PRESENT').length + this.lateDays + this.nonFulfilledOfficeHours;

    this.absentDays = attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'ABSENT').length;
    this.leaveDays = attendanceTimeSheets.filter(
      obj =>
        obj.attendanceStatus === 'MENTIONABLE_ANNUAL_LEAVE' ||
        obj.attendanceStatus === 'MENTIONABLE_CASUAL_LEAVE' ||
        obj.attendanceStatus === 'NON_MENTIONABLE_COMPENSATORY_LEAVE' ||
        obj.attendanceStatus === 'NON_MENTIONABLE_PATERNITY_LEAVE' ||
        obj.attendanceStatus === 'NON_MENTIONABLE_MATERNITY_LEAVE' ||
        obj.attendanceStatus === 'OTHER'
    ).length;

    this.govtHolidays = attendanceTimeSheets.filter(
      obj => obj.attendanceStatus === 'GOVT_HOLIDAY' || obj.attendanceStatus === 'PRESENT_GOVT_HOLIDAY'
    ).length;
    this.weeklyOffday = attendanceTimeSheets.filter(
      obj => obj.attendanceStatus === 'WEEKLY_OFFDAY' || obj.attendanceStatus === 'PRESENT_WEEKLY_OFFDAY'
    ).length;
    this.notCompiledDays = 0;
  }

  loadFromRangeAndEmployee(): void {
    const timeRange = this.createFromForm();
    const employee = this.editForm.get('employeeId')!.value;
    this.employeeId = employee.employeeId;
    this.attendanceTimeSheetAdminService.getEmployeeById(this.employeeId).subscribe((res: HttpResponse<IEmployeeMinimal>) => {
      this.employeeMinimal = res.body!;
    });

    this.attendanceTimeSheetAdminService.queryByDateRange(timeRange).subscribe((res: HttpResponse<IAttendanceTimeSheet[]>) => {
      this.attendanceTimeSheets = res.body || [];
      this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;
      this.processAtsData(this.attendanceTimeSheets);
    });

    this.editForm.get(['status'])!.setValue('ALL');
  }

  loadLastThirtyDays(): void {
    this.setInitialDateInDatePicker();
    const timeRange = this.createFromForm();
    const employee = this.editForm.get('employeeId')!.value;
    this.employeeId = employee.employeeId;
    this.attendanceTimeSheetAdminService.getEmployeeById(this.employeeId).subscribe((res: HttpResponse<IEmployeeMinimal>) => {
      this.employeeMinimal = res.body!;
    });

    this.attendanceTimeSheetAdminService.queryByDateRange(timeRange).subscribe((res: HttpResponse<IAttendanceTimeSheet[]>) => {
      this.attendanceTimeSheets = res.body || [];
      this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;

      this.totalDays = this.attendanceTimeSheets?.length;
      this.lateDays = this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'LATE').length;

      this.nonFulfilledOfficeHours = this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'NON_FULFILLED_OFFICE_HOURS').length;
      // late days are also considered as present
      this.presentDays =
        this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'PRESENT').length + this.lateDays + this.nonFulfilledOfficeHours;

      this.absentDays = this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'ABSENT').length;
      this.leaveDays = this.attendanceTimeSheets.filter(
        obj =>
          obj.attendanceStatus === 'MENTIONABLE_ANNUAL_LEAVE' ||
          obj.attendanceStatus === 'MENTIONABLE_CASUAL_LEAVE' ||
          obj.attendanceStatus === 'NON_MENTIONABLE_COMPENSATORY_LEAVE' ||
          obj.attendanceStatus === 'NON_MENTIONABLE_PATERNITY_LEAVE' ||
          obj.attendanceStatus === 'NON_MENTIONABLE_MATERNITY_LEAVE' ||
          obj.attendanceStatus === 'OTHER'
      ).length;

      this.govtHolidays = this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'GOVT_HOLIDAY').length;
      this.weeklyOffday = this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'WEEKLY_OFFDAY').length;
      this.notCompiledDays = 0;
    });

    this.editForm.get(['status'])!.setValue(this.attendanceStatus[0].valueOf());
  }

  registerChangeInAttendanceEntries(): void {
    this.eventSubscriber = this.eventManager.subscribe('attendanceEntryListModification', () => this.loadFromRangeAndEmployee());
  }

  private createFromForm(): ITimeRangeAndEmployeeId {
    return {
      ...new TimeRangeAndEmployeeId(),
      employeeId: this.employeeIdForm.get(['employeeId'])!.value,
      startDate: this.editForm.get(['startDate'])!.value.format('YYYY-MM-DD'),
      endDate: this.editForm.get(['endDate'])!.value.format('YYYY-MM-DD'),
    };
  }

  public getUserFriendlyAttendanceStatus(status: any): string {
    return this.attendanceService.getUserFriendlyAttendanceStatus(status);
  }

  public getTimeStringFromDecimalValue(workinghour: any): string {
    if (workinghour === null || workinghour === undefined) return '00:00';
    const h = Math.floor(workinghour);
    const m = Math.floor(workinghour * 100 - h * 100);
    const hour = h < 10 ? '0' + h : h + '';
    const minute = m < 10 ? '0' + m : m + '';
    return hour + ':' + minute;
  }

  checkDate(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;
    const employeeId = this.employeeIdForm.get(['employeeId'])!.value;

    if (startDate && endDate && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;

      if (employeeId !== null && employeeId !== undefined) {
        this.loadFromRangeAndEmployee();
      }
    }
  }

  calculateTotalWorkingDays(totalDays: any, govtHolidays: any, weeklyOffday: any): number {
    return totalDays - govtHolidays - weeklyOffday;
  }

  updateAttendanceTimeSheet(date: any): void {
    const timeRange = {
      ...new TimeRangeAndEmployeeId(),
      employeeId: this.employeeIdForm.get(['employeeId'])!.value,
      startDate: date.format(DATE_FORMAT),
      endDate: date.format(DATE_FORMAT),
    };

    //change status type set
    this.attendanceTimeSheetAdminService.queryByDateRange(timeRange).subscribe(res => {
      if (res.body!.length > 0) {
        const ats: IAttendanceTimeSheet = res.body![0];

        for (let i = 0; i < this.attendanceTimeSheets.length; i++) {
          const element = this.attendanceTimeSheets[i];
          if (dayjs(element.date).isSame(date)) {
            this.attendanceTimeSheets[i] = ats;
          }
        }

        for (let i = 0; i < this.filteredAttendanceTimeSheets.length; i++) {
          const element = this.filteredAttendanceTimeSheets[i];
          if (dayjs(element.date).isSame(date)) {
            this.filteredAttendanceTimeSheets[i] = ats;
          }
        }
      }
    });
  }

  filterAttendanceList(): void {
    const checkStatus = this.editForm.get(['status'])!.value;
    if (checkStatus === 'ALL') {
      this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;
    } else {
      this.filteredAttendanceTimeSheets = this.attendanceTimeSheets.filter(s => {
        if (checkStatus === 'LEAVE_WITHOUT_PAY') {
          return s.attendanceStatus === 'LEAVE_WITHOUT_PAY' || s.attendanceStatus === 'LEAVE_WITHOUT_PAY_SANDWICH';
        } else {
          return s.attendanceStatus === checkStatus;
        }
      });
    }
  }

  openSimpleLeaveApplicationModal(attendanceEntry: IAttendanceTimeSheet): void {
    const date = attendanceEntry.date;
    if (date) {
      const employeeId = this.employeeIdForm.get('employeeId')!.value;

      const modalRef = this.modalService.open(AtsAdminLeaveApplicationDialogComponent, {
        ariaLabelledBy: 'modal-basic-title',
        centered: true,
        size: 'lg',
      });

      modalRef.componentInstance.employeeId = employeeId;
      modalRef.componentInstance.selectedDateForLeaveApply = date;

      modalRef.result.then(
        result => {
          this.updateAttendanceTimeSheet(date);
        },
        reason => {
          this.updateAttendanceTimeSheet(date);
        }
      );
    } else {
      swalForWarningWithMessage('No Date Selected');
    }
  }

  openManualAttendanceEntryModal(date: any): void {
    if (date) {
      const employeeId = this.employeeIdForm.get('employeeId')!.value;
      this.manualAttendanceEntryService.findInOrOutTimeByDateAndEmployeeId(date, employeeId).subscribe(res => {
        const attendance = res.body!;

        const modalRef = this.modalService.open(ATSAdminManualAttendanceEntryDialogComponent, {
          ariaLabelledBy: 'modal-basic-title',
          size: 'md',
          centered: true,
        });
        modalRef.componentInstance.employeeId = employeeId;
        modalRef.componentInstance.manualAttendanceEntry = attendance;
        modalRef.componentInstance.selectedDate = date;

        modalRef.result.then(
          result => {
            this.updateAttendanceTimeSheet(date);
          },
          reason => {
            this.updateAttendanceTimeSheet(date);
          }
        );
      });
    } else {
      swalForWarningWithMessage('No Date Selected');
    }
  }

  openMovementEntryModal(date: any): void {
    if (date) {
      swalConfirmationWithMessage(`Apply Movement Entry for: ${dayjs(date).format(DATE_FORMAT)}`, 'Proceed').then(result => {
        if (result.isConfirmed) {
          const movementEntry: IMovementEntry = {
            ...new MovementEntry(),
            startDate: date,
            startTime: dayjs(),
            startNote: 'startNote-HR Regularization',
            endDate: date,
            endTime: dayjs(),
            endNote: 'endNote-HR Regularization',
            type: MovementType.MOVEMENT,
            status: Status.APPROVED,
            employeeId: this.employeeIdForm.get(['employeeId'])!.value,
            createdAt: date,
            note: 'HR Regularization',
          };

          this.movementEntryService.applyAndApproveMovementEntryByHR(movementEntry).subscribe(res => {
            swalSuccessWithMessage('Movement Applied and Approved!');
            this.updateAttendanceTimeSheet(date);
          });
        }
      });
    } else {
      swalForWarningWithMessage('No Date Selected');
    }
  }

  print(): void {
    window.print();
  }

  exportAtsData(): void {
    const fileName = 'attendanceTimeSheet.xlsx';
    const timeRange = this.createFromForm();
    swalOnLoading('Loading ...');
    this.attendanceTimeSheetAdminService.exportAtsDataInXL(timeRange).subscribe(
      x => {
        // It is necessary to create a new blob object with mime-type explicitly set
        // otherwise only Chrome works like it should
        const newBlob = new Blob([x], { type: 'application/octet-stream' });

        // IE doesn't allow using a blob object directly as link href
        // instead it is necessary to use msSaveOrOpenBlob
        if (window.navigator && (window.navigator as any).msSaveOrOpenBlob) {
          (window.navigator as any).msSaveOrOpenBlob(newBlob, fileName);
          return;
        }

        // For other browsers:
        // Create a link pointing to the ObjectURL containing the blob.
        const data = window.URL.createObjectURL(newBlob);

        const link = document.createElement('a');
        link.href = data;
        link.download = fileName;
        // this is necessary as link.click() does not work on the latest firefox
        link.dispatchEvent(new MouseEvent('click', { bubbles: true, cancelable: true, view: window }));
        // tslint:disable-next-line:typedef
        setTimeout(function () {
          // For Firefox it is necessary to delay revoking the ObjectURL
          window.URL.revokeObjectURL(data);
          link.remove();
        }, 100);
        swalClose();
      },
      err => {
        swalClose();
        swalOnRequestError();
      }
    );
  }
}
