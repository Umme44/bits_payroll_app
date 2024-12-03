import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { IEmployeeSalary } from '../../shared/model/employee-salary.model';
import { ActivatedRoute } from '@angular/router';
import { IEmployee } from '../../shared/model/employee.model';
import { EmployeeService } from '../../entities/employee/employee.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-monthly-salary-pay-slip',
  templateUrl: './monthly-salary-pay-slip.component.html',
  styleUrls: ['monthly-salary-pay-slip.component.scss'],
})
export class MonthlySalaryPaySlipComponent implements OnInit {
  employeeSalary: IEmployeeSalary | null = null;
  employee: IEmployee | null = null;

  @ViewChild('pdfPayslip', { static: false })
  pdfPayslip: ElementRef | undefined;

  constructor(protected activatedRoute: ActivatedRoute, protected employeeService: EmployeeService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ employeeSalary }) => (this.employeeSalary = employeeSalary));

    if (this.employeeSalary!.employeeId != null) {
      this.employeeService
        .find(this.employeeSalary!.employeeId)
        .subscribe((res: HttpResponse<IEmployee>) => (this.employee = res.body || null));
    }
  }

  previousState(): void {
    window.history.back();
  }

  public downloadAsPDF(): void {
    window.print();
  }
}
