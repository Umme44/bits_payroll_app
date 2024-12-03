import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { Month } from 'app/shared/model/enumerations/month.model';
import { textNormalize } from 'app/shared/common-util-methods/common-util-methods';
import { ISelectableDTO } from 'app/shared/model/selectable-dto.model';
import { SalaryPayslipAdminService } from './salary-payslip-admin.service';
import { ISalaryPayslip } from 'app/shared/model/salary-payslip.model';
import { IEmployeeMinimal } from 'app/shared/model/employee-minimal.model';
import { OrganizationFilesUrl } from '../../shared/constants/organization-files-url';
import { IEmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';
import { EmployeeService } from '../../shared/legacy/legacy-service/employee.service';

@Component({
  selector: 'jhi-salary-payslip-admin',
  templateUrl: './salary-payslip-admin.component.html',
  styleUrls: ['salary-payslip-admin.component.scss'],
})
export class SalaryPayslipAdminComponent implements OnInit {
  employeeSalaryPaySlip: ISalaryPayslip | null = null;
  employeeSalaries?: IEmployeeSalary[];

  employees: IEmployeeMinimal[] = [];
  tipContent?: string = '';
  loadEmployeeSalary = false;

  editForm = this.fb.group({
    employeeId: [],
    year: [],
    month: [],
  });

  currentYear: number = new Date().getFullYear();
  years: number[];

  salaryMonths: ISelectableDTO[] = [];

  constructor(protected payslipService: SalaryPayslipAdminService, protected employeeService: EmployeeService, protected fb: FormBuilder) {
    this.years = [];
  }

  ngOnInit(): void {
    this.payslipService.getAllEligibleEmployeeForSalaryPayslip().subscribe((res: HttpResponse<IEmployeeMinimal[]>) => {
      this.employees = res.body || [];

      this.employees = this.employees.map(employee => {
        return {
          id: employee.id,
          pin: employee.pin,
          fullName: employee.pin + '-' + employee.fullName + '-' + employee.designationName + '-' + employee.departmentName,
        };
      });
    });
    this.tipContent = 'no payslip exists for selected month';
  }

  previousState(): void {
    window.history.back();
  }

  onClickShowPayslipStatement(): void {
    if (this.editForm.get(['year'])!.value != null && this.editForm.get(['month'])!.value != null) {
      this.loadEmployeeSalary = true;

      const employeeId = this.editForm.get(['employeeId'])!.value;
      const year = this.editForm.get(['year'])!.value;
      const month = this.editForm.get(['month'])!.value;

      this.payslipService.getPayslipForYearMonth(employeeId, year, month).subscribe(res => {
        this.employeeSalaryPaySlip = res.body;
      });
    }
  }

  public downloadAsPDF(): void {
    window.print();
  }

  makeMonthNameShort(month: any): string {
    month = month.toString();
    return month.charAt(0).toUpperCase() + month.charAt(1).toLowerCase() + month.charAt(2).toLowerCase();
  }

  onChangeYear($event: any): void {
    if ($event.target.value) {
      this.editForm.get(['year'])!.setValue($event.target.value);
      const employeeId = this.editForm.get(['employeeId'])!.value;
      const year = this.editForm.get(['year'])!.value;

      this.payslipService.getYearWiseMyDisbursedSalaryMonthList(employeeId, year).subscribe(res => {
        this.salaryMonths = res;
      });
    }
  }

  onSelectEmployee(): void {
    if (this.editForm.get(['employeeId'])!.value !== null && this.editForm.get(['employeeId'])!.value !== undefined) {
      const employeeId = this.editForm.get(['employeeId'])!.value;
      this.payslipService.getMyDisbursedSalaryYearList(employeeId).subscribe(res => {
        this.years = res;
      });
    }
  }

  monthNormalize(month: Month | undefined): string {
    return textNormalize(month!.toString());
  }

  getSalaryPayslipLetterHead(): string {
    return OrganizationFilesUrl.SALARY_PAYSLIP_LETTER_HEAD;
  }
}
