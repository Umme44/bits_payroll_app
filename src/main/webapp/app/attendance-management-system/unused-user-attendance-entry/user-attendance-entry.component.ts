import { Component, OnDestroy, OnInit } from '@angular/core';
import { UserAttendanceEntryService } from './user-attendance-entry.service';
import { DatePipe } from '@angular/common';
import { HttpResponse } from '@angular/common/http';
import { Observable, Subscription, timer } from 'rxjs';
import { map, share } from 'rxjs/operators';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { AttendanceEntry, IAttendanceEntry } from '../../shared/legacy/legacy-model/attendance-entry.model';
import { EmployeeService } from '../../shared/legacy/legacy-service/employee.service';
import { DATE_TIME_FORMAT } from '../../config/input.constants';
import dayjs from 'dayjs/esm';
import { SaveComponentComponent } from '../../shared/save-component/save-component.component';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
@Component({
  selector: 'jhi-user-attendance-entry',
  templateUrl: './user-attendance-entry.component.html',
  styleUrls: ['user-attendance-entry.component.scss'],
  providers: [DatePipe],
})
export class UserAttendanceEntryComponent implements OnInit, OnDestroy {
  rxTime = new Date();
  subscription: Subscription | undefined;

  isSaving = false;
  employees: IEmployee[] = [];

  attendanceEntry: IAttendanceEntry | null = null;

  currentTimeString!: string;
  checkInTimeString!: string;
  checkOutTimeString!: string;
  checkInTime!: Date;
  checkOutTime!: Date;
  workingTimeDifference!: string;
  clockInBtnText!: string;
  clockOutBtnText!: string;
  isLate!: boolean;

  editForm = this.fb.group({
    id: [],
    date: [],
    inTime: [],
    inNote: [],
    outTime: [],
    outNote: [],
    status: [],
    employeeId: [],
  });

  checkInDone = false;
  checkOutDone = false;
  isCheckInAndCheckOutBothDone?: boolean;

  checkInSuccess?: boolean;
  checkOutSuccess?: boolean;

  constructor(
    protected userAttendanceEntryService: UserAttendanceEntryService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    protected modalService: NgbModal
  ) {
    this.checkInSuccess = false;
    this.checkOutSuccess = false;
    this.clockInBtnText = 'Clock In';
    this.clockOutBtnText = 'Clock Out';
    this.isLate = false;
    // setInterval(this.getUpdatedCurrentTime, 1000);
  }

  ngOnInit(): void {
    // Using RxJS Timer
    this.subscription = timer(0, 1000)
      .pipe(
        map(() => new Date()),
        share()
      )
      .subscribe(time => {
        this.rxTime = time;
        time.getHours() >= 10 ? (this.clockInBtnText = 'Late In') : 'Clock In';
        time.getHours() >= 10 ? (this.isLate = true) : false;
      });

    this.loadStatus();
  }

  ngOnDestroy(): void {
    if (this.subscription) {
      this.subscription.unsubscribe();
    }
  }

  updateForm(attendanceEntry: IAttendanceEntry): void {
    this.editForm.patchValue({
      id: attendanceEntry.id,
      date: attendanceEntry.date,
      inTime: attendanceEntry.inTime ? attendanceEntry.inTime.format(DATE_TIME_FORMAT) : null,
      inNote: attendanceEntry.inNote,
      outTime: attendanceEntry.outTime ? attendanceEntry.outTime.format(DATE_TIME_FORMAT) : null,
      outNote: attendanceEntry.outNote,
      status: attendanceEntry.status,
      employeeId: attendanceEntry.employeeId,
    });
  }

  previousState(): void {
    window.history.back();
  }

  loadStatusWithoutFlags(): void {
    this.userAttendanceEntryService.todayStatus().subscribe((res: HttpResponse<IAttendanceEntry>) => {
      this.attendanceEntry = res.body || null;
    });
  }

  loadStatus(): void {
    this.userAttendanceEntryService.todayStatus().subscribe((res: HttpResponse<IAttendanceEntry>) => {
      this.attendanceEntry = res.body || null;

      if (this.attendanceEntry !== null) {
        this.checkInDone = this.attendanceEntry.inTime !== undefined;
        this.checkOutDone = this.attendanceEntry.outTime !== undefined;
        this.isCheckInAndCheckOutBothDone = this.attendanceEntry.outTime !== undefined;
        this.clockOutBtnText = this.attendanceEntry.outTime !== undefined ? 'Clock Out Again' : 'Clock Out';
        if (this.attendanceEntry.inTime !== undefined) this.checkInTime = this.attendanceEntry.inTime.toDate();
        if (this.attendanceEntry.outTime !== undefined) this.checkOutTime = this.attendanceEntry.outTime.toDate();
        if (this.attendanceEntry.inTime !== undefined) this.checkInTimeString = this.getTimeString(this.attendanceEntry.inTime.toDate());
        if (this.attendanceEntry.outTime !== undefined) this.checkOutTimeString = this.getTimeString(this.attendanceEntry.outTime.toDate());

        this.workingTimeDifference = this.getWorkingTimeDifference(this.checkInTime, this.checkOutTime);
      } else {
        this.checkInDone = false;
        this.checkOutDone = false;
        this.isCheckInAndCheckOutBothDone = false;
      }
    });
  }

  checkIn(): void {
    // if (confirm('Are you sure you want to check in?')) {
    const modalRef = this.modalService.open(SaveComponentComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.headerText = 'Confirm Check In';
    modalRef.componentInstance.text = 'Are you sure you want to check in?';
    modalRef.componentInstance.leftButton = 'No';
    modalRef.componentInstance.rightButton = 'Yes';

    modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
      if (receivedEntry) {
        this.checkInTimeString = this.getUpdatedCurrentTime();
        this.checkInTime = new Date();
        this.clockOutBtnText = 'Clock Out';
        this.checkInDone = true;
        this.save();
        this.checkInSuccess = true;
        setTimeout(() => (this.checkInSuccess = false), 2000);
        // }
      }
    });
  }

  // checkout again not working properly
  checkOut(): void {
    // if (confirm('Are you sure you want to ' + this.clockOutBtnText.toLowerCase() + '?')) {

    const modalRef = this.modalService.open(SaveComponentComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.headerText = 'Confirm Checkout';
    modalRef.componentInstance.text = 'Are you sure you want to ' + this.clockOutBtnText.toLowerCase() + '?';
    modalRef.componentInstance.leftButton = 'No';
    modalRef.componentInstance.rightButton = 'Yes';

    modalRef.componentInstance.passEntry.subscribe((receivedEntry: boolean) => {
      if (receivedEntry) {
        this.checkInDone = true;
        this.checkOutDone = true;
        this.checkOutTimeString = this.getUpdatedCurrentTime();
        this.checkOutTime = new Date();
        this.isCheckInAndCheckOutBothDone = true;
        this.clockOutBtnText = 'Clock Out Again';
        this.workingTimeDifference = this.getWorkingTimeDifference(this.checkInTime, this.checkOutTime);
        this.save();
        this.checkOutSuccess = true;
        setTimeout(() => (this.checkOutSuccess = false), 2000);
        // }
      }
    });
  }

  save(): void {
    this.isSaving = true;
    const attendanceEntry = this.createFromForm();
    this.loadStatusWithoutFlags();
  }

  trackById(index: number, item: IEmployee): any {
    return item.id;
  }

  public getUpdatedCurrentTime(): string {
    const currentTime = new Date();
    currentTime.getHours() >= 10 ? (this.clockInBtnText = 'Late In') : 'Clock In';
    currentTime.getHours() >= 10 ? (this.isLate = true) : false;
    return this.getTimeString(currentTime);
  }

  getTimeString(date: Date): string {
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();

    let timeString = hours % 12 === 0 ? '12' : hours % 12;
    timeString = timeString + ':' + (minutes < 10 ? '0' : '') + minutes + ':' + (seconds < 10 ? '0' : '') + seconds + ' ';
    if (hours > 11) {
      timeString += 'PM';
    } else {
      timeString += 'AM';
    }
    this.currentTimeString = timeString;

    return timeString;
  }

  getWorkingTimeDifference(start: Date, end: Date): string {
    const diff = Math.abs(start.getTime() - end.getTime());
    const hours = Math.floor(diff / 36e5);
    const mins = Math.floor(diff / 1000 / 60) % 60;
    const hourString: string = hours.toString() + ' ' + (hours > 1 ? 'hours' : 'hour');
    const minString: string = mins.toString() + ' ' + (mins > 1 ? 'minutes' : 'minute');
    return hours > 0 ? hourString + ' and ' + minString : minString;
  }

  isCheckOutDone(): boolean {
    return this.checkOutDone;
  }

  isCheckInDone(): boolean {
    return this.checkInDone;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAttendanceEntry>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  private createFromForm(): IAttendanceEntry {
    return {
      ...new AttendanceEntry(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      inTime: this.editForm.get(['inTime'])!.value ? dayjs(this.editForm.get(['inTime'])!.value, DATE_TIME_FORMAT) : undefined,
      inNote: this.editForm.get(['inNote'])!.value,
      outTime: this.editForm.get(['outTime'])!.value ? dayjs(this.editForm.get(['outTime'])!.value, DATE_TIME_FORMAT) : undefined,
      outNote: this.editForm.get(['outNote'])!.value,
      status: this.editForm.get(['status'])!.value,
      employeeId: this.editForm.get(['employeeId'])!.value,
    };
  }
}
