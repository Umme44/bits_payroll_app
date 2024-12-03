import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IIndividualArrearSalary } from '../individual-arrear-salary.model';

@Component({
  selector: 'jhi-individual-arrear-salary-detail',
  templateUrl: './individual-arrear-salary-detail.component.html',
})
export class IndividualArrearSalaryDetailComponent implements OnInit {
  individualArrearSalary: IIndividualArrearSalary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ individualArrearSalary }) => {
      this.individualArrearSalary = individualArrearSalary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
