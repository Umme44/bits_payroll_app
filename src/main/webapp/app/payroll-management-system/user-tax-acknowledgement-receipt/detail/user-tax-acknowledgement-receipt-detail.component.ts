import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import {ITaxAcknowledgementReceipt} from "../tax-acknowledgement-receipt.model";
import {DataUtils} from "../../../core/util/data-util.service";
@Component({
  selector: 'jhi-tax-acknowledgement-receipt-detail',
  templateUrl: './user-tax-acknowledgement-receipt-detail.component.html',
})
export class UserTaxAcknowledgementReceiptDetailComponent implements OnInit {
  taxAcknowledgementReceipt: ITaxAcknowledgementReceipt | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ taxAcknowledgementReceipt }) => (this.taxAcknowledgementReceipt = taxAcknowledgementReceipt));
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(contentType = '', base64String: string): void {
    this.dataUtils.openFile(contentType, base64String);
  }

  previousState(): void {
    window.history.back();
  }
}
