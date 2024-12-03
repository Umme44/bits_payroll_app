import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArrearSalary } from '../arrear-salary.model';

@Component({
  selector: 'jhi-arrear-salary-detail',
  templateUrl: './arrear-salary-detail.component.html',
})
export class ArrearSalaryDetailComponent implements OnInit {
  arrearSalary: IArrearSalary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arrearSalary }) => {
      this.arrearSalary = arrearSalary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
