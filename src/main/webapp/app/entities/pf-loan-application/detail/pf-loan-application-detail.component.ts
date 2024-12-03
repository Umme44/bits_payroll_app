import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPfLoanApplication } from '../pf-loan-application.model';

@Component({
  selector: 'jhi-pf-loan-application-detail',
  templateUrl: './pf-loan-application-detail.component.html',
})
export class PfLoanApplicationDetailComponent implements OnInit {
  pfLoanApplication: IPfLoanApplication | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoanApplication }) => {
      this.pfLoanApplication = pfLoanApplication;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
