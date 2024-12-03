import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAitPayment } from '../ait-payment.model';

@Component({
  selector: 'jhi-ait-payment-detail',
  templateUrl: './ait-payment-detail.component.html',
})
export class AitPaymentDetailComponent implements OnInit {
  aitPayment: IAitPayment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ aitPayment }) => {
      this.aitPayment = aitPayment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
