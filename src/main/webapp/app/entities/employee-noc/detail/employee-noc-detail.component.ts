import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeNOC } from '../employee-noc.model';

@Component({
  selector: 'jhi-employee-noc-detail',
  templateUrl: './employee-noc-detail.component.html',
})
export class EmployeeNOCDetailComponent implements OnInit {
  employeeNOC: IEmployeeNOC | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeNOC }) => {
      this.employeeNOC = employeeNOC;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
