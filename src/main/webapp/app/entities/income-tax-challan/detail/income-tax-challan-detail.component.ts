import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIncomeTaxChallan } from '../income-tax-challan.model';

@Component({
  selector: 'jhi-income-tax-challan-detail',
  templateUrl: './income-tax-challan-detail.component.html',
})
export class IncomeTaxChallanDetailComponent implements OnInit {
  incomeTaxChallan: IIncomeTaxChallan | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ incomeTaxChallan }) => {
      this.incomeTaxChallan = incomeTaxChallan;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
