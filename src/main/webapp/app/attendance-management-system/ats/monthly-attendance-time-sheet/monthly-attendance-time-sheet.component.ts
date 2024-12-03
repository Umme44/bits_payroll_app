import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { MonthlyAttendanceTimeSheetService } from './monthly-attendance-time-sheet.service';
import { EventManager } from 'app/core/util/event-manager.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { IMonthlyAttendanceTimeSheet } from 'app/shared/model/monthly-attendance-time-sheet.model';
import { IAttendanceTimeSheetMini } from 'app/shared/model/attendance-time-sheet-mini.model';
import Swal from 'sweetalert2';
import { ApprovalDTO } from 'app/shared/model/approval-dto.model';
import dayjs from 'dayjs/esm';
import { PreRegularizeModalComponent } from 'app/attendance-management-system/ats/monthly-attendance-time-sheet/pre-regularize-modal.component';
import { Filter } from 'app/common/employee-address-book/filter.model';
import { IMonthlyAttendanceTimeSheetList } from 'app/shared/model/monthly-attendance-time-sheet-list.model';
import { DateRangeDTO } from 'app/shared/model/DateRangeDTO';
import { DesignationService } from '../../../shared/legacy/legacy-service/designation.service';
import { DepartmentService } from '../../../shared/legacy/legacy-service/department.service';
import { UnitService } from '../../../shared/legacy/legacy-service/unit.service';
import { IDesignation } from '../../../shared/legacy/legacy-model/designation.model';
import { IDepartment } from '../../../shared/legacy/legacy-model/department.model';
import { IUnit } from '../../../shared/legacy/legacy-model/unit.model';
import { ILeaveApplication } from '../../../shared/legacy/legacy-model/leave-application.model';
import {CustomValidator} from "../../../validators/custom-validator";
import {swalPatternError} from "../../../shared/swal-common/swal-common";
import {
  IMonthlyAttendanceTimeSheetEmployeeList
} from '../../../shared/model/monthly-attendance-time-sheet-employee-list.model';

@Component({
  selector: 'jhi-attendance-time-sheet',
  templateUrl: './monthly-attendance-time-sheet.component.html',
  styleUrls: ['monthly-attendance-time-sheet.component.scss'],
})
export class MonthlyAttendanceTimeSheetComponent implements OnInit, OnDestroy {
  ch!: any;
  monthlyAttendanceTimeSheets?: IMonthlyAttendanceTimeSheet[];

  eventSubscriber?: Subscription;
  date?: number;
  h?: number;
  m?: any;
  hour?: any;
  minute?: any;
  today!: any;
  monthFromToday!: any;
  dayParam!: any;
  isLoading = true;
  totalDays = 0;
  lateDays = 0;
  nonFulfilledOfficeHours = 0;
  presentDays = 0;
  absentDays = 0;
  leaveDays = 0;
  govtHolidays = 0;
  weeklyOffday = 0;
  notCompiledDays = 0;
  allSelector = false;
  selectedIdSetForATS = new Set();
  selectedEmployeeAts!: IMonthlyAttendanceTimeSheet[];
  monthlyAtsExportDto!: IMonthlyAttendanceTimeSheetList;
  approvalDTO = new ApprovalDTO();

  designations!: IDesignation[];
  departments!: IDepartment[];
  units!: IUnit[];

  filters = new Filter();

  isInvalid = false;

  leaveApplications!: ILeaveApplication[];

  editForm = this.fb.group({
    startDate: [new Date()],
    endDate: [],
    searchText: ['', [CustomValidator.naturalTextValidator()]],
    departmentId: [0],
    designationId: [0],
    unitId: [0],
  });

  constructor(
    protected attendanceTimeSheetService: MonthlyAttendanceTimeSheetService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected designationService: DesignationService,
    private departmentService: DepartmentService,
    private unitService: UnitService,
    private fb: FormBuilder
  ) {
    this.today = new Date();
    this.monthFromToday = new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000);
    this.dayParam = this.today.toISOString().substring(0, 10);
    this.filters.endDate = this.today;
    this.filters.startDate = this.monthFromToday;
  }

  loadAll(): void {
    this.designationService.query().subscribe((res: HttpResponse<IDesignation[]>) => (this.designations = res.body || []));
    this.departmentService.query().subscribe((res: HttpResponse<IDepartment[]>) => (this.departments = res.body || []));
    this.unitService.query().subscribe((res: HttpResponse<IUnit[]>) => (this.units = res.body || []));
  }

  differenceBetweenDays(timeRange: any): number {
    return Math.floor((Date.parse(timeRange.endDate) - Date.parse(timeRange.startDate)) / 86400000);
  }

  differenceBetweenTwoDate(filterObject: Filter): number {
    return Math.floor((Date.parse(filterObject.endDate.toString()) - Date.parse(filterObject.startDate.toString())) / 86400000);
  }

  showErrorForExceeding60Days(): void {
    Swal.fire({
      icon: 'error',
      title: 'Oops...',
      text: 'You are only allowed to see up to 60 days of Monthly Attendance!',
    });
  }

  loadFromRange(): void {
    // const timeRange = this.createFromForm();
    if (this.differenceBetweenTwoDate(this.filters) > 60) {
      this.showErrorForExceeding60Days();
    } else {
      this.isLoading = true;
      this.differenceBetweenTwoDate(this.filters);
      const filterObject = this.createFilterFromForm();
      this.attendanceTimeSheetService.queryUsingFilter(filterObject).subscribe((res: HttpResponse<IMonthlyAttendanceTimeSheet[]>) => {
        this.monthlyAttendanceTimeSheets = res.body || [];
        this.isLoading = false;
      }, err => {
        this.isLoading = false;
        swalPatternError()
      });
    }
  }

  private createFilterFromForm(): Filter {
    return {
      ...new Filter(),
      searchText: this.editForm.get(['searchText'])!.value,
      departmentId: this.editForm.get(['departmentId'])!.value,
      destinationId: this.editForm.get(['designationId'])!.value,
      unitId: this.editForm.get(['unitId'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
    };
  }

  preConfirmationRegularizeAbsent(): void {
    const date = new Date();
    const firstDay = new Date(date.getFullYear(), date.getMonth(), 1);
    const lastDay = new Date(date.getFullYear(), date.getMonth() + 1, 0);
    const modalRef = this.modalService.open(PreRegularizeModalComponent, {
      size: 'xl',
      scrollable: true,
      backdrop: true,
    });
    modalRef.componentInstance.timeRange = this.createFromForm();
  }

  autoLeaveCut(): void {
    const timeRange = this.createFromForm();
    this.isLoading = true;
    /*this.attendanceTimeSheetService.autoCutLeave(timeRange).subscribe((response: HttpResponse<boolean>) => {
      // if(response.body === true) alert("Absent days adjusted");
      // else alert("Failed to adjust absent days");
      this.attendanceTimeSheetService.queryByDateRange(timeRange).subscribe((res: HttpResponse<IMonthlyAttendanceTimeSheet[]>) => {
        this.monthlyAttendanceTimeSheets = res.body || [];
        this.isLoading = false;
      });
    });*/

    // this.setInitialDateInDatePicker();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInAttendanceEntries();
    this.setInitialDateInDatePicker();
    this.loadFromRange();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  setInitialDateInDatePicker(): void {
    /*this.editForm.controls['endDate'].setValue(this.today.toISOString().substring(0, 10));
    this.editForm.controls['startDate'].setValue(this.monthFromToday.toISOString().substring(0, 10));*/
    this.editForm.controls['endDate'].setValue(dayjs(this.today));
    this.editForm.controls['startDate'].setValue(dayjs(this.monthFromToday) as any);
  }

  registerChangeInAttendanceEntries(): void {
    this.eventSubscriber = this.eventManager.subscribe('attendanceEntryListModification', () => this.loadFromRange());
  }

  public truncate(status: any): string {
    if (status == null) return '';
    this.ch = status;
    if (status.length <= 10) return status;
    return status.slice(0, 10) + '...';
  }

  public convertMinsToHrsMins(workinghour: any): string {
    if (workinghour === null || workinghour === undefined) return '00:00';
    this.h = Math.floor(workinghour);
    this.m = workinghour - this.h;
    this.m = Math.floor(this.m * 100);
    this.hour = this.h < 10 ? '0' + this.h : this.h + '';
    this.minute = this.m < 10 ? '0' + this.m : this.m + '';

    return this.hour > 0 ? this.hour + ' hour ' + this.minute + ' minutes' : this.minute + ' minutes';
  }

  public getTimeStringFromDecimalValue(workinghour: any): string {
    if (workinghour === null || workinghour === undefined) return '00:00';
    const h = Math.floor(workinghour);
    const m = Math.floor(workinghour * 100 - h * 100);
    const hour = h < 10 ? '0' + h : h + '';
    const minute = m < 10 ? '0' + m : m + '';
    return hour + ':' + minute;
  }

  private createFromForm(): DateRangeDTO {
    return {
      ...new DateRangeDTO(),
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
    };
  }

  loadLastThirtyDays(): void {
    this.setInitialDateInDatePicker();
    const timeRange = this.createFromForm();
    this.attendanceTimeSheetService.queryByDateRange(timeRange).subscribe((res: HttpResponse<IMonthlyAttendanceTimeSheet[]>) => {
      this.monthlyAttendanceTimeSheets = res.body || [];
    });
  }

  getTotalDays(attendanceTimeSheetMiniList: IAttendanceTimeSheetMini[]): Number {
    return attendanceTimeSheetMiniList.length;
  }

  getPresentDays(attendanceTimeSheetMiniList: IAttendanceTimeSheetMini[]): Number {
    return attendanceTimeSheetMiniList.filter(obj => obj.attendanceStatus === 'PRESENT').length;
  }

  getLeaveDays(attendanceTimeSheetMiniList: IAttendanceTimeSheetMini[]): Number {
    return attendanceTimeSheetMiniList.filter(obj => obj.attendanceStatus === 'LEAVE').length;
  }

  getHolidays(attendanceTimeSheetMiniList: IAttendanceTimeSheetMini[]): Number {
    return attendanceTimeSheetMiniList.filter(obj => obj.attendanceStatus === 'WEEKLY_OFFDAY' || obj.attendanceStatus === 'GOVT_HOLIDAY')
      .length;
  }

  getAbsentDays(attendanceTimeSheetMiniList: IAttendanceTimeSheetMini[]): Number {
    return attendanceTimeSheetMiniList.filter(obj => obj.attendanceStatus === 'ABSENT').length;
  }

  checkDate(): void {
    const startDate = this.editForm.get(['startDate'])!.value;
    const endDate = this.editForm.get(['endDate'])!.value;

    if (startDate !== undefined && endDate !== undefined && startDate > endDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
  }

  onChange(): void {
    // this.loadFromRange();
  }

  selectorOnChange($event: any): void {
    const employeeId = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.monthlyAttendanceTimeSheets !== undefined) {
      this.monthlyAttendanceTimeSheets = this.monthlyAttendanceTimeSheets.map(ats => {
        if (ats.employeeId === employeeId) {
          ats.isChecked = isChecked;
          this.allSelector = false;
          return ats;
        }
        if (employeeId === -1) {
          ats.isChecked = this.allSelector;
          return ats;
        }
        return ats;
      });
    }
    // clear previous set
    this.selectedIdSetForATS.clear();
    this.selectedEmployeeAts = [];
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    for (let i = 0; i < this.monthlyAttendanceTimeSheets!.length; i++) {
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
      if (this.monthlyAttendanceTimeSheets![i].isChecked === true) {
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
        this.selectedEmployeeAts.push(this.monthlyAttendanceTimeSheets![i]);
        this.selectedIdSetForATS.add(this.monthlyAttendanceTimeSheets![i].employeeId);
      }
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSetForATS.values()).map(value => value as number);
  }

  exportSelectedATS(): void {
    const fileName = 'monthlyAts.xlsx';
    this.attendanceTimeSheetService.exportMonthlyAtsInExcel(this.selectedEmployeeAts).subscribe(x => {
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
    });
  }

  exportSelectedATSv2(): void {
    const fileName = 'monthlyAts.xlsx';
    const employeeIdList: (number | undefined)[] = this.selectedEmployeeAts.map(item => item.employeeId);
    let startDate = this.editForm.get(['startDate'])!.value;
    let endDate = this.editForm.get(['endDate'])!.value;

    let req: IMonthlyAttendanceTimeSheetEmployeeList = {
      employeeIdList: employeeIdList,
      startDate: startDate,
      endDate: endDate,
    };

    this.attendanceTimeSheetService.exportMonthlyAtsInExcelv2(req).subscribe(x => {
      // It is necessary to create a new blob object with mime-type explicitly set
      // otherwise only Chrome works like it should
      const newBlob = new Blob([x], { type: 'application/octet-stream' });

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
    });
  }
}
