import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeStaticFile } from '../employee-static-file.model';

@Component({
  selector: 'jhi-employee-static-file-detail',
  templateUrl: './employee-static-file-detail.component.html',
})
export class EmployeeStaticFileDetailComponent implements OnInit {
  employeeStaticFile: IEmployeeStaticFile | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeStaticFile }) => {
      this.employeeStaticFile = employeeStaticFile;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
