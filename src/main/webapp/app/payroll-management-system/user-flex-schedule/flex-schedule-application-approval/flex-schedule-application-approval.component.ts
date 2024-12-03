import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs';

import { FlexScheduleApplicationApprovalService } from './flex-schedule-application-approval.service';
import {
  swalOnApprovalConfirmation,
  swalOnApprovedSuccess,
  swalOnRejectConfirmation,
  swalOnRejectedSuccess,
  swalOnRequestErrorWithBackEndErrorTitle,
} from 'app/shared/swal-common/swal-common';
import { Status } from 'app/shared/model/enumerations/status.model';
import { IFlexScheduleApplication } from '../../../shared/legacy/legacy-model/flex-schedule-application.model';
import { FlexScheduleApplicationDetailApprovalModalComponent } from './flex-schedule-application-detail-approval-modal.component';
import {CustomValidator} from "../../../validators/custom-validator";

@Component({
  selector: 'jhi-flex-schedule-application-approval',
  templateUrl: './flex-schedule-application-approval.component.html',
})
export class FlexScheduleApplicationApprovalComponent implements OnInit, OnDestroy {
  pageType!: string;
  listOfIds: number[] = [];

  flexScheduleApplications: IFlexScheduleApplication[] = [];
  flexScheduleApplicationsFiltered: IFlexScheduleApplication[] = [];

  status!: Status;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected flexScheduleApplicationApprovalService: FlexScheduleApplicationApprovalService,
    protected modalService: NgbModal
  ) {
    this.pageType = activatedRoute.snapshot.params['pageType'];
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flexScheduleApplication }) => {
      this.flexScheduleApplicationsFiltered = this.flexScheduleApplications = flexScheduleApplication;
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
      document.getElementsByName('flexScheduleApplicationSelectBox').forEach(value => {
        (value as HTMLInputElement).checked = true;
      });
      this.flexScheduleApplicationsFiltered.forEach(value => {
        this.listOfIds.push(value.id!);
      });
    } else {
      document.getElementsByName('flexScheduleApplicationSelectBox').forEach(value => {
        (value as HTMLInputElement).checked = false;
      });
    }
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
      document.getElementsByName('flexScheduleApplicationSelectAllCheckBox').forEach(value => {
        (value as HTMLInputElement).checked = false;
      });

      // remove item from list
      const index = this.listOfIds.indexOf(idValue);
      if (index > -1) {
        // only splice array when item is found
        this.listOfIds.splice(index, 1); // 2nd parameter defines remove one item only
      }
    }
  }

  approveSelected(): void {
    swalOnApprovalConfirmation().then(result => {
      if (result.isConfirmed) {
        this.status = Status.APPROVED;
        if (this.pageType === 'lm') {
          this.subscribeToSaveResponse(this.flexScheduleApplicationApprovalService.approveSelectedLM(this.listOfIds));
        } else {
          this.subscribeToSaveResponse(this.flexScheduleApplicationApprovalService.approveSelectedHR(this.listOfIds));
        }
      }
    });
  }

  denySelected(): void {
    swalOnRejectConfirmation().then(result => {
      if (result.isConfirmed) {
        this.status = Status.NOT_APPROVED;
        if (this.pageType === 'lm') {
          this.subscribeToSaveResponse(this.flexScheduleApplicationApprovalService.denySelectedLM(this.listOfIds));
        } else {
          this.subscribeToSaveResponse(this.flexScheduleApplicationApprovalService.denySelectedHR(this.listOfIds));
        }
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

    if (this.pageType === 'hr') {
      this.flexScheduleApplicationApprovalService.findAllPendingHR().subscribe(res => {
        this.flexScheduleApplicationsFiltered = this.flexScheduleApplications = res.body!;
      });
    } else if (this.pageType === 'lm') {
      this.flexScheduleApplicationApprovalService.findAllPendingLM().subscribe(res => {
        this.flexScheduleApplicationsFiltered = this.flexScheduleApplications = res.body!;
      });
    }
  }

  getProfilePicture(pin: String): String {
    return SERVER_API_URL + '/files/get-employees-image/' + pin;
  }

  search(searchText: string): void {
    if(this.showInvalidSearchTextError(searchText)) {
      this.listOfIds = [];

      // de-select all
      document.getElementsByName('flexScheduleApplicationSelectAllCheckBox').forEach(x => {
        (x as HTMLInputElement).checked = false;
      });
      document.getElementsByName('flexScheduleApplicationSelectBox').forEach(value => {
        (value as HTMLInputElement).checked = false;
      });

      this.flexScheduleApplicationsFiltered = this.flexScheduleApplications.filter(flexScheduleApplication => {
        // search by --> pin, name
        const regexObj = new RegExp(searchText, 'i');
        if (regexObj.test(flexScheduleApplication.pin!) || regexObj.test(flexScheduleApplication.fullName!)) {
          return flexScheduleApplication;
        }
        return null;
      });
    }
    else this.isSearchTextInvalid = true;
  }

  isSearchTextInvalid = false;
  showInvalidSearchTextError(searchText: string): boolean{
    this.isSearchTextInvalid = !CustomValidator.NATURAL_TEXT_PATTERN.test(searchText)
    return !this.isSearchTextInvalid
  }

  openView(flexScheduleApplication: IFlexScheduleApplication): void {
    const modalRef = this.modalService.open(FlexScheduleApplicationDetailApprovalModalComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.flexScheduleApplication = flexScheduleApplication;
    modalRef.componentInstance.modalType = 'approval';
  }

  textSlicing(text: string): String {
    return text.slice(0, 25) + '...';
  }
}
