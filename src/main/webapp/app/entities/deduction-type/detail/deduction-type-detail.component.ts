import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDeductionType } from '../deduction-type.model';

@Component({
  selector: 'jhi-deduction-type-detail',
  templateUrl: './deduction-type-detail.component.html',
})
export class DeductionTypeDetailComponent implements OnInit {
  deductionType: IDeductionType | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ deductionType }) => {
      this.deductionType = deductionType;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
