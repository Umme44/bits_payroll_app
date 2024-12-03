import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPfArrear } from '../pf-arrear.model';
import { textNormalize } from '../../../shared/common-util-methods/common-util-methods';

@Component({
  selector: 'jhi-pf-arrear-detail',
  templateUrl: './pf-arrear-detail.component.html',
})
export class PfArrearDetailComponent implements OnInit {
  pfArrear: IPfArrear | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfArrear }) => {
      this.pfArrear = pfArrear;
    });
  }

  previousState(): void {
    window.history.back();
  }

  monthNameNormalize(month: any): string {
    return textNormalize(month);
  }
}
