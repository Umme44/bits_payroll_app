import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IMobileBill } from '../mobile-bill.model';
import { IEmployee } from '../../employee-custom/employee-custom.model';
import { EmployeeCustomService } from '../../employee-custom/service/employee-custom.service';
import { HttpResponse } from '@angular/common/http';
import dayjs from 'dayjs/esm';

@Component({
  selector: 'jhi-mobile-bill-detail',
  templateUrl: './mobile-bill-detail.component.html',
})
export class MobileBillDetailComponent implements OnInit {
  mobileBill: IMobileBill | null = null;
  employee: IEmployee | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected employeeService: EmployeeCustomService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mobileBill }) => {
      this.mobileBill = mobileBill;
      if (this.mobileBill!.employeeId != null) {
        this.employeeService
          .find(this.mobileBill!.employeeId)
          .subscribe((res: HttpResponse<IEmployee>) => (this.employee = res.body || null));
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  toDate(year: number, month: number): string {
    return dayjs(new Date(Number(year), Number(month) - 1)).format('MMM, YYYY');
  }
}
