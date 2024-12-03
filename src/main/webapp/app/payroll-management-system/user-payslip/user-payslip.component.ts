import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { UserPayslipService } from './user-payslip.service';
import { EmployeeCommonService } from '../../common/employee-address-book/employee-common.service';
import { FormBuilder } from '@angular/forms';
import { Month } from 'app/shared/model/enumerations/month.model';
import { textNormalize } from 'app/shared/common-util-methods/common-util-methods';
import { ISelectableDTO } from 'app/shared/model/selectable-dto.model';
import { ISalaryPayslip } from 'app/shared/model/salary-payslip.model';
import { swalOnRequestErrorWithBackEndErrorTitle } from 'app/shared/swal-common/swal-common';
import { OrganizationFilesUrl } from '../../shared/constants/organization-files-url';
import { IEmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';
import { IEmployee } from '../../entities/employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-user-payslip',
  templateUrl: './user-payslip.component.html',
  styleUrls: ['user-payslip.component.scss'],
})
export class UserPayslipComponent implements OnInit {
  employeeSalaryPaySlip: ISalaryPayslip | null = null;
  employeeSalaries?: IEmployeeSalary[];

  employee: IEmployee | null = null;
  tipContent?: string = '';
  loadEmployeeSalary = false;

  editForm = this.fb.group({
    employeeSalaryId: [],
    year: [],
    month: [],
  });

  currentYear: number = new Date().getFullYear();
  years: number[];

  salaryMonths: ISelectableDTO[] = [];

  constructor(protected payslipService: UserPayslipService, protected employeeService: EmployeeCommonService, protected fb: FormBuilder) {
    this.years = [];
  }

  ngOnInit(): void {
    this.employeeService.getCurrentEmployee().subscribe((res: HttpResponse<IEmployee>) => {
      this.employee = res.body || null;
    });

    this.payslipService.getMyDisbursedSalaryYearList().subscribe(res => {
      this.years = res;
    });

    this.tipContent = 'no payslip exists for selected month';
  }

  previousState(): void {
    window.history.back();
  }

  onClickShowPayslipStatement(): void {
    if (this.editForm.get(['year'])!.value != null && this.editForm.get(['month'])!.value != null) {
      this.loadEmployeeSalary = true;

      const year = this.editForm.get(['year'])!.value;
      const month = this.editForm.get(['month'])!.value;

      this.payslipService.getPayslipForYearMonth(year, month).subscribe(
        res => {
          this.employeeSalaryPaySlip = res.body;
        },
        err => {
          swalOnRequestErrorWithBackEndErrorTitle(err.message);
        }
      );
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
      this.payslipService.getYearWiseMyDisbursedSalaryMonthList($event.target.value).subscribe(res => {
        this.salaryMonths = res;
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
