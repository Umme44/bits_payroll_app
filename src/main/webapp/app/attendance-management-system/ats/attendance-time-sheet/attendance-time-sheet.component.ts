import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import {
  AttendanceTimeSheetService,
  HAS_APPROVED_LEAVE_MSG,
  HAS_MOVEMENT_ENTRY_MSG,
  HAS_PENDING_LEAVE_APPLICATION_MSG,
  HAS_PENDING_MANUAL_ATTENDANCE_MSG,
  HAS_PENDING_MOVEMENT_ENTRY_MSG,
  HAS_PRESENT_STATUS_MSG,
} from './attendance-time-sheet.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { IAttendanceTimeSheet } from 'app/shared/model/attendance-time-sheet.model';
import { FormBuilder } from '@angular/forms';
import dayjs from 'dayjs/esm';
import { swalClose, swalForWarningWithMessage, swalOnLoading, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import { DateRangeDTO, IDateRangeDTO } from 'app/shared/model/DateRangeDTO';
import { EmployeeCommonService } from 'app/common/employee-address-book/employee-common.service';
import { DefinedKeys } from '../../../config/defined-keys.constant';
import { ConfigService } from '../../../shared/legacy/legacy-service/config.service';
import { EventManager } from '../../../core/util/event-manager.service';
import { AttendanceService } from '../../../shared/legacy/legacy-service/attendance.service';
import { IEmployee } from '../../../entities/employee-custom/employee-custom.model';
import { DATE_FORMAT } from '../../../config/input.constants';

@Component({
  selector: 'jhi-attendance-time-sheet',
  templateUrl: './attendance-time-sheet.component.html',
  styleUrls: ['attendance-time-sheet.component.scss'],
})
export class AttendanceTimeSheetComponent implements OnInit {
  attendanceTimeSheets: IAttendanceTimeSheet[] = [];
  filteredAttendanceTimeSheets: IAttendanceTimeSheet[] = [];
  eventSubscriber?: Subscription;
  date?: number;
  h?: number;
  m?: number;
  hour?: string;
  minute?: string;
  today = new Date();
  monthFromToday!: Date;
  employee!: IEmployee;

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

  editForm = this.fb.group({
    startDate: [new Date()],
    endDate: [],
    status: ['ALL'],
  });

  attendanceStatusList: string[] = [
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

  maxDaysForAttendanceDataLoad = 30;

  constructor(
    protected attendanceTimeSheetService: AttendanceTimeSheetService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected router: Router,
    private fb: FormBuilder,
    private employeeCommonService: EmployeeCommonService,
    private configService: ConfigService,
    private attendanceService: AttendanceService
  ) {
    this.today = new Date();
    this.monthFromToday = new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000);
  }

  ngOnInit(): void {
    this.setInitialDateInDatePicker();
    this.loadFromRange();
    this.loadEmployeeData();
    this.loadMaxAllowedDaysKeyValue();
  }

  loadMaxAllowedDaysKeyValue(): void {
    this.configService.findByKeyCommon(DefinedKeys.max_duration_in_days_for_attendance_data_load).subscribe(
      res => {
        const config = res.body!;
        if (isNaN(parseInt(config.value!, 10))) {
          throw 'Failed to parse ' + config.value;
        } else {
          this.maxDaysForAttendanceDataLoad = parseInt(config.value!, 10);
        }
      },
      error => {
        this.maxDaysForAttendanceDataLoad = 30;
      }
    );
  }

  loadFromRange(): void {
    const timeRange = this.createFromForm();

    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;

    // if date is undefined, return from here
    if (!startDate || !endDate) {
      return;
    }

    const duration = dayjs.duration(endDate.diff(startDate));
    const days = duration.asDays();

    if (days > this.maxDaysForAttendanceDataLoad) {
      swalForWarningWithMessage(`You are only allowed to view up to ${this.maxDaysForAttendanceDataLoad} days of Attendance!`, 5000);
      return;
    }

    swalOnLoading('Loading');

    this.attendanceTimeSheetService.queryByDateRange(timeRange).subscribe((res: HttpResponse<IAttendanceTimeSheet[]>) => {
      swalClose();
      this.attendanceTimeSheets = res.body || [];
      this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;

      // filter by status
      const checkStatus = this.editForm.get('status')!.value;
      if (!checkStatus || checkStatus === 'ALL') {
        this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;
      } else {
        this.filteredAttendanceTimeSheets = this.attendanceTimeSheets.filter(s => s.attendanceStatus === checkStatus);
      }

      // counting of status
      this.totalDays = this.attendanceTimeSheets.length;
      this.lateDays = this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'LATE').length;

      this.nonFulfilledOfficeHours = this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'NON_FULFILLED_OFFICE_HOURS').length;

      this.autoPunchDays = this.attendanceTimeSheets.filter(obj => obj.isAutoPunchOut === true).length;

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

      this.govtHolidays = this.attendanceTimeSheets.filter(
        obj => obj.attendanceStatus === 'GOVT_HOLIDAY' || obj.attendanceStatus === 'PRESENT_GOVT_HOLIDAY'
      ).length;
      this.weeklyOffday = this.attendanceTimeSheets.filter(
        obj => obj.attendanceStatus === 'WEEKLY_OFFDAY' || obj.attendanceStatus === 'PRESENT_WEEKLY_OFFDAY'
      ).length;
      this.notCompiledDays = 0;
    });
  }

  private loadEmployeeData(): void {
    this.employeeCommonService.getCurrentEmployeeMinimal().subscribe(res => {
      this.employee = res.body!;
    });
  }

  setInitialDateInDatePicker(): void {
    this.editForm.controls['endDate'].setValue(dayjs(this.today));
    this.editForm.controls['startDate'].setValue(dayjs(this.monthFromToday) as any);
  }

  public getTimeStringFromDecimalValue(workinghour: any): string {
    if (workinghour === null || workinghour === undefined) return '00:00';
    const h = Math.floor(workinghour);
    const m = Math.floor(workinghour * 100 - h * 100);
    const hour = h < 10 ? '0' + h : h + '';
    const minute = m < 10 ? '0' + m : m + '';
    return hour + ':' + minute;
  }

  private createFromForm(): IDateRangeDTO {
    return {
      ...new DateRangeDTO(),
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
    };
  }

  calculateTotalWorkingDays(totalDays: any, govtHolidays: any, weeklyOffday: any): number {
    return totalDays - govtHolidays - weeklyOffday;
  }

  showSwalValidationMessage(attendanceEntry: IAttendanceTimeSheet): boolean {
    if (attendanceEntry.hasLeaveStatus === true) {
      swalForWarningWithMessage(HAS_APPROVED_LEAVE_MSG);
      return false;
    } else if (attendanceEntry.hasMovementStatus === true) {
      swalForWarningWithMessage(HAS_MOVEMENT_ENTRY_MSG);
      return false;
    } else if (attendanceEntry.hasPresentStatus === true) {
      swalForWarningWithMessage(HAS_PRESENT_STATUS_MSG);
      return false;
    } else if (attendanceEntry.hasPendingLeaveApplication === true) {
      swalForWarningWithMessage(HAS_PENDING_LEAVE_APPLICATION_MSG);
      return false;
    } else if (attendanceEntry.hasPendingManualAttendance === true) {
      swalForWarningWithMessage(HAS_PENDING_MANUAL_ATTENDANCE_MSG);
      return false;
    } else if (attendanceEntry.hasPendingMovementEntry === true) {
      swalForWarningWithMessage(HAS_PENDING_MOVEMENT_ENTRY_MSG);
      return false;
    } else {
      return true;
    }
  }

  routeToManualAttendancePage(attendanceEntry: IAttendanceTimeSheet): void {
    const result = this.showSwalValidationMessage(attendanceEntry);
    if (result === true) {
      this.router.navigate(['/manual-attendance-entry/apply']);
      sessionStorage.setItem('manualAttendanceEntryApplyDate', attendanceEntry.date!.toString());
    }
  }

  routeToMovementEntryPage(attendanceEntry: IAttendanceTimeSheet): void {
    const result = this.showSwalValidationMessage(attendanceEntry);
    if (result === true) {
      this.router.navigate(['/movement-entry/apply']);
      sessionStorage.setItem('movementEntryApplyDate', attendanceEntry.date!.toString());
    }
  }

  routeToLeaveApplicationDate(attendanceEntry: IAttendanceTimeSheet): void {
    const result = this.showSwalValidationMessage(attendanceEntry);
    if (result === true) {
      this.router.navigate(['/user-leave-application']);
      sessionStorage.setItem('userLeaveApplicationApplyDate', attendanceEntry.date!.toString());
    }
  }

  exportAtsData(): void {
    const timeRange = this.createFromForm();
    const fileName =
      'ats-' +
      this.employee.pin +
      '-from-' +
      dayjs(timeRange.startDate).format(DATE_FORMAT) +
      '-to-' +
      dayjs(timeRange.endDate).format(DATE_FORMAT) +
      '.xlsx';
    swalOnLoading('Loading ...');
    this.attendanceTimeSheetService.exportAtsDataInXL(timeRange).subscribe(
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

  print(): void {
    window.print();
  }

  public getUserFriendlyAttendanceStatus(status: any): string {
    return this.attendanceService.getUserFriendlyAttendanceStatus(status);
  }

  filterAttendanceList(): void {
    const checkStatus = this.editForm.get('status')!.value;
    if (!checkStatus || checkStatus === 'ALL') {
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
}
