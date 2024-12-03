import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { MonthlyAttendanceTimeSheetService } from 'app/attendance-management-system/ats/monthly-attendance-time-sheet/monthly-attendance-time-sheet.service';
import { ITimeRange } from 'app/shared/model/time-range.model';
import Swal from 'sweetalert2';
import { EventManager } from 'app/core/util/event-manager.service';
import { SWAL_CANCEL_BTN_TEXT, SWAL_CONFIRM_BTN_TEXT, SWAL_SURE } from 'app/shared/swal-common/swal.properties.constant';
import { ILeaveApplication } from '../../../shared/legacy/legacy-model/leave-application.model';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';

@Component({
  selector: 'jhi-pre-regularize-modal',
  templateUrl: './pre-regularize-modal.component.html',
})
export class PreRegularizeModalComponent implements OnInit {
  @Input()
  employeeId!: number;

  @Input()
  timeRange!: ITimeRange;

  @Output() passEntry: EventEmitter<boolean> = new EventEmitter();

  leaveApplications!: ILeaveApplication[];
  noLeaveApplications = true;

  constructor(
    private activeModal: NgbActiveModal,
    private monthlyAttendanceTimeSheetService: MonthlyAttendanceTimeSheetService,
    protected eventManager: EventManager
  ) {}

  ngOnInit(): void {
    this.monthlyAttendanceTimeSheetService.autoCutLeaveSummary(this.timeRange).subscribe(res => {
      this.leaveApplications = res.body!;
      if (this.leaveApplications.length > 0) this.noLeaveApplications = false;
    });
  }

  leaveRegularize(): void {
    //sweat alert for confirmation
    Swal.fire({
      text: SWAL_SURE,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        Swal.fire({
          html: 'Regularizing...',
          allowOutsideClick: false,
          timerProgressBar: true,
          didOpen(): void {
            Swal.showLoading();
          },
        });
        this.autoLeaveCut();
      }
    });
  }

  autoLeaveCut(): void {
    const timeRange = this.timeRange;
    //this.isLoading = true;
    this.monthlyAttendanceTimeSheetService.autoCutLeave(timeRange).subscribe((response: HttpResponse<boolean>) => {
      Swal.close();
      this.eventManager.broadcast('attendanceEntryListModification');
      this.activeModal.close();
    });
  }

  cancel(): void {
    this.passEntry.emit(false);
    this.activeModal.dismiss();
  }

  export(type: String): void {
    const fileName = 'leave_regularize_' + this.timeRange.startDate + '_' + this.timeRange.endDate + '_' + '' + '.xlsx';
    /*if (type === 'short-summary') fileName = type + '_report_' + '' + '_' + '' + '.csv';
    if (type === 'summary') fileName = type + '_report_' + '' + '_' + '' + '.csv';*/
    this.monthlyAttendanceTimeSheetService.autoCutLeaveSummaryReport(this.timeRange).subscribe(x => {
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

  trackId(index: number, item: ILeaveApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }
}
