import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Observable, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ManualAttendanceEntryCommonService } from '../manual-attendance-entry-common.service';
import { ActivatedRoute } from '@angular/router';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import { AttendanceApprovalService } from './attendance-approval.service';
import Swal from 'sweetalert2';
import { FormControl } from '@angular/forms';
import {
  SWAL_APPROVE_CONFIRMATION,
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED,
  SWAL_APPROVED_ICON,
  SWAL_CANCEL_BTN_TEXT,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_REJECT_CONFIRMATION,
  SWAL_REJECTED,
  SWAL_REJECTED_ICON,
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
} from '../../../shared/swal-common/swal.properties.constant';
import { swalChangesNotSaved } from '../../../shared/swal-common/swal-common';
import { IManualAttendanceEntry } from '../../../shared/legacy/legacy-model/manual-attendance-entry.model';
import { EventManager } from '../../../core/util/event-manager.service';
import { ParseLinks } from '../../../core/util/parse-links.service';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../config/color.code.constant';
import {CustomValidator} from "../../../validators/custom-validator";

@Component({
  selector: 'jhi-attendance-approval',
  templateUrl: './attendance-approval.component.html',
})
export class AttendanceApprovalComponent implements OnInit, OnDestroy {
  manualAttendanceEntries: IManualAttendanceEntry[];
  manualAttendanceEntriesFiltered?: IManualAttendanceEntry[];

  eventSubscriber?: Subscription;
  links: any;

  pageType!: String;

  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchTxt = new FormControl('');
  isApproving = false;

  constructor(
    protected attendanceApprovalService: AttendanceApprovalService,
    protected manualAttendanceEntryService: ManualAttendanceEntryCommonService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected parseLinks: ParseLinks,
    private activatedRoute: ActivatedRoute
  ) {
    this.manualAttendanceEntries = [];
  }

  loadAll(): void {
    if (this.pageType === 'lm') {
      this.attendanceApprovalService.getAllPendingLm().subscribe((res: HttpResponse<IManualAttendanceEntry[]>) => {
        if (res.body) {
          this.manualAttendanceEntries = res.body;
          this.manualAttendanceEntriesFiltered = this.manualAttendanceEntries;
        } else this.manualAttendanceEntries = [];
      });
    } else {
      this.attendanceApprovalService.getAllPendingHr().subscribe((res: HttpResponse<IManualAttendanceEntry[]>) => {
        if (res.body) {
          this.manualAttendanceEntries = res.body;
          this.manualAttendanceEntriesFiltered = this.manualAttendanceEntries;
        } else {
          this.manualAttendanceEntries = [];
        }
      });
    }
  }

  search(): void {
    this.manualAttendanceEntriesFiltered = this.manualAttendanceEntries?.filter(manualAttendanceEntry => {
      manualAttendanceEntry.isChecked = false;
      this.allSelector = false;
      this.approvalDTO.listOfIds = [];
      if (manualAttendanceEntry?.pin?.toString().match(this.searchTxt.value)) {
        return manualAttendanceEntry;
      }
      return null;
    });
  }

  onSearchTextChangeV2(searchText: any): void {
    if(this.showInvalidSearchTextError(searchText)) {
      this.manualAttendanceEntriesFiltered = this.manualAttendanceEntries?.filter(manualAttendanceEntry => {
        manualAttendanceEntry.isChecked = false;
        this.allSelector = false;
        this.approvalDTO.listOfIds = [];
        // search by --> pin, name
        const regexObj = new RegExp(searchText, 'i');
        if (regexObj.test(manualAttendanceEntry.pin!) || new RegExp(searchText, 'i').test(manualAttendanceEntry.fullName!)) {
          return manualAttendanceEntry;
        }
        return null;
      });
    }
    else this.isSearchTextInvalid = true
  }

  isSearchTextInvalid = false;
  showInvalidSearchTextError(searchText: string): boolean{
    this.isSearchTextInvalid = !CustomValidator.NATURAL_TEXT_PATTERN.test(searchText)
    return !this.isSearchTextInvalid
  }

  reset(): void {
    this.manualAttendanceEntries = [];
    this.loadAll();
  }

  ngOnInit(): void {
    this.pageType = this.activatedRoute.snapshot.url[0].path;
    this.loadAll();
    this.registerChangeInManualAttendanceEntries();
    this.approvalDTO.listOfIds = [];
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IManualAttendanceEntry): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInManualAttendanceEntries(): void {
    this.eventSubscriber = this.eventManager.subscribe('manualAttendanceEntryListModification', () => this.reset());
  }

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.manualAttendanceEntriesFiltered !== undefined) {
      this.manualAttendanceEntriesFiltered = this.manualAttendanceEntriesFiltered.map(d => {
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
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    for (let i = 0; i < this.manualAttendanceEntriesFiltered!.length; i++) {
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
      if (this.manualAttendanceEntriesFiltered![i].isChecked === true) {
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
        this.selectedIdSet.add(this.manualAttendanceEntriesFiltered![i].id);
      }
    }
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => value as number);
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
        if (this.pageType === 'lm') {
          this.subscribeToSaveResponse(this.attendanceApprovalService.approveSelectedLm(this.approvalDTO));
        } else {
          this.subscribeToSaveResponse(this.attendanceApprovalService.approveSelectedHr(this.approvalDTO));
        }
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
      this.isApproving = false;
      if (result.isConfirmed) {
        if (this.pageType === 'lm') {
          this.subscribeToSaveResponse(this.attendanceApprovalService.denySelectedLm(this.approvalDTO));
        } else {
          this.subscribeToSaveResponse(this.attendanceApprovalService.denySelectedHr(this.approvalDTO));
        }
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

  protected onSaveSuccess(): void {
    if (this.isApproving) {
      Swal.fire({
        icon: SWAL_APPROVED_ICON,
        text: SWAL_APPROVED,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: SWAL_REJECTED,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    }
    this.clearAllChecks();
    this.loadAll();
    this.isApproving = false;
  }

  protected onSaveError(): void {
    Swal.fire({
      icon: SWAL_RESPONSE_ERROR_ICON,
      text: SWAL_RESPONSE_ERROR_TEXT,
    });
    this.clearAllChecks();
    this.loadAll();
    this.isApproving = false;
  }

  clearAllChecks(): void {
    this.allSelector = false;
    this.manualAttendanceEntries?.map(manualAttendanceEntry => {
      manualAttendanceEntry.isChecked = false;
    });
    this.searchTxt.setValue('');
    this.manualAttendanceEntriesFiltered = [];
    this.approvalDTO.listOfIds = [];
  }

  approveAll(): void {
    Swal.fire({
      text: SWAL_APPROVE_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        if (this.pageType === 'lm') {
          this.attendanceApprovalService.approveAllLm().subscribe((res: HttpResponse<boolean>) => {
            if (res.body === true) {
              Swal.fire('All pending Attendance applications Approved!', '', 'success');
              this.loadAll();
            } else {
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
              });
            }
          });
        } else {
          this.attendanceApprovalService.approveAllHr().subscribe((res: HttpResponse<boolean>) => {
            if (res.body === true) {
              Swal.fire('All pending Attendance applications Approved!', '', 'success');
              this.loadAll();
            } else {
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
              });
            }
          });
        }
      } else if (result.isDenied) {
        Swal.fire('Changes are not saved', '', 'info');
      }
    });
  }

  denyAll(): void {
    Swal.fire({
      text: SWAL_REJECT_CONFIRMATION,
      showCancelButton: true,
      confirmButtonColor: PRIMARY_COLOR,
      cancelButtonColor: DANGER_COLOR,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      cancelButtonText: SWAL_CANCEL_BTN_TEXT,
    }).then(result => {
      if (result.isConfirmed) {
        if (this.pageType === 'lm') {
          this.attendanceApprovalService.denyAllLm().subscribe((res: HttpResponse<boolean>) => {
            if (res.body === true) {
              Swal.fire('Selected Attendance applications Rejected!', '', 'success');
              this.loadAll();
            } else {
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
              });
            }
          });
        } else {
          this.attendanceApprovalService.denyAllHr().subscribe((res: HttpResponse<boolean>) => {
            if (res.body === true) {
              Swal.fire('Selected Attendance applications Rejected!', '', 'success');
              this.loadAll();
            } else {
              Swal.fire({
                icon: 'error',
                title: 'Oops...',
                text: 'Something went wrong!',
              });
            }
          });
        }
      } else if (result.isDenied) {
        Swal.fire('Changes are not saved', '', 'info');
      }
    });
  }

  textSlicing(note: any): string {
    return note.slice(0, 30) + '...';
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    this.allSelector = false;
    this.manualAttendanceEntriesFiltered?.forEach(data => {
      data.isChecked = false;
    });
  }
}
