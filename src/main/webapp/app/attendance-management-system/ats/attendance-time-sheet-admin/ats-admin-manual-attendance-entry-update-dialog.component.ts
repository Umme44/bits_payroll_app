import { Component, OnInit } from '@angular/core';
import dayjs from 'dayjs/esm';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FormBuilder, Validators } from '@angular/forms';
import { EventManager } from 'app/core/util/event-manager.service';
import { ActivatedRoute } from '@angular/router';
import { swalSuccessWithMessage } from 'app/shared/swal-common/swal-common';
import { IManualAttendanceEntry, ManualAttendanceEntry } from '../../../shared/legacy/legacy-model/manual-attendance-entry.model';
import { ManualAttendanceEntryService } from '../../../shared/legacy/legacy-service/manual-attendance-entry.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { TIME_FORMAT } from '../../../config/input.constants';

@Component({
  templateUrl: './ats-admin-manual-attendance-entry-update-dialog.component.html',
  styleUrls: ['./attendance-time-sheet-admin.component.scss', '../../../dashboard/dashboard.scss'],
})
export class ATSAdminManualAttendanceEntryDialogComponent implements OnInit {
  isInvalidManualAttendanceEntry = false;

  employeeId!: number;
  manualAttendanceEntry!: IManualAttendanceEntry;
  selectedDate!: any;

  manualAttendanceEditForm = this.fb.group({
    id: [],
    date: ['', [Validators.required]],
    inTime: ['', [Validators.required]],
    inNote: ['', [Validators.maxLength(255)]],
    outTime: [''],
    outNote: ['', [Validators.maxLength(255)]],
    note: ['', [Validators.maxLength(255)]],
    status: [],
    isLineManagerApproved: [false],
    isHRApproved: [true],
    isRejected: [false],
    rejectionComment: [],
    employeeId: [],
    startDate: [],
    endDate: [],
  });

  constructor(
    protected manualAttendanceEntryService: ManualAttendanceEntryService,
    protected eventManager: EventManager,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    public activeModal: NgbActiveModal,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.updateManualAttendanceEntryForm(this.manualAttendanceEntry, this.selectedDate);
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  createManualAttendanceEntry(): IManualAttendanceEntry {
    return {
      ...new ManualAttendanceEntry(),
      date: this.manualAttendanceEditForm.get('date')!.value as any,
      inTime: this.manualAttendanceEditForm.get(['inTime'])!.value
        ? dayjs(this.manualAttendanceEditForm.get(['inTime'])!.value, TIME_FORMAT)
        : undefined,
      outTime: this.manualAttendanceEditForm.get(['outTime'])!.value
        ? dayjs(this.manualAttendanceEditForm.get(['outTime'])!.value, TIME_FORMAT)
        : undefined,
      isHRApproved: true,
      isLineManagerApproved: false,
      isRejected: false,
      employeeId: this.employeeId,
      note: 'HR Regularize',
    };
  }

  updateManualAttendanceEntryForm(manualAttendanceEntry: IManualAttendanceEntry, day?: any): void {
    this.manualAttendanceEditForm.patchValue({
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

  applyManualAttendanceEntry(): void {
    const manualAttendanceEntry = this.createManualAttendanceEntry();
    this.manualAttendanceEntryService.applyAndApproveByHR(manualAttendanceEntry).subscribe(res => {
      swalSuccessWithMessage('Attendance Applied and Approved!');
      this.modalService.dismissAll();
    });
  }

  checkTime(): void {
    const inTime = this.manualAttendanceEditForm.get(['inTime'])!.value;
    const outTime = this.manualAttendanceEditForm.get(['outTime'])!.value;

    if (inTime && outTime) {
      if (inTime > outTime) {
        this.isInvalidManualAttendanceEntry = true;
      } else {
        this.isInvalidManualAttendanceEntry = false;
      }
    }
  }
}
