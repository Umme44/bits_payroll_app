import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFinalSettlement } from '../final-settlement.model';
import { EmployeeCategory } from '../../../shared/model/enumerations/employee-category.model';

@Component({
  selector: 'jhi-final-settlement-detail',
  templateUrl: './final-settlement-detail.component.html',
})
export class FinalSettlementDetailComponent implements OnInit {
  finalSettlement: IFinalSettlement | null = null;

  @ViewChild('pdfFinalSettlement', { static: false })
  pdfFinalSettlement: ElementRef | undefined;

  organizationFullName = '';

  constructor(protected activatedRoute: ActivatedRoute) {
    if (sessionStorage.getItem('organizationFullName')) {
      this.organizationFullName = sessionStorage.getItem('organizationFullName')!;
    }
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ finalSettlement }) => {
      this.finalSettlement = finalSettlement;
    });
  }

  hasAccess(): boolean {
    return this.finalSettlement!.employeeCategory === EmployeeCategory.REGULAR_CONFIRMED_EMPLOYEE;
  }

  public downloadAsPDF(): void {
    window.print();
  }

  previousState(): void {
    window.history.back();
  }
}
