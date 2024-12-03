import { Component, OnInit } from '@angular/core';
import dayjs from 'dayjs/esm';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { EventManager } from 'app/core/util/event-manager.service';
import { ActivatedRoute } from '@angular/router';
import { swalForWarningWithMessage, swalSuccessWithMessage } from 'app/shared/swal-common/swal-common';
import { HttpResponse } from '@angular/common/http';
import { LeaveType } from 'app/shared/model/enumerations/leave-type.model';
import { swalConfirmationWithMessage } from 'app/shared/swal-common/swal-confirmation.common';
import { LeaveSummaryEndUserViewService } from 'app/attendance-management-system/leave-summary-end-user-view/leave-summary-end-user-view.component.service';
import { ILeaveBalanceEndUserView } from '../../../shared/legacy/legacy-model/leave-balance-end-user-view.model';
import { LeaveApplicationService } from '../../../shared/legacy/legacy-service/leave-application.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { ILeaveApplication, LeaveApplication } from '../../../shared/legacy/legacy-model/leave-application.model';
import { DATE_FORMAT } from '../../../config/input.constants';

@Component({
  templateUrl: './ats-admin-leave-application-dialog.component.html',
  styleUrls: ['./attendance-time-sheet-admin.component.scss', '../../../dashboard/dashboard.scss'],
})
export class AtsAdminLeaveApplicationDialogComponent implements OnInit {
  employeeId!: number;

  selectedDateForLeaveApply!: any;
  selectedLeaveType!: LeaveType;
  remainingLeaveBalance!: number;
  leaveApplyWaringMsg = '';

  leaveBalanceEndUserView?: ILeaveBalanceEndUserView[];

  remainingAnnualLeave = 0;
  remainingCasualLeave = 0;
  remainingCompensatoryLeave = 0;

  constructor(
    protected leaveApplicationService: LeaveApplicationService,
    protected leaveSummaryEndUserViewService: LeaveSummaryEndUserViewService,
    protected eventManager: EventManager,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    public activeModal: NgbActiveModal,
    protected modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loadRemainingLeaveBalanceByEmployeeAndYear();
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  createLeaveApplication(): ILeaveApplication {
    return {
      ...new LeaveApplication(),
      applicationDate: this.selectedDateForLeaveApply,
      leaveType: this.selectedLeaveType,
      description: 'Automated Regularization by HR',
      startDate: this.selectedDateForLeaveApply,
      endDate: this.selectedDateForLeaveApply,
      isLineManagerApproved: false,
      isHRApproved: true,
      isRejected: false,
      rejectionComment: undefined,
      isHalfDay: false,
      durationInDay: 1,
      phoneNumberOnLeave: undefined,
      addressOnLeave: undefined,
      reason: '',
      employeeId: this.employeeId,
    };
  }

  preValidateLeaveApplication(leaveType: string): void {
    this.leaveApplyWaringMsg = '';
    this.remainingLeaveBalance = 0;

    if (leaveType === 'casual') {
      this.selectedLeaveType = LeaveType.MENTIONABLE_CASUAL_LEAVE;
    } else if (leaveType === 'annual') {
      this.selectedLeaveType = LeaveType.MENTIONABLE_ANNUAL_LEAVE;
    } else if (leaveType === 'compensatory') {
      this.selectedLeaveType = LeaveType.NON_MENTIONABLE_COMPENSATORY_LEAVE;
    } else if (leaveType === 'leaveWithoutPay') {
      this.selectedLeaveType = LeaveType.LEAVE_WITHOUT_PAY;
    } else {
      swalForWarningWithMessage(leaveType + ' is unknown!');
    }

    const leaveApplication = this.createLeaveApplication();

    this.leaveApplicationService.hasAnyApprovedAndPendingLeaveByEmployeeIdAndDateRange(leaveApplication).subscribe(hasAny => {
      if (hasAny.body === true) {
        swalForWarningWithMessage('Already leave applied for ' + dayjs(this.selectedDateForLeaveApply).format(DATE_FORMAT));
      } else {
        if (leaveApplication.leaveType === LeaveType.MENTIONABLE_CASUAL_LEAVE) {
          this.leaveApplicationService.getMonthlyRemainingCasualLeave(leaveApplication).subscribe(res => {
            const remainingMonthlyCasualLeave = res.body!;
            if (remainingMonthlyCasualLeave <= 0) {
              swalConfirmationWithMessage(`Already 02** Casual Leave Applied for this month.`, 'Proceed').then(result => {
                if (result.isConfirmed) {
                  this.applyLeaveByHR(leaveApplication, leaveType);
                }
              });
            } else {
              this.applyLeaveByHR(leaveApplication, leaveType);
            }
          });
        } else if (leaveApplication.leaveType === LeaveType.LEAVE_WITHOUT_PAY) {
          this.leaveApplicationService.applyLeaveAndApprove(leaveApplication).subscribe(res => {
            swalSuccessWithMessage('Leave Applied and Approved!');
            this.modalService.dismissAll();
          });
        } else {
          this.applyLeaveByHR(leaveApplication, leaveType);
        }
      }
    });
  }

  applyLeaveByHR(leaveApplication: ILeaveApplication, leaveType: string): void {
    this.leaveApplicationService.getRemainingLeaveBalance(leaveApplication).subscribe(
      res => {
        this.remainingLeaveBalance = res.body!;
      },
      error => {},
      () => {
        if (this.remainingLeaveBalance >= 1) {
          this.leaveApplicationService.applyLeaveAndApprove(leaveApplication).subscribe(res => {
            swalSuccessWithMessage('Leave Applied and Approved!');
            this.modalService.dismissAll();
          });
        } else {
          swalForWarningWithMessage(`Remaining ${leaveType} leave: ${this.remainingLeaveBalance}`);
        }
      }
    );
  }

  loadRemainingLeaveBalanceByEmployeeAndYear(): void {
    if (this.employeeId) {
      const year = new Date(this.selectedDateForLeaveApply).getFullYear();
      this.leaveSummaryEndUserViewService
        .loadByYearAndEmployeeId(year, this.employeeId)
        .subscribe((resp: HttpResponse<ILeaveBalanceEndUserView[]>) => {
          this.leaveBalanceEndUserView = resp.body || [];

          const annualLeaveDetails = this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.MENTIONABLE_ANNUAL_LEAVE)[0]!;

          if (annualLeaveDetails) {
            this.remainingAnnualLeave =
              annualLeaveDetails.openingBalance +
              annualLeaveDetails.amount -
              (annualLeaveDetails.daysRequested - annualLeaveDetails.daysCancelled);
          }

          const casualLeaveDetails = this.leaveBalanceEndUserView.filter(x => x.leaveType === LeaveType.MENTIONABLE_CASUAL_LEAVE)[0];

          if (casualLeaveDetails) {
            this.remainingCasualLeave =
              casualLeaveDetails.openingBalance +
              casualLeaveDetails.amount -
              (casualLeaveDetails.daysRequested - casualLeaveDetails.daysCancelled);
          }

          const compensatoryLeaveDetails = this.leaveBalanceEndUserView.filter(
            x => x.leaveType === LeaveType.NON_MENTIONABLE_COMPENSATORY_LEAVE
          )[0];

          if (compensatoryLeaveDetails) {
            this.remainingCompensatoryLeave =
              compensatoryLeaveDetails.openingBalance +
              compensatoryLeaveDetails.amount -
              (compensatoryLeaveDetails.daysRequested - compensatoryLeaveDetails.daysCancelled);
          }
        });
    } else {
      swalForWarningWithMessage('Employee Id is undefined');
    }
  }
}
