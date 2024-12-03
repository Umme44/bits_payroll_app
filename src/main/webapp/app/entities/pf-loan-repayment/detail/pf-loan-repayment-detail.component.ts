import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPfLoanRepayment } from '../pf-loan-repayment.model';

@Component({
  selector: 'jhi-pf-loan-repayment-detail',
  templateUrl: './pf-loan-repayment-detail.component.html',
})
export class PfLoanRepaymentDetailComponent implements OnInit {
  pfLoanRepayment: IPfLoanRepayment | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoanRepayment }) => {
      this.pfLoanRepayment = pfLoanRepayment;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
