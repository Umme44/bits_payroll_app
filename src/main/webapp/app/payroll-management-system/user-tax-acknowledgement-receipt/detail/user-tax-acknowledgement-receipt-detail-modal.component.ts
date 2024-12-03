import { Component, OnDestroy, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute } from '@angular/router';
import { AcknowledgementStatus } from '../../../shared/model/enumerations/acknowledgement-status.model';
import {ITaxAcknowledgementReceipt} from "../tax-acknowledgement-receipt.model";

@Component({
  selector: 'jhi-user-tax-acknowledgement-receipt-detail-modal',
  templateUrl: './user-tax-acknowledgement-receipt-detail-modal.component.html',
})
export class UserTaxAcknowledgementReceiptDetailModalComponent implements OnInit, OnDestroy {
  taxAcknowledgementReceipt!: ITaxAcknowledgementReceipt;

  constructor(protected activatedRoute: ActivatedRoute, protected activeModal: NgbActiveModal, private modalService: NgbModal) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {}

  previousState(): void {
    window.history.back();
  }

  closeModal(): void {
    this.activeModal.dismiss('Cross click');
  }

  getUserFriendlyUi(acknowledgementStatus: any): string {
    if (acknowledgementStatus === AcknowledgementStatus.SUBMITTED) {
      return 'Submitted';
    } else if (acknowledgementStatus === AcknowledgementStatus.RECEIVED) {
      return 'Received';
    } else {
      return '';
    }
  }
}
