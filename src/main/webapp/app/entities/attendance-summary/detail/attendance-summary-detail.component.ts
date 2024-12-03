import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import dayjs from 'dayjs/esm';

import { HttpResponse } from '@angular/common/http';
import {IAttendanceSummary} from "../attendance-summary.model";
import {IEmployee} from "../../employee/employee.model";
import {EmployeeService} from "../../employee/service/employee.service";
import {SalaryLockService} from "../../../shared/legacy/legacy-service/salary-lock-service";


@Component({
  selector: 'jhi-attendance-summary-detail',
  templateUrl: './attendance-summary-detail.component.html',
})
export class AttendanceSummaryDetailComponent implements OnInit {
  attendanceSummary: IAttendanceSummary | null = null;
  employee: IEmployee | null = null;
  isSalaryLocked!: boolean;

  constructor(
    protected activatedRoute: ActivatedRoute,
    protected employeeService: EmployeeService,
    protected salaryLockService: SalaryLockService
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ attendanceSummary }) => {
      this.attendanceSummary = attendanceSummary;
      this.checkThisMonthSalaryIsLocked(attendanceSummary.month, attendanceSummary.year);
    });
    if (this.attendanceSummary!.employeeId != null) {
      this.employeeService
        .find(this.attendanceSummary!.employeeId)
        .subscribe((res: HttpResponse<IEmployee>) => (this.employee = res.body || null));
    }
  }

  checkThisMonthSalaryIsLocked(month: any, year: any): void {
    //check selected month salary is locked
    this.salaryLockService.isSalaryLocked(month.toString(), year.toString()).subscribe(isLocked => {
      if (isLocked.body && isLocked.body === true) {
        this.isSalaryLocked = true;
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  convertMonthNumberToDayjs(month: number): dayjs.Dayjs {
    return dayjs().month(month);
  }
}
