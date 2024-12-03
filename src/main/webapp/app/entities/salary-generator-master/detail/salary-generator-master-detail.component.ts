import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalaryGeneratorMaster } from '../salary-generator-master.model';

@Component({
  selector: 'jhi-salary-generator-master-detail',
  templateUrl: './salary-generator-master-detail.component.html',
})
export class SalaryGeneratorMasterDetailComponent implements OnInit {
  salaryGeneratorMaster: ISalaryGeneratorMaster | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryGeneratorMaster }) => {
      this.salaryGeneratorMaster = salaryGeneratorMaster;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
