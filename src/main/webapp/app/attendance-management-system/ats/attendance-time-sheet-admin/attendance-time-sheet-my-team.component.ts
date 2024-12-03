import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
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
import { EmployeeMinimalListType } from 'app/shared/model/enumerations/employee-minimal-list-type.model';
import { MyTeamService } from 'app/attendance-management-system/my-team/my-team.service';
import { swalClose, swalOnLoading, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import { IEmployeeMinimal } from 'app/shared/model/employee-minimal.model';
import { AttendanceService } from '../../../shared/legacy/legacy-service/attendance.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';

@Component({
  selector: 'jhi-attendance-time-sheet-my-team',
  templateUrl: './attendance-time-sheet-my-team.component.html',
  styleUrls: ['./attendance-time-sheet-admin.component.scss'],
})
export class AttendanceTimeSheetMyTeamComponent implements OnInit, AfterViewInit, OnDestroy {
  ch!: any;
  attendanceTimeSheets: IAttendanceTimeSheet[] = [];
  filteredAttendanceTimeSheets: IAttendanceTimeSheet[] = [];
  eventSubscriber?: Subscription;
  employeeMinimal?: IEmployeeMinimal;
  date?: number;
  h?: number;
  m?: any;
  hour?: any;
  minute?: any;
  today!: any;
  monthFromToday!: any;
  dayParam!: any;
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

  attendanceStatus: string[] = [
    'ABSENT',
    'PRESENT',
    'LATE',
    'LEAVE',
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
    'OTHER',
  ];
  selectedMemberId!: number;

  pageType: string | undefined;

  isInvalid = false;

  editForm = this.fb.group({
    startDate: [new Date()],
    endDate: [],
    status: [this.attendanceStatus],
    employeeId: this.fb.group({
      employeeId: [null, Validators.required],
    }),
  });

  constructor(
    protected attendanceTimeSheetAdminService: AttendanceTimeSheetAdminService,
    protected eventManager: EventManager,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected modalService: NgbModal,
    private myTeamService: MyTeamService,
    private fb: FormBuilder,
    private attendanceService: AttendanceService
  ) {
    this.today = new Date();
    // this.monthFromToday = new Date().setDate(this.today) - 30;
    this.monthFromToday = new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000);
    this.dayParam = this.today.toISOString().substring(0, 10);
    const pageType = this.activatedRoute.snapshot.data['pageType'];
    if (pageType === 'myTeam') {
      this.pageType = EmployeeMinimalListType.MY_TEAM;
      // getting the selected team members' id.
      const id = sessionStorage.getItem('selectedTeamMemberId')!;
      const defaultSelectedMemberId = sessionStorage.getItem('defaultSelectedMemberId')!;

      if (id !== null && id !== undefined) {
        this.selectedMemberId = parseInt(id, 10);
      } else {
        this.selectedMemberId = parseInt(defaultSelectedMemberId, 10);
      }
    }
  }

  ngOnInit(): void {
    this.registerChangeInAttendanceEntries();
    this.date = Date.now();
    this.setInitialDateInDatePicker();
  }

  ngOnDestroy(): void {
    sessionStorage.removeItem('selectedTeamMemberId');
  }

  ngAfterViewInit(): void {
    if (this.pageType === EmployeeMinimalListType.MY_TEAM) {
      this.loadFromRangeAndEmployee();
    }
  }

  setInitialDateInDatePicker(): void {
    this.editForm.controls['endDate'].setValue(dayjs(this.today));
    this.editForm.controls['startDate'].setValue(dayjs(this.monthFromToday) as any);
  }

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  get listType(): EmployeeMinimalListType {
    if (this.pageType === EmployeeMinimalListType.MY_TEAM) {
      return EmployeeMinimalListType.MY_TEAM;
    } else {
      return EmployeeMinimalListType.ALL;
    }
  }

  processAtsData(attendanceTimeSheets: IAttendanceTimeSheet[]): void {
    this.totalDays = attendanceTimeSheets?.length;
    this.lateDays = attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'LATE').length;

    this.nonFulfilledOfficeHours = attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'NON_FULFILLED_OFFICE_HOURS').length;
    // late days are also considered as present
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

    if (this.pageType === EmployeeMinimalListType.MY_TEAM) {
      this.attendanceTimeSheetAdminService
        .queryByDateRangeForTeamMember(timeRange)
        .subscribe((res: HttpResponse<IAttendanceTimeSheet[]>) => {
          this.attendanceTimeSheets = res.body || [];
          this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;
          this.processAtsData(this.attendanceTimeSheets);
        });
    } else {
      this.attendanceTimeSheetAdminService.queryByDateRange(timeRange).subscribe((res: HttpResponse<IAttendanceTimeSheet[]>) => {
        this.attendanceTimeSheets = res.body || [];
        this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;
        this.processAtsData(this.attendanceTimeSheets);
      });
    }

    this.editForm.get(['status'])!.setValue(this.attendanceStatus[0].valueOf());
    //this.setInitialDateInDatePicker();
  }

  loadLastThirtyDays(): void {
    this.setInitialDateInDatePicker();
    const timeRange = this.createFromForm();
    const employee = this.editForm.get('employeeId')!.value;
    this.employeeId = employee.employeeId;
    this.attendanceTimeSheetAdminService.getEmployeeById(this.employeeId).subscribe((res: HttpResponse<IEmployeeMinimal>) => {
      this.employeeMinimal = res.body!;
    });
    if (this.pageType === EmployeeMinimalListType.MY_TEAM) {
      this.loadFromRangeAndEmployee();
    } else {
      this.attendanceTimeSheetAdminService.queryByDateRange(timeRange).subscribe((res: HttpResponse<IAttendanceTimeSheet[]>) => {
        this.attendanceTimeSheets = res.body || [];
        this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;

        this.totalDays = this.attendanceTimeSheets?.length;
        this.lateDays = this.attendanceTimeSheets.filter(obj => obj.attendanceStatus === 'LATE').length;

        this.nonFulfilledOfficeHours = this.attendanceTimeSheets.filter(
          obj => obj.attendanceStatus === 'NON_FULFILLED_OFFICE_HOURS'
        ).length;
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
    }

    this.editForm.get(['status'])!.setValue(this.attendanceStatus[0].valueOf());
  }

  load(): void {
    const employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    const timeRange = this.createFromForm();
    this.attendanceTimeSheetAdminService.queryByDateRange(timeRange).subscribe((res: HttpResponse<IAttendanceTimeSheet[]>) => {
      this.attendanceTimeSheets = res.body || [];
      this.filteredAttendanceTimeSheets = this.attendanceTimeSheets;
    });
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

  public getStringValue(status: any): string {
    if (status === 'BLANK') return '-';
    if (status === 'BLANK_INEFFECTIVE') return '-';
    if (status === 'WEEKLY_OFFDAY') return 'WEEKEND';
    if (status === 'NON_FULFILLED_OFFICE_HOURS') return 'NFOH';
    if (status === 'GOVT_HOLIDAY') return ' GOVT HOLIDAY';
    if (status === 'PRESENT_GOVT_HOLIDAY') return 'PRESENT GOVT HOLIDAY';
    if (status === 'PRESENT_WEEKLY_OFFDAY') return 'PRESENT WEEKLY OFFDAY';
    if (status === 'ABSENT') return 'ABSENT';
    if (status === 'LATE') return 'LATE';
    if (status === 'NON_FULFILLED_OFFICE_HOURS') return 'NFOH';
    if (status === 'MENTIONABLE_ANNUAL_LEAVE') return 'ANNUAL LEAVE';
    if (status === 'MENTIONABLE_CASUAL_LEAVE') return 'CASUAL LEAVE';
    if (status === 'NON_MENTIONABLE_COMPENSATORY_LEAVE') return 'COMPENSATORY LEAVE';
    if (status === 'NON_MENTIONABLE_PANDEMIC_LEAVE') return 'PANDEMIC LEAVE';
    if (status === 'NON_MENTIONABLE_PATERNITY_LEAVE') return 'PATERNITY LEAVE';
    if (status === 'NON_MENTIONABLE_MATERNITY_LEAVE') return 'MATERNITY LEAVE';
    if (status === 'LEAVE') return 'LEAVE';
    if (status === 'OTHER') return 'OTHER';
    return status;
  }

  public getUserFriendlyAttendanceStatus(status: any): string {
    return this.attendanceService.getUserFriendlyAttendanceStatus(status);
  }

  public checkNFOH(status: any): boolean {
    if (status === 'NON_FULFILLED_OFFICE_HOURS') return true;
    return false;
  }

  public truncate(status: any): string {
    if (status == null) return '';
    this.ch = status;
    if (status.length <= 10) return status;
    const str = status.slice(0, 10) + '...';
    return str;
  }

  public convertMinsToHrsMins(workinghour: any): string {
    if (workinghour === null || workinghour === undefined) return '00:00';
    this.h = Math.floor(workinghour);
    this.m = workinghour - this.h;
    this.m = Math.floor(this.m * 100);
    this.hour = this.h < 10 ? '0' + this.h : this.h + '';
    this.minute = this.m < 10 ? '0' + this.m : this.m + '';
    return this.hour + ':' + this.minute;
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

    if (startDate && endDate && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  calculateTotalWorkingDays(totalDays: any, govtHolidays: any, weeklyOffday: any): number {
    return totalDays - govtHolidays - weeklyOffday;
  }

  filterAttendanceList(event: any): void {
    const checkStatus = this.editForm.get(['status'])!.value;
    if (checkStatus === 'ALL') {
      this.loadLastThirtyDays();
    } else {
      this.filteredAttendanceTimeSheets = this.attendanceTimeSheets.filter(s => {
        if (checkStatus === 'LEAVE_WITHOUT_PAY') {
          return s.attendanceStatus === 'LEAVE_WITHOUT_PAY' || 'LEAVE_WITHOUT_PAY_SANDWICH';
        } else {
          return s.attendanceStatus === checkStatus;
        }
      });
    }
  }

  exportMyTeamAtsData(): void {
    const fileName = 'attendanceTimeSheet.xlsx';
    const timeRange = this.createFromForm();
    swalOnLoading('Loading ...');
    this.attendanceTimeSheetAdminService.exportMyTeamAtsDataInXL(timeRange).subscribe(
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
}
