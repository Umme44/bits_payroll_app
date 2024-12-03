import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { ISelectableDTO } from 'app/shared/model/selectable-dto.model';
import { UserPayslipService } from 'app/payroll-management-system/user-payslip/user-payslip.service';
import { EmployeeCommonService } from 'app/common/employee-address-book/employee-common.service';
import { swalOnRequestErrorWithBackEndErrorTitle } from 'app/shared/swal-common/swal-common';
import { IIndividualArrearPayslip } from 'app/shared/model/individual-arrear-payslip.model';
import { OrganizationFilesUrl } from '../../../config/organization-files-url';

@Component({
  selector: 'jhi-arrear-slip',
  templateUrl: './arrear-slip.component.html',
  styleUrls: ['./arrear-slip.component.scss'],
})
export class ArrearSlipComponent implements OnInit {
  employeeArrearPaySlip: IIndividualArrearPayslip | null = null;

  tipContent?: string = '';
  loadPayslip = false;

  editForm = this.fb.group({
    year: [],
    title: [],
  });

  currentYear: number = new Date().getFullYear();
  years: number[];

  arrearTitles: ISelectableDTO[] = [];

  constructor(protected payslipService: UserPayslipService, protected employeeService: EmployeeCommonService, protected fb: FormBuilder) {
    this.years = [];
  }

  ngOnInit(): void {
    this.payslipService.getMyArrearYearList().subscribe(res => {
      this.years = res;
    });

    this.tipContent = 'no payslip exists for selected title';
  }

  previousState(): void {
    window.history.back();
  }

  onClickShowPayslipStatement(): void {
    if (this.editForm.get(['year'])!.value != null && this.editForm.get(['title'])!.value != null) {
      this.loadPayslip = true;

      const year = this.editForm.get(['year'])!.value;
      const title = this.editForm.get(['title'])!.value;

      this.payslipService.getMyArrearDetail(title).subscribe(
        res => {
          this.employeeArrearPaySlip = res;
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

  onChangeYear($event: any): void {
    if ($event.target.value) {
      this.payslipService.getMyArrearTitleListByYear($event.target.value).subscribe(res => {
        this.arrearTitles = res;
        this.editForm.get('title')?.reset();
      });
    }
  }

  getSalaryPayslipLetterHead(): string {
    return OrganizationFilesUrl.SALARY_PAYSLIP_LETTER_HEAD;
  }
}
