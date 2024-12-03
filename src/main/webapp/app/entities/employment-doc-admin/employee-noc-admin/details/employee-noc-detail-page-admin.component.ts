import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { IEmployeeNOC } from '../../model/employee-noc.model';

@Component({
  selector: 'jhi-employee-noc-detail-admin',
  templateUrl: './employee-noc-detail-page-admin.component.html',
})
export class EmployeeNOCDetailAdminComponent implements OnInit {
  employeeNOC: IEmployeeNOC | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeNOC }) => (this.employeeNOC = employeeNOC));
  }

  previousState(): void {
    window.history.back();
  }
}
