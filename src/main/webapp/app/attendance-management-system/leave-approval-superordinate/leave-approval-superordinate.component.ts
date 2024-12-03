import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { EventManager } from 'app/core/util/event-manager.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ApprovalDTO } from 'app/shared/model/approval-dto.model';
import { LeaveApprovalLmService } from 'app/attendance-management-system/leave-approval-superordinate/leave-approval-superordinate.service';
import Swal from 'sweetalert2';
import { FormControl } from '@angular/forms';
import {
  SWAL_APPROVE_CONFIRMATION,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_REJECT_CONFIRMATION,
} from 'app/shared/swal-common/swal.properties.constant';
import { swalChangesNotSaved, swalOnApprovedSuccess, swalOnRejectedSuccess, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../config/color.code.constant';
import { ILeaveApplication, LeaveApplication } from '../../shared/legacy/legacy-model/leave-application.model';
import { LeaveApplicationDeleteDialogComponent } from '../../entities/leave-application/delete/leave-application-delete-dialog.component';
import { LeaveType } from 'app/shared/model/enumerations/leave-type.model';
import { ILeaveBalanceEndUserView } from 'app/shared/legacy/legacy-model/leave-balance-end-user-view.model';
import { LeaveSummaryEndUserViewService } from '../leave-summary-end-user-view/leave-summary-end-user-view.component.service';
import {CustomValidator} from "../../validators/custom-validator";

@Component({
  selector: 'jhi-leave-approval-superordinate',
  templateUrl: './leave-approval-superordinate.component.html',
  styleUrls: ['leave-approval-superordinate.component.scss'],
})
export class LeaveApprovalSuperordinateComponent implements OnInit, OnDestroy {
  leaveApplications?: ILeaveApplication[];
  leaveApplicationsFiltered?: ILeaveApplication[];

  individualLeaveBalance: ILeaveBalanceEndUserView[] = [];

  eventSubscriber?: Subscription;
  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchTxt = new FormControl('');
  isApproving = false; //false means, I am rejecting

  selectedLeaveApplication?: ILeaveApplication;
  selectedLeaveType?: LeaveType;

  constructor(
    protected leaveApprovalLmService: LeaveApprovalLmService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected leaveSummaryEndUserViewService: LeaveSummaryEndUserViewService
  ) {}

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInLeaveApplications();
    this.approvalDTO.listOfIds = [];
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  loadAll(): void {
    this.leaveApprovalLmService.getAllPending().subscribe((res: HttpResponse<ILeaveApplication[]>) => {
      this.leaveApplications = res.body || [];
      this.leaveApplicationsFiltered = this.leaveApplications;
    });
  }

  search(searchText: string): void {
    if(this.showInvalidSearchTextError(searchText)){
      this.allSelector = false;
      this.approvalDTO.listOfIds = [];

      this.leaveApplicationsFiltered = this.leaveApplications?.filter(leaveApplication => {
        leaveApplication.isChecked = false;

        // search by --> pin, name
        const regexObj = new RegExp(searchText, 'i');
        if (regexObj.test(leaveApplication.pin!) || regexObj.test(leaveApplication.fullName!)) {
          return leaveApplication;
        }
        return null;
      });
    }
    else this.isSearchTextInvalid=true;
  }
  isSearchTextInvalid = false;
  showInvalidSearchTextError(searchText: string): boolean{
    this.isSearchTextInvalid = !CustomValidator.NATURAL_TEXT_PATTERN.test(searchText)
    return !this.isSearchTextInvalid
  }

  trackId(index: number, item: ILeaveApplication): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLeaveApplications(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveApplicationListModification', () => this.loadAll());
  }

  delete(leaveApplication: ILeaveApplication): void {
    const modalRef = this.modalService.open(LeaveApplicationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.leaveApplication = leaveApplication;
  }

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.leaveApplicationsFiltered !== undefined) {
      this.leaveApplicationsFiltered = this.leaveApplicationsFiltered.map(d => {
        if (d.id === id) {
          d.isChecked = isChecked;
          this.allSelector = false;
          return d;
        }
        if (id === -1) {
          d.isChecked = this.allSelector;
          return d;
        }
        return d;
      });
    }

    // clear previous set
    this.selectedIdSet.clear();
    for (let i = 0; i < this.leaveApplications!.length; i++) {
      if (this.leaveApplicationsFiltered![i].isChecked === true) {
        this.selectedIdSet.add(this.leaveApplicationsFiltered![i].id);
      }
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => Number(value));
  }

  approveSelected(): void {
    Swal.fire({
      text: SWAL_APPROVE_CONFIRMATION,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.isApproving = true;
        this.subscribeToSaveResponse(this.leaveApprovalLmService.approveSelected(this.approvalDTO));
      } else if (result.isDenied) {
        swalChangesNotSaved();
      }
    });
  }

  denySelected(): void {
    Swal.fire({
      text: SWAL_REJECT_CONFIRMATION,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.isApproving = false;
        this.subscribeToSaveResponse(this.leaveApprovalLmService.denySelected(this.approvalDTO));
      } else if (result.isDenied) {
        swalChangesNotSaved();
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<boolean>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  private onSaveSuccess(): void {
    if (this.isApproving) {
      swalOnApprovedSuccess();
    } else {
      swalOnRejectedSuccess();
    }
    this.clearAllChecks();
    this.loadAll();
  }

  private onSaveError(): void {
    swalOnRequestError();
  }

  clearAllChecks(): void {
    this.allSelector = false;
    this.leaveApplications?.map(leaveApplication => {
      leaveApplication.isChecked = false;
    });
    this.searchTxt.setValue('');
    this.leaveApplicationsFiltered = [];
    this.approvalDTO.listOfIds = [];
  }

  textSlicing(note: any): string {
    return note.slice(0, 30) + '...';
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    this.allSelector = false;
    this.leaveApplications?.forEach(data => {
      data.isChecked = false;
    });
  }

  showApproveModal(leaveApplication: LeaveApplication, myModal: any): void {
    this.selectedLeaveApplication = leaveApplication;
    this.selectedLeaveType = leaveApplication.leaveType;
    this.leaveSummaryEndUserViewService
      .loadByYearAndSubordinateId(0, leaveApplication.employeeId as any)
      .subscribe((resp: HttpResponse<ILeaveBalanceEndUserView[]>) => {
        this.individualLeaveBalance = resp.body || [];
        this.modalService.open(myModal, { centered: true, size: 'xl' });
      });
  }

  confirmApproval(modal: any): void {
    this.leaveApprovalLmService.approveById(this.selectedLeaveApplication?.id).subscribe(
      res => {
        modal.close();
        this.isApproving = true;
        this.onSaveSuccess();
      },
      error => {
        modal.close();
        this.onSaveError();
      }
    );
  }

  confirmRejection(modal: any): void {
    this.leaveApprovalLmService.rejectById(this.selectedLeaveApplication?.id).subscribe(
      res => {
        modal.close();
        this.isApproving = false;
        this.onSaveSuccess();
      },
      error => {
        modal.close();
        this.onSaveError();
      }
    );
  }

  totalLeaveCalculation(openingBalance: any, leaveBalance: any): number {
    return openingBalance + leaveBalance;
  }

  totalLeaveAfterApprove(leaveBalance: any, selectedLeave: any): number {
    let total = leaveBalance.daysRemaining;
    if (leaveBalance.leaveType === selectedLeave.leaveType) {
      total = total - selectedLeave.durationInDay;
    }
    return total;
  }

  public truncate(msg: any): string {
    if (msg == null) return '';
    if (msg.length <= 100) return msg;
    const str = msg.slice(0, 100) + '...';
    return str;
  }
}
