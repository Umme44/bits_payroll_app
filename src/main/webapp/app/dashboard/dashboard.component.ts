import { Component, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { AccountService } from 'app/core/auth/account.service';
import { DatePipe } from '@angular/common';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { NgbCarouselConfig, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DashboardService } from './dashboard.service';
import dayjs from 'dayjs/esm';
import Swal from 'sweetalert2';
import { DANGER_COLOR, PRIMARY_COLOR } from '../config/color.code.constant';
import { swalForWarningWithMessage, swalOnRequestError } from '../shared/swal-common/swal-common';
import { ApprovalsModalComponent } from './approvals-modal/approvals-modal.component';
import { AttendancesModalComponent } from './attendances-modal/attendances-modal.component';
import { MyStuffsModalComponent } from './my-stuffs-modal/my-stuffs-modal.component';
import { NoticeDetailsModalComponent } from './notice-details-modal/notice-details-modal.component';
import { OfferDetailsModalComponent } from './offer-details-modal/offer-details-modal.component';
import { INotification } from '../shared/model/notification.model';
import { NotificationService } from '../layouts/navbar/notification.service';
import { RequisitionModalComponent } from './requisition-modal/requisition-modal.component';
import { IDateRangeDTO } from '../shared/model/DateRangeDTO';
import { StatementDetailsModalComponent } from './statement-details-modal/statement-details-modal.component';
import { PayslipDetailsModalComponent } from './payslip-details-modal/payslip-details-modal';
import { NomineeDetailsModalComponent } from './nominee-details-modal/nominee-details-modal';
import { IDashboardItemAccessControl } from '../shared/model/dashboard-item-access-control.model';
import { Account } from '../core/auth/account.model';
import { AttendanceTimeSheetService } from '../shared/service/attendance-time-sheet.service';
import { UserPfStatementService } from '../shared/service/user-pf-statement.service';
import { AttendanceTimeSheet, IAttendanceTimeSheet } from '../shared/model/attendance-time-sheet.model';
import { UserAttendanceEntryService } from '../shared/service/user-attendance-entry.service';
import { AttendanceStatus } from '../shared/model/enumerations/attendance-status.model';
import {
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DEFAULT_TIMER,
  SWAL_DENY_BTN_TEXT,
  SWAL_REJECTED_ICON,
} from '../shared/swal-common/swal.properties.constant';
import { IRrfRaiseValidity } from '../shared/model/rrf-raise-validity.model';
import { EmployeeService } from '../shared/legacy/legacy-service/employee.service';
import { IAttendanceEntry } from '../shared/legacy/legacy-model/attendance-entry.model';
import { IOfficeNotices } from '../shared/legacy/legacy-model/office-notices.model';
import { IOffer } from '../shared/legacy/legacy-model/offer.model';
import { AttendanceService } from '../shared/legacy/legacy-service/attendance.service';
import { OfferService } from '../shared/legacy/legacy-service/offer.service';
import { RecruitmentRequisitionFormService } from '../shared/legacy/legacy-service/recruitment-requisition-form.service';

@Component({
  selector: 'jhi-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['dashboard.scss'],
  providers: [DatePipe],
})
export class DashboardComponent implements OnInit, OnDestroy {
  subscription: Subscription | undefined;
  haveIAnySubordinate = false;

  account: Account | null = null;
  authSubscription?: Subscription;

  attendanceEntry: IAttendanceEntry | null = null;

  currentTimeString!: string;
  checkInTimeString!: string;
  checkOutTimeString!: string;

  dashboardItemAccessControl!: IDashboardItemAccessControl;

  checkInDone = false;
  checkOutDone = false;

  attendanceTimeSheets?: IAttendanceTimeSheet[];

  officeNotices: IOfficeNotices[] = [];

  isValidUserPfStatement!: boolean;
  recentOfficeNotices: IOfficeNotices[] = [];
  recentOffers: IOffer[] = [];
  selectedType = 'notice';
  notification!: INotification;

  constructor(
    private accountService: AccountService,
    protected userAttendanceEntryService: UserAttendanceEntryService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder,
    private config: NgbCarouselConfig,
    protected modalService: NgbModal,
    protected attendanceTimeSheetService: AttendanceTimeSheetService,
    protected dashboardService: DashboardService,
    protected userPfStatementService: UserPfStatementService,
    protected offerService: OfferService,
    private notificationService: NotificationService,
    private recruitmentRequisitionFormService: RecruitmentRequisitionFormService,
    private router: Router,
    private attendanceService: AttendanceService
  ) {}

  ngOnInit(): void {
    if (!this.isAuthenticated()) {
      this.login();
    }
    this.authSubscription = this.accountService.getAuthenticationState().subscribe(account => (this.account = account));

    this.loadStatus();
    this.loadAllAttendances();
    this.loadUserPfStatementValidity();
    this.anySubOrdinate();
    this.loadRecentOfficeNotices();

    this.openNavigationModal();

    if (sessionStorage.getItem('dashboardItemAccessControl') === null) {
      this.loadDashboardItemsAccessibility();
    }
  }

  loadDashboardItemsAccessibility(): void {
    this.dashboardService.getAllAccessControl().subscribe(res => {
      this.dashboardItemAccessControl = res.body!;
      sessionStorage.setItem('dashboardItemAccessControl', JSON.stringify(this.dashboardItemAccessControl));
    });
  }

  anySubOrdinate(): void {
    this.dashboardService.anySubOrdinate().subscribe(res => {
      if (res.body && res.body === true) {
        this.haveIAnySubordinate = true;
      }
    });
  }

  loadUserPfStatementValidity(): void {
    this.userPfStatementService.checkValidityOfUserPfStatement().subscribe((response: boolean) => {
      this.isValidUserPfStatement = response;
    });
  }

  isAuthenticated(): boolean {
    return this.accountService.isAuthenticated();
  }

  login(): void {
    this.router.navigate(['/login']);
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }

    if (this.subscription) {
      this.subscription.unsubscribe();
    }

    // dismiss all Modal, if navigate to other page
    this.modalService.dismissAll();
  }

  loadAllAttendances(): void {
    this.attendanceTimeSheetService.dashboardUserAttendance().subscribe(res => {
      return (this.attendanceTimeSheets = res.body || []);
    });
    if (!this.attendanceTimeSheets || this.attendanceTimeSheets.length === 0) {
      const today = dayjs();
      this.attendanceTimeSheets = [];
      for (let i = 0; i < 6; i++) {
        this.attendanceTimeSheets.push({
          ...new AttendanceTimeSheet(),
          date: dayjs(today, 'DD-MM-YYYY').subtract(i, 'days'),
          attendanceStatus: AttendanceStatus.ABSENT,
        });
      }
    }
  }

  loadRecentOfficeNotices(): void {
    this.dashboardService.findAllRecentNotices().subscribe(res => {
      this.recentOfficeNotices = (res.body || []) as any;
    });
  }

  loadStatus(): void {
    this.userAttendanceEntryService.todayStatus().subscribe(res => {
      this.attendanceEntry = res.body || null;

      if (this.attendanceEntry !== null) {
        this.checkInDone = this.attendanceEntry.inTime !== undefined;
        this.checkOutDone = this.attendanceEntry.outTime !== undefined;
        //this.clockOutBtnText = this.attendanceEntry.outTime !== undefined ? 'Punch Out' : 'Punch In';

        if (this.attendanceEntry.inTime !== undefined && this.attendanceEntry.inTime !== null) {
          this.checkInTimeString = this.getTimeStringFromDate(this.attendanceEntry.inTime.toDate(), true, false);
        }
        if (this.attendanceEntry.outTime !== undefined && this.attendanceEntry.outTime !== null) {
          this.checkOutTimeString = this.getTimeStringFromDate(this.attendanceEntry.outTime.toDate(), true, false);
        }
      } else {
        this.checkInDone = false;
        this.checkOutDone = false;
      }
    });
  }

  preValidateWebPunch(): void {
    //once check out, cannot check out again
    if (this.checkOutDone) {
      return;
    } else {
      //pre-check if( Employee Work From Home Enabled)
      //then check -> today in Movement Status
      this.subscribeIsWHFEnabled(this.userAttendanceEntryService.isWFHEnabled());
    }
  }

  protected subscribeIsWHFEnabled(result: Observable<HttpResponse<Boolean>>): void {
    result.subscribe(
      res => this.onSuccessOfIsWFHEnableRequest(res),
      () => this.onError()
    );
  }

  private onSuccessOfIsWFHEnableRequest(res: any): void {
    if (res.body && res.body === true) {
      //if WHF enabled, check employee has movement entry or leave in Today
      this.findConflictWithLeaveAndMovementEntry();
      //this.subscribeCheckMovementEntry(this.userMovementEntryService.anyMovementEntryConflict(todayToDateFormat, todayToDateFormat));
    } else {
      Swal.fire({
        icon: 'warning',
        text: 'Seems like you are doing physical office.',
        showConfirmButton: false,
        timer: SWAL_DEFAULT_TIMER,
      });
    }
  }

  findConflictWithLeaveAndMovementEntry(): void {
    const dateRange: IDateRangeDTO = { startDate: dayjs(), endDate: dayjs() };

    this.attendanceTimeSheetService.findApplicationsByDateRange(dateRange).subscribe((res: { body: any }) => {
      const attendanceTimeSheet: IAttendanceTimeSheet = res.body!;
      if (attendanceTimeSheet.hasPendingMovementEntry) {
        swalForWarningWithMessage('You are already in movement status.', 2000);
      } else if (attendanceTimeSheet.hasPendingLeaveApplication) {
        swalForWarningWithMessage('You have applied leave today.', 2000);
      } else if (attendanceTimeSheet.hasMovementStatus) {
        swalForWarningWithMessage('You are already in movement status.', 2000);
      } else if (attendanceTimeSheet.hasLeaveStatus) {
        swalForWarningWithMessage('You are already in leave today.', 2000);
      } else {
        if (this.checkInDone) {
          this.checkOut();
        } else {
          this.checkIn();
        }
      }
    });
  }

  private onError(): void {
    swalOnRequestError();
  }

  checkIn(): void {
    Swal.fire({
      text: 'Punch In ?',
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_DENY_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.userAttendanceEntryService.onlinePunch().subscribe((response: any) => {
          if (response) {
            this.loadStatus();
            this.loadAllAttendances();
          }
        });
      }
    });
  }

  checkOut(): void {
    Swal.fire({
      text: 'Punch Out ?',
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_DENY_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        this.userAttendanceEntryService.onlinePunch().subscribe((response: any) => {
          if (response) {
            this.loadStatus();
            this.loadAllAttendances();
          }
        });
      }
    });
  }

  public getTimeStringFromDecimalValue(workinghour: any): string {
    if (workinghour === null || workinghour === undefined) return '00:00';
    const h = Math.floor(workinghour);
    const m = Math.floor(workinghour * 100 - h * 100);
    const hour = h < 10 ? '0' + h : h + '';
    const minute = m < 10 ? '0' + m : m + '';
    return hour + ':' + minute;
  }

  public getUIFriendlyAttendanceStatus(status: any): string {
    return this.attendanceService.getUIFriendlyAttendanceStatusDashboard(status);
  }

  private getTimeStringFromDate(date: Date, containsDay = false, containsSeconds = false): string {
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();

    let timeString = hours % 12 === 0 ? '12' : hours % 12;
    timeString = timeString + ':' + (minutes < 10 ? '0' : '') + minutes;
    if (containsSeconds && !containsDay) {
      timeString += ':' + (seconds < 10 ? '0' : '') + seconds + ' ';
    } else if (!containsSeconds && containsDay) {
      const month = date.toLocaleString('default', { month: 'short' });
      timeString = date.getDate() + ' ' + month + ', ' + timeString + ' ';
    } else if (containsSeconds && containsDay) {
      timeString += ':' + (seconds < 10 ? '0' : '') + seconds + ' ';
      const month = date.toLocaleString('default', { month: 'short' });
      timeString = date.getDate() + ' ' + month + ', ' + timeString + ' ';
    } else {
      timeString += ' ';
    }
    if (hours > 11) {
      timeString += 'PM';
    } else {
      timeString += 'AM';
    }
    this.currentTimeString = timeString;

    return timeString;
  }

  private isLeapYear(year: number): boolean {
    return (year % 4 === 0 && year % 100 !== 0) || year % 400 === 0;
  }

  openApprovalsModal(): void {
    this.modalService.open(ApprovalsModalComponent, {
      centered: true,
    });
  }

  openAttendanceModal(): void {
    this.modalService.open(AttendancesModalComponent, {
      centered: true,
    });
  }

  openMyStuffsModal(): void {
    this.modalService.open(MyStuffsModalComponent, {
      centered: true,
    });
  }

  openNoticeDetailsModal(notice: IOfficeNotices): void {
    const modalRef = this.modalService.open(NoticeDetailsModalComponent, { centered: true });
    modalRef.componentInstance.notice = notice;
  }

  openOfferDetailsModal(offer: IOffer): void {
    const modalRef = this.modalService.open(OfferDetailsModalComponent, { centered: true });
    modalRef.componentInstance.offer = offer;
  }

  onClickTypeChange(type: string): void {
    this.selectedType = type;
    if (this.selectedType === 'offer' && this.recentOffers.length === 0) {
      this.offerService.recentList().subscribe(res => {
        this.recentOffers = res.body!;
      });
    }
  }

  openRequisitionModal(): void {
    this.modalService.open(RequisitionModalComponent, {
      centered: true,
    });
  }

  openMyStatementModal(): void {
    this.modalService.open(StatementDetailsModalComponent, {
      centered: true,
    });
  }

  openMyPaySlipModal(): void {
    this.modalService.open(PayslipDetailsModalComponent, {
      centered: true,
    });
  }

  openMyNomineeModal(): void {
    this.modalService.open(NomineeDetailsModalComponent, {
      centered: true,
    });
  }

  navigateToRRF(routeLink: string): void {
    this.recruitmentRequisitionFormService.canRaiseRRF().subscribe((res: HttpResponse<IRrfRaiseValidity>) => {
      if (res.body!.canRaiseRRFOnBehalf && res.body!.canRaiseRRFOwn) {
        this.openRequisitionModal();
      } else if (res.body!.canRaiseRRFOwn) {
        this.router.navigate([routeLink]);
      } else {
        Swal.fire({
          icon: SWAL_REJECTED_ICON,
          text: 'Oops! You cannot raise RRF',
          timer: 2500,
          showConfirmButton: false,
        });
      }
    });
  }

  private openNavigationModal(): void {
    //open approval modal, if session has key
    if (sessionStorage.getItem('keepApprovalModalOpen') && sessionStorage.getItem('keepApprovalModalOpen') === 'yes') {
      this.openApprovalsModal();
      sessionStorage.removeItem('keepApprovalModalOpen');
    }

    //open my stuff modal, if session has key
    if (sessionStorage.getItem('keepMyStuffModalOpen') && sessionStorage.getItem('keepMyStuffModalOpen') === 'yes') {
      this.openMyStuffsModal();
      sessionStorage.removeItem('keepMyStuffModalOpen');
    }

    //open pay slip modal, if session has key
    if (sessionStorage.getItem('keepPaySlipModalOpen') && sessionStorage.getItem('keepPaySlipModalOpen') === 'yes') {
      this.openMyPaySlipModal();
      sessionStorage.removeItem('keepPaySlipModalOpen');
    }

    //open statement modal, if session has key
    if (sessionStorage.getItem('keepStatementModalOpen') && sessionStorage.getItem('keepStatementModalOpen') === 'yes') {
      this.openMyStatementModal();
      sessionStorage.removeItem('keepStatementModalOpen');
    }

    //open pf statement modal, if session has key
    if (sessionStorage.getItem('keepPfStatementModalOpen') && sessionStorage.getItem('keepPfStatementModalOpen') === 'yes') {
      this.openMyStatementModal();
      sessionStorage.removeItem('keepPfStatementModalOpen');
    }

    //open tax statement modal, if session has key
    if (sessionStorage.getItem('keepTaxStatementModalOpen') && sessionStorage.getItem('keepTaxStatementModalOpen') === 'yes') {
      this.openMyStatementModal();
      sessionStorage.removeItem('keepTaxStatementModalOpen');
    }

    //open nominee modal, if session has key
    if (sessionStorage.getItem('keepNomineeModalOpen') && sessionStorage.getItem('keepNomineeModalOpen') === 'yes') {
      this.openMyNomineeModal();
      sessionStorage.removeItem('keepNomineeModalOpen');
    }

    //open attendances model, if session has key
    if (sessionStorage.getItem('keepAttendancesModalOpen') && sessionStorage.getItem('keepAttendancesModalOpen') === 'yes') {
      this.openAttendanceModal();
      sessionStorage.removeItem('keepAttendancesModalOpen');
    }
  }
}
