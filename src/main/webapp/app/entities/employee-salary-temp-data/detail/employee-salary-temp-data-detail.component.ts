import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeSalaryTempData } from '../employee-salary-temp-data.model';

@Component({
  selector: 'jhi-employee-salary-temp-data-detail',
  templateUrl: './employee-salary-temp-data-detail.component.html',
})
export class EmployeeSalaryTempDataDetailComponent implements OnInit {
  employeeSalaryTempData: IEmployeeSalaryTempData | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeSalaryTempData }) => {
      this.employeeSalaryTempData = employeeSalaryTempData;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
