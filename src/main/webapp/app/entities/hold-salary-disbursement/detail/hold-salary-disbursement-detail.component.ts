import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHoldSalaryDisbursement } from '../hold-salary-disbursement.model';

@Component({
  selector: 'jhi-hold-salary-disbursement-detail',
  templateUrl: './hold-salary-disbursement-detail.component.html',
})
export class HoldSalaryDisbursementDetailComponent implements OnInit {
  holdSalaryDisbursement: IHoldSalaryDisbursement | null = null;
  searchText = '';

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ holdSalaryDisbursement }) => {
      this.holdSalaryDisbursement = holdSalaryDisbursement;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
