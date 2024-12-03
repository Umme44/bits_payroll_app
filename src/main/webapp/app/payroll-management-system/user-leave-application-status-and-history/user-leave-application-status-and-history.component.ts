import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { LeaveType } from 'app/shared/model/enumerations/leave-type.model';
import { LeaveSummaryEndUserViewService } from 'app/attendance-management-system/leave-summary-end-user-view/leave-summary-end-user-view.component.service';
import { swalOnDeleteConfirmation, swalOnDeleteSuccess, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import { ILeaveApplication } from '../../shared/legacy/legacy-model/leave-application.model';
import { ILeaveBalanceEndUserView } from '../../shared/legacy/legacy-model/leave-balance-end-user-view.model';
import { EventManager } from '../../core/util/event-manager.service';
import { LeaveApplicationService } from '../../shared/legacy/legacy-service/leave-application.service';

@Component({
  selector: 'jhi-user-leave-application-status-and-history',
  templateUrl: './user-leave-application-status-and-history.component.html',
  styleUrls: ['user-leave-application-status-and-history.component.scss'],
})
export class UserLeaveApplicationStatusAndHistoryComponent implements OnInit, OnDestroy {
  leaveApplications?: ILeaveApplication[];
  leaveBalanceEndUserView?: ILeaveBalanceEndUserView[];
  eventSubscriber?: Subscription;

  annualLeave = 0;
  casualLeave = 0;
  compensatoryLeave = 0;
  pandemicLeave = 0;
  paternityLeave = 0;
  maternityLeave = 0;

  selectedLeaveDetails!: ILeaveApplication;

  constructor(
    protected leaveSummaryEndUserViewService: LeaveSummaryEndUserViewService,
    protected leaveApplicationService: LeaveApplicationService,
    protected eventManager: EventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.leaveApplicationService.userLeaveApplicationStatusAndHistory().subscribe((res: HttpResponse<ILeaveApplication[]>) => {
      this.leaveApplications = res.body || [];
      this.leaveApplications?.sort((leave1, leave2) => {
        return leave2.startDate!.toDate().getTime() - leave1.startDate!.toDate().getTime();
      });
    });
  }

  ngOnInit(): void {
    this.loadAll();
    this.loadMyLeaveBalance();
    this.registerChangeInLeaveApplications();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
    this.modalService.dismissAll();
  }

  trackId(index: number, item: ILeaveApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLeaveApplications(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveApplicationListModification', () => this.loadAll());
  }

  delete(leaveApplication: ILeaveApplication): void {
    swalOnDeleteConfirmation().then(result => {
      if (result.isConfirmed) {
        this.leaveApplicationService.deleteUserLeaveApplication(leaveApplication.id!).subscribe(
          () => this.deleteOnSuccess(),
          () => this.deleteOnError()
        );
      }
    });
  }

  deleteOnSuccess(): void {
    swalOnDeleteSuccess();
    this.loadAll();
    this.loadMyLeaveBalance();
  }

  deleteOnError(): void {
    swalOnRequestError();
  }

  public truncate(msg: any): string {
    if (msg == null) return '';
    if (msg.length <= 100) return msg;
    const str = msg.slice(0, 100) + '...';
    return str;
  }

  public getString(msg: any): string {
    return msg.toString();
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

  totalLeaveCalculation(openingBalance: any, leaveBalance: any): number {
    return openingBalance + leaveBalance;
  }

  open(content: any, leaveApplication: ILeaveApplication): void {
    const modalRef = this.modalService.open(content, { size: 'lg' });
    this.selectedLeaveDetails = leaveApplication;
  }
}
