import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IPfLoanApplication } from '../../shared/legacy/legacy-model/pf-loan-application.model';

@Component({
  selector: 'jhi-pf-loan-application-detail',
  templateUrl: './pf-loan-application-form-details.component.html',
})
export class PfLoanApplicationFormDetailComponent implements OnInit {
  pfLoanApplication: IPfLoanApplication | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfLoanApplication }) => (this.pfLoanApplication = pfLoanApplication));
  }

  previousState(): void {
    window.history.back();
  }
}
