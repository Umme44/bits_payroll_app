import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArrearSalaryItem } from '../arrear-salary-item.model';

@Component({
  selector: 'jhi-arrear-salary-item-detail',
  templateUrl: './arrear-salary-item-detail.component.html',
})
export class ArrearSalaryItemDetailComponent implements OnInit {
  arrearSalaryItem: IArrearSalaryItem | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arrearSalaryItem }) => {
      this.arrearSalaryItem = arrearSalaryItem;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
