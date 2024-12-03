import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IArrearSalaryMaster } from '../arrear-salary-master.model';

@Component({
  selector: 'jhi-arrear-salary-master-detail',
  templateUrl: './arrear-salary-master-detail.component.html',
})
export class ArrearSalaryMasterDetailComponent implements OnInit {
  arrearSalaryMaster: IArrearSalaryMaster | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ arrearSalaryMaster }) => {
      this.arrearSalaryMaster = arrearSalaryMaster;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
