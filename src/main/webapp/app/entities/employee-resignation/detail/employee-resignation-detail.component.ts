import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeResignation } from '../employee-resignation.model';

@Component({
  selector: 'jhi-employee-resignation-detail',
  templateUrl: './employee-resignation-detail.component.html',
})
export class EmployeeResignationDetailComponent implements OnInit {
  employeeResignation: IEmployeeResignation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeResignation }) => {
      this.employeeResignation = employeeResignation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
