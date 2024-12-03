import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormControl } from '@angular/forms';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';
import Swal from 'sweetalert2';
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_CHANGED,
  SWAL_CONFIRM_BTN_TEXT,
  SWAL_DENY_BTN_TEXT,
  SWAL_RESPONSE_ERROR_ICON,
  SWAL_RESPONSE_ERROR_TEXT,
  SWAL_RESPONSE_ERROR_TITLE,
  SWAL_SURE,
} from '../../../shared/swal-common/swal.properties.constant';
import { DANGER_COLOR, PRIMARY_COLOR } from '../../../shared/constants/color.code.constant';
import { Observable } from 'rxjs';
import { IFestivalBonusDetails } from '../../festival-bonus-details/festival-bonus-details.model';
import { FestivalBonusDetailsService } from '../../festival-bonus-details/service/festival-bonus-details.service';
import { HoldFbDisbursementService } from '../service/hold-fb-disbursement.service';
import { IHoldFbDisbursement } from '../hold-fb-disbursement.model';
import { DATE_FORMAT } from '../../../config/input.constants';
import { IHoldFbDisbursementApproval, HoldFbDisbursementApproval } from '../hold-fb-disbursement-approval.model';

@Component({
  selector: 'jhi-hold-festival-bonus-list',
  templateUrl: './hold-festival-bonus-list.component.html',
  styleUrls: ['./hold-festival-bonus-list.component.scss'],
})
export class HoldFestivalBonusListComponent implements OnInit {
  searchText: String = '';
  date = new FormControl(null);
  remark = new FormControl(null);
  selectedBonusDetail!: IFestivalBonusDetails;
  holdBonusList: IFestivalBonusDetails[] = [];
  selectedIdSet = new Set();
  allSelector = false;
  listOfId: number[] = [];

  constructor(
    protected fbDetailService: FestivalBonusDetailsService,
    protected modalService: NgbModal,
    protected holdFbDisbursementService: HoldFbDisbursementService
  ) {
    this.holdBonusList = [];
  }

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.fbDetailService
      .festivalBonusHoldList({
        searchText: this.searchText,
      })
      .subscribe((res: HttpResponse<IFestivalBonusDetails[]>) => (res.body ? (this.holdBonusList = res.body) : (this.holdBonusList = [])));
  }

  disburseFBModal(content: any): void {
    this.modalService.open(content, { centered: true, ariaLabelledBy: 'modal-basic-title' }).result.then(
      result => {
        this.swalConfirmation();
      },
      reason => {
        this.date.reset();
        this.remark.reset();
      }
    );
  }

  reset(): void {
    this.loadAll();
  }

  swalConfirmation(): void {
    Swal.fire({
      text: SWAL_SURE,
      showDenyButton: true,
      confirmButtonText: SWAL_CONFIRM_BTN_TEXT,
      confirmButtonColor: PRIMARY_COLOR,
      denyButtonText: SWAL_DENY_BTN_TEXT,
      denyButtonColor: DANGER_COLOR,
    }).then(result => {
      if (result.isConfirmed) {
        this.subscribeToSaveResponse(this.holdFbDisbursementService.createOld(this.createFromForm()));
      }
    });
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHoldFbDisbursement>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected createFromForm(): IHoldFbDisbursementApproval {
    return {
      ...new HoldFbDisbursementApproval(),
      remarks: this.remark.value,
      listOfId: this.listOfId,
      disbursedAt: this.date.value ? dayjs(this.date.value, DATE_FORMAT) : undefined,
    };
  }

  protected onSaveSuccess(): void {
    Swal.fire({
      icon: SWAL_APPROVED_ICON,
      text: 'Disbursed',
      timer: SWAL_APPROVE_REJECT_TIMER,
      showConfirmButton: false,
    });
    this.date.reset();
    this.remark.reset();
    this.loadAll();
    this.listOfId = [];
  }

  protected onSaveError(): void {
    Swal.fire({
      icon: SWAL_RESPONSE_ERROR_ICON,
      title: SWAL_RESPONSE_ERROR_TITLE,
      text: SWAL_RESPONSE_ERROR_TEXT,
    });
    this.date.reset();
    this.remark.reset();
    this.loadAll();
  }

  search($event: any): void {
    this.searchText = $event;
    this.reset();
  }

  onChange($event: any): void {
    const id = Number($event.target.value);
    const isChecked = $event.target.checked;
    if (this.holdBonusList !== undefined) {
      this.holdBonusList = this.holdBonusList.map(d => {
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
    for (let i = 0; i < this.holdBonusList!.length; i++) {
      // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
      if (this.holdBonusList![i].isChecked === true) {
        // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
        this.selectedIdSet.add(this.holdBonusList![i].id);
      }
    }
    this.listOfId = Array.from(this.selectedIdSet.values()).map(value => value as number);
  }
}
