import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEmployeeDocument } from '../employee-document.model';

@Component({
  selector: 'jhi-employee-document-detail',
  templateUrl: './employee-document-detail.component.html',
})
export class EmployeeDocumentDetailComponent implements OnInit {
  employeeDocument: IEmployeeDocument | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeDocument }) => {
      this.employeeDocument = employeeDocument;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
