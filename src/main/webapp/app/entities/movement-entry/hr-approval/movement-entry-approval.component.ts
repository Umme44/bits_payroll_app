import { Component, HostListener, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import {
  SWAL_APPROVE_CONFIRMATION,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENIED_BTN_TIMER,
  SWAL_DENY_BTN_TEXT,
  SWAL_DENY_BUTTON_SELECT,
  SWAL_DENY_BUTTON_SELECT_ICON,
  SWAL_REJECT_CONFIRMATION,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../shared/constants/color.code.constant';
import {
  swalChangesNotSaved,
  swalOnApprovedSuccess,
  swalOnRejectedSuccess,
  swalOnRequestError,
} from '../../../shared/swal-common/swal-common';
import { ApprovalDTO } from '../../../shared/model/approval-dto.model';
import { MovementEntryApprovalService } from '../service/movement-entry-approval-service';
import { IMovementEntry } from '../movement-entry.model';

@Component({
  selector: 'jhi-movement-entry-approval',
  templateUrl: './movement-entry-approval.component.html',
})
export class MovementEntryApprovalComponent implements OnInit {
  movementEntries: IMovementEntry[] = [];
  movementEntriesFiltered: IMovementEntry[] = [];

  pageType!: string;

  toggleCheck = false;

  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchText = new FormControl('');
  isApproving = false;

  constructor(private activatedRoute: ActivatedRoute, private movementEntryApprovalService: MovementEntryApprovalService) {}

  ngOnInit(): void {
    this.pageType = this.activatedRoute.snapshot.url[1].path;
    this.loadAll();
    this.approvalDTO.listOfIds = [];
  }

  protected loadAll(): void {
    if (this.pageType === 'lm') {
      this.movementEntryApprovalService.getAllPendingLM().subscribe(res => {
        this.movementEntriesFiltered = this.movementEntries = res.body!;
      });
    } else {
      this.movementEntryApprovalService.getAllPendingHR().subscribe(res => {
        this.movementEntriesFiltered = this.movementEntries = res.body!;
      });
    }
  }

  search(searchText: any): void {
    // d-select all
    this.allSelector = false;
    this.searchText.setValue(searchText);

    this.movementEntriesFiltered = this.movementEntries.filter(movementEntry => {
      movementEntry.isChecked = false;

      // search by --> pin, name
      const regexObj = new RegExp(searchText, 'i');
      if (regexObj.test(movementEntry.pin!) || regexObj.test(movementEntry.employeeName!)) {
        return movementEntry;
      }
      return null;
    });
  }

  clearAllChecks(): void {
    this.allSelector = false;
    this.movementEntriesFiltered.map(movementEntry => {
      movementEntry.isChecked = false;
    });
    this.searchText.setValue('');
    this.approvalDTO.listOfIds = [];
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
          this.subscribeToSaveResponse(this.movementEntryApprovalService.approveSelectedLM(this.approvalDTO));
        } else {
          this.subscribeToSaveResponse(this.movementEntryApprovalService.approveSelectedHR(this.approvalDTO));
        }
      } else if (result.isDenied) {
        swalChangesNotSaved();
      }
    });
  }

  rejectSelected(): void {
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
        if (this.pageType === 'lm') {
          this.subscribeToSaveResponse(this.movementEntryApprovalService.rejectSelectedLM(this.approvalDTO));
        } else {
          this.subscribeToSaveResponse(this.movementEntryApprovalService.rejectSelectedHR(this.approvalDTO));
        }
      } else if (result.isDenied) {
        Swal.fire({
          icon: SWAL_DENY_BUTTON_SELECT_ICON,
          text: SWAL_DENY_BUTTON_SELECT,
          timer: SWAL_DENIED_BTN_TIMER,
          showConfirmButton: false,
        });
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

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;

    if (this.movementEntriesFiltered) {
      this.movementEntriesFiltered = this.movementEntriesFiltered.map(movementEntry => {
        if (movementEntry.id === id) {
          movementEntry.isChecked = isChecked;
          this.allSelector = false;
          return movementEntry;
        }
        if (id === -1) {
          movementEntry.isChecked = this.allSelector;
          return movementEntry;
        }
        return movementEntry;
      });
    }

    // clear previous set
    this.selectedIdSet.clear();
    this.movementEntriesFiltered.forEach(movementEntry => {
      if (movementEntry.isChecked) {
        this.selectedIdSet.add(movementEntry.id);
      }
    });
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => value as number);
  }

  onSelect(id: any): void {
    if (this.toggleCheck === true) {
      this.toggleCheck = false;
    } else {
      this.toggleCheck = true;
    }
    const isChecked = this.toggleCheck;
    if (this.movementEntriesFiltered) {
      this.movementEntriesFiltered = this.movementEntriesFiltered.map(movementEntry => {
        if (movementEntry.id === id) {
          movementEntry.isChecked = isChecked;
          this.allSelector = false;
          return movementEntry;
        }
        if (id === -1) {
          movementEntry.isChecked = this.allSelector;
          return movementEntry;
        }
        return movementEntry;
      });
    }

    // clear previous set
    this.selectedIdSet.clear();
    this.movementEntriesFiltered.forEach(movementEntry => {
      if (movementEntry.isChecked) {
        this.selectedIdSet.add(movementEntry.id);
      }
    });
    this.approvalDTO.listOfIds = Array.from(this.selectedIdSet.values()).map(value => value as number);
  }

  trackId(index: number, item: IMovementEntry): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  textSlicing(note: any): string {
    return note.slice(0, 30) + '...';
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    this.allSelector = false;
    this.movementEntriesFiltered.forEach(data => {
      data.isChecked = false;
    });
  }
}
