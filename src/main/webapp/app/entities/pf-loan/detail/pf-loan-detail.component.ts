import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPfLoan } from '../pf-loan.model';

@Component({
  selector: 'jhi-pf-loan-detail',
  templateUrl: './pf-loan-detail.component.html',
})
export class PfLoanDetailComponent implements OnInit {
  pfLoan: IPfLoan | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoan }) => {
      this.pfLoan = pfLoan;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
