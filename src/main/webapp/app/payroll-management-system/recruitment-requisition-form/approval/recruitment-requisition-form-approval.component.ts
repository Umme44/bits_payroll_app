import { Component, HostListener, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';
import { ApprovalDTO } from 'app/shared/model/approval-dto.model';
import Swal from 'sweetalert2';
import { DANGER_COLOR, PRIMARY_COLOR } from 'app/shared/constants/color.code.constant';
import { FormControl } from '@angular/forms';
import {
  SWAL_APPROVE_CONFIRMATION,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_REJECT_CONFIRMATION,
} from 'app/shared/swal-common/swal.properties.constant';
import { swalChangesNotSaved, swalOnApprovedSuccess, swalOnRejectedSuccess, swalOnRequestError } from 'app/shared/swal-common/swal-common';
import { RRFApprovalService } from './recruitment-requisition-form-approval.service';
import { IRecruitmentRequisitionForm } from '../../../shared/legacy/legacy-model/recruitment-requisition-form.model';
import {
  IRecruitmentRequisitionBudget
} from '../../../entities/recruitment-requisition-budget/recruitment-requisition-budget.model';
import {
  RecruitmentRequisitionBudgetService
} from '../../../entities/recruitment-requisition-budget/service/recruitment-requisition-budget.service';
import { EventManager } from '../../../core/util/event-manager.service';

@Component({
  selector: 'jhi-rrf-approval',
  templateUrl: './recruitment-requisition-form-approval.component.html',
})
export class RecruitmentRequisitionFormApprovalComponent implements OnInit, OnDestroy {
  recruitmentRequisitionForms?: IRecruitmentRequisitionForm[];
  recruitmentRequisitionFormsFiltered?: IRecruitmentRequisitionForm[];
  recruitmentRequisitionBudgets?: IRecruitmentRequisitionBudget[] = [];

  eventSubscriber?: Subscription;
  allSelector = false;
  approvalDTO = new ApprovalDTO();
  selectedIdSet = new Set();
  searchTxt = new FormControl('');
  isApproving = false; //false means, I am rejecting

  constructor(
    protected rrfApprovalService: RRFApprovalService,
    protected eventManager: EventManager,
    protected modalService: NgbModal,
    protected router: Router,
    protected recruitmentRequisitionBudgetService: RecruitmentRequisitionBudgetService
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
    this.rrfApprovalService.getAllPending().subscribe((res: HttpResponse<IRecruitmentRequisitionForm[]>) => {
      this.recruitmentRequisitionForms = res.body || [];
      this.recruitmentRequisitionFormsFiltered = this.recruitmentRequisitionForms;
    });
    this.recruitmentRequisitionBudgetService.findByLoggedInUserId().subscribe((res: HttpResponse<IRecruitmentRequisitionBudget[]>) => {
      if (res.body !== null) {
        this.recruitmentRequisitionBudgets = res.body.filter(item => item.year === new Date().getFullYear());
      } else this.recruitmentRequisitionBudgets = [];
    });
  }

  onSearchTextChangeV2(searchText: any): void {
    this.allSelector = false;
    this.approvalDTO.listOfIds = [];

    this.recruitmentRequisitionFormsFiltered = this.recruitmentRequisitionForms!.filter(rrf => {
      rrf.isChecked = false;

      // search by --> pin, name
      const regexObj = new RegExp(searchText, 'i');
      if (regexObj.test(rrf.requesterPin!) || new RegExp(searchText, 'i').test(rrf.requesterFullName!)) {
        return rrf;
      }
      return null;
    });
  }

  trackId(index: number, item: IRecruitmentRequisitionForm): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInLeaveApplications(): void {
    this.eventSubscriber = this.eventManager.subscribe('leaveApplicationListModification', () => this.loadAll());
  }

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.recruitmentRequisitionFormsFiltered !== undefined) {
      this.recruitmentRequisitionFormsFiltered = this.recruitmentRequisitionFormsFiltered.map(d => {
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
    for (let i = 0; i < this.recruitmentRequisitionForms!.length; i++) {
      if (this.recruitmentRequisitionFormsFiltered![i].isChecked === true) {
        this.selectedIdSet.add(this.recruitmentRequisitionFormsFiltered![i].id);
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
        this.subscribeToSaveResponse(this.rrfApprovalService.approveSelected(this.approvalDTO));
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
        this.subscribeToSaveResponse(this.rrfApprovalService.denySelected(this.approvalDTO));
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
    this.recruitmentRequisitionForms?.map(rrf => {
      rrf.isChecked = false;
    });
    this.searchTxt.setValue('');
    this.recruitmentRequisitionFormsFiltered = [];
    this.approvalDTO.listOfIds = [];
  }

  navigatePrintView(id: number): void {
    sessionStorage.setItem('rrfPrintViewMidHeader', '/recruitment-requisition-form/user/approval');
    this.router.navigate(['/recruitment-requisition-form/user/approval', id, 'view']);
  }

  @HostListener('document:keyup.escape', ['$event']) onKeydownHandler(event: KeyboardEvent): void {
    this.allSelector = false;
    this.recruitmentRequisitionFormsFiltered?.forEach(data => {
      data.isChecked = false;
    });
  }
}
