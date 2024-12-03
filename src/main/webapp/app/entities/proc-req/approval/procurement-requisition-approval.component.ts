import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';
import { FormControl, Validators } from '@angular/forms';

import {
  swalOnApprovalConfirmation,
  swalOnApprovedSuccess,
  swalOnRejectConfirmation,
  swalOnRejectedSuccess,
  swalOnRequestErrorWithBackEndErrorTitle,
} from 'app/shared/swal-common/swal-common';
import { Status } from 'app/shared/model/enumerations/status.model';
import { IProcReqMaster } from '../../proc-req-master/proc-req-master.model';
import { ProcReqService } from '../service/proc-req.service';
@Component({
  selector: 'jhi-procurement-requisition-approval',
  templateUrl: './procurement-requisition-approval.component.html',
})
export class ProcurementRequisitionApprovalComponent implements OnInit, OnDestroy {
  listOfIds: number[] = [];

  procReqMasters: IProcReqMaster[] = [];
  procReqMastersFiltered: IProcReqMaster[] = [];

  status!: Status;

  rejectionReason = new FormControl('', [Validators.required]);
  isCTOApprovalIsRequired = new FormControl(null, [Validators.required]);
  isApprovingByDeptHead = false;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected procReqService: ProcReqService,
    protected modalService: NgbModal,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadPendingList();
  }

  loadPendingList(): void {
    this.procReqService.findAllPendingLM().subscribe(response => {
      this.procReqMastersFiltered = this.procReqMasters = response.body!;
      // if (this.procReqMasters.length > 0) {
      //   this.isApprovingByDeptHead = this.procReqMasters[0].nextRecommendationOrder === 1; // 1 means first approval, so dept head is approving now
      // }
    });
  }

  ngOnDestroy(): void {
    this.modalService.dismissAll();
  }

  selectAllCheckBox(event: any): void {
    // all select check all
    // if un check, then de select all
    const isChecked = event.target.checked;
    this.listOfIds = [];

    if (isChecked) {
      document.getElementsByName('individualCheckBox').forEach(value => {
        (value as HTMLInputElement).checked = true;
      });
      this.procReqMastersFiltered.forEach(value => {
        this.listOfIds.push(value.id!);
      });
    } else {
      document.getElementsByName('individualCheckBox').forEach(value => {
        (value as HTMLInputElement).checked = false;
      });
    }
    this.refreshIsApprovingByDeptFlag();
  }

  selectIndividualCheckBox(event: any, idValue: number): void {
    const isChecked = event.target.checked;
    //const idValue = event.target.value;
    if (isChecked) {
      this.listOfIds.push(idValue);

      // only unique values
      this.listOfIds = this.listOfIds.filter((v, i, a) => {
        return a.indexOf(v) === i;
      });
    } else {
      // un-check all selector checkbox
      document.getElementsByName('selectAllCheckBox').forEach(value => {
        (value as HTMLInputElement).checked = false;
      });

      // remove item from list
      const index = this.listOfIds.indexOf(idValue);
      if (index > -1) {
        // only splice array when item is found
        this.listOfIds.splice(index, 1); // 2nd parameter defines remove one item only
      }
    }
    this.refreshIsApprovingByDeptFlag();
  }

  refreshIsApprovingByDeptFlag(): void {
    this.isApprovingByDeptHead =
      this.procReqMasters.filter(({ id, nextRecommendationOrder }) => this.listOfIds.includes(id!) && nextRecommendationOrder === 1)
        .length > 0; //1 means first approval, so dept head is approving now

    // this.isApprovingByDeptHead =
    //   this.procReqMasters.filter(item => {
    //     return this.listOfIds.indexOf(item.id!) !== -1 && item.nextRecommendationOrder === 1; //1 means first approval, so dept head is approving now
    //   }).length > 0;
  }

  resetAllSelectorCheckbox(): void {
    this.listOfIds = [];
    // un-check all selector checkbox
    document.getElementsByName('selectAllCheckBox').forEach(value => {
      (value as HTMLInputElement).checked = false;
    });
  }

  approveSelected(): void {
    swalOnApprovalConfirmation().then(result => {
      if (result.isConfirmed) {
        this.status = Status.APPROVED;
        if (this.isApprovingByDeptHead) {
          this.subscribeToSaveResponse(this.procReqService.approvedByDepartmentHead(this.isCTOApprovalIsRequired.value, this.listOfIds));
        } else {
          this.subscribeToSaveResponse(this.procReqService.approveSelected(this.listOfIds));
        }
      }
    });
  }

  rejectSelected(procReqMasterId: number): void {
    swalOnRejectConfirmation().then(result => {
      if (result.isConfirmed) {
        this.status = Status.NOT_APPROVED;
        this.subscribeToSaveResponse(
          this.procReqService.rejectSelected({
            id: procReqMasterId,
            rejectionReason: this.rejectionReason.value,
          })
        );
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<boolean>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      res => this.onSaveError(res)
    );
  }

  private onSaveError(res: any): void {
    swalOnRequestErrorWithBackEndErrorTitle(res.error.title);
  }

  private onSaveSuccess(): void {
    if (this.status === Status.APPROVED) {
      swalOnApprovedSuccess();
    } else {
      swalOnRejectedSuccess();
    }
    this.resetAllSelectorCheckbox();
    this.loadPendingList();
  }

  getProfilePicture(pin: String): String {
    return SERVER_API_URL + '/files/get-employees-image/' + pin;
  }

  search(searchText: any): void {
    this.listOfIds = [];

    // de-select all
    document.getElementsByName('selectAllCheckBox').forEach(x => {
      (x as HTMLInputElement).checked = false;
    });
    document.getElementsByName('individualCheckBox').forEach(value => {
      (value as HTMLInputElement).checked = false;
    });

    this.procReqMastersFiltered = this.procReqMasters.filter(procReqMaster => {
      // search by --> pin, name
      const regexObj = new RegExp(searchText, 'i');
      if (regexObj.test(procReqMaster.requestedByPIN!) || regexObj.test(procReqMaster.requestedByFullName!)) {
        return procReqMaster;
      }
      return null;
    });
  }

  // openView(flexScheduleApplication: IFlexScheduleApplication): void {
  //   const modalRef = this.modalService.open(FlexScheduleApplicationDetailApprovalModalComponent, { size: 'lg', backdrop: 'static' });
  //   modalRef.componentInstance.flexScheduleApplication = flexScheduleApplication;
  //   modalRef.componentInstance.modalType = 'approval';
  // }

  textSlicing(text: string): String {
    return text.slice(0, 25) + '...';
  }

  openRejectionModal(procReqMasterId: number, content: any): void {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title', centered: true }).result.then(
      result => {
        this.status = Status.NOT_APPROVED;
        this.subscribeToSaveResponse(
          this.procReqService.rejectSelected({
            id: procReqMasterId,
            rejectionReason: this.rejectionReason.value,
          })
        );
      },
      () => {}
    );
  }

  openDeptHeadApprovalModal(content: any): void {
    this.modalService.open(content, { ariaLabelledBy: 'modal-basic-title', centered: true }).result.then(
      result => {
        this.status = Status.APPROVED;
        this.subscribeToSaveResponse(this.procReqService.approvedByDepartmentHead(this.isCTOApprovalIsRequired.value, this.listOfIds));
      },
      () => {}
    );
  }

  navigatePrintView(id: number): void {
    sessionStorage.setItem('prfPrintViewMidHeader', '/procurement-requisition-user/approval');
    this.router.navigate(['/procurement-requisition-user', id, 'print-view']);
  }
}
