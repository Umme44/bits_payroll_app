import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArrearPayment } from '../arrear-payment.model';

@Component({
  selector: 'jhi-arrear-payment-detail',
  templateUrl: './arrear-payment-detail.component.html',
})
export class ArrearPaymentDetailComponent implements OnInit {
  arrearPayment: IArrearPayment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arrearPayment }) => {
      this.arrearPayment = arrearPayment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
