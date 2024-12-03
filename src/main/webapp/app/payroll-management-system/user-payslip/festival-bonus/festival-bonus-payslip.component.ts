import { Component, OnInit } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { UserPayslipService } from 'app/payroll-management-system/user-payslip/user-payslip.service';
import { EmployeeCommonService } from 'app/common/employee-address-book/employee-common.service';
import { EmployeeSalary, IEmployeeSalary } from '../../../shared/legacy/legacy-model/employee-salary.model';
import { OrganizationFilesUrl } from '../../../shared/constants/organization-files-url';
import { IFestivalBonusDetails } from '../../../shared/legacy/legacy-model/festival-bonus-details.model';
import { IEmployee } from '../../../entities/employee-custom/employee-custom.model';

@Component({
  selector: 'jhi-festival-bonus-payslip',
  templateUrl: './festival-bonus-payslip.component.html',
  styleUrls: ['festival-bonus-payslip-component.scss'],
})
export class FestivalBonusPayslipComponent implements OnInit {
  employee!: IEmployee;
  employeeSalary!: IEmployeeSalary;
  festivalBonusDetails: IFestivalBonusDetails[] = [];
  selectedFestival: IFestivalBonusDetails | undefined;
  selectedYear = 0;
  selectedFestivalSlNo!: string;
  loadEmployeeSalary!: boolean;

  editForm = this.fb.group({
    employeeSalaryId: [],
    year: [],
    festivalNo: [],
  });

  currentYear: number = new Date().getFullYear();
  years: number[] = [];

  tipContent!: string;

  constructor(protected payslipService: UserPayslipService, protected employeeService: EmployeeCommonService, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.employeeService.getCurrentEmployee().subscribe(res => {
      this.employee = res.body!;
    });

    this.payslipService.getMyFestivalYearList().subscribe(res => {
      this.years = res;
    });

    this.tipContent = 'no payslip exists for selected Festival';
  }

  onClickShowPayslipStatement(): void {
    if (this.editForm.get(['year'])!.value != null && this.editForm.get(['festivalNo'])!.value != null) {
      this.loadEmployeeSalary = true;

      const festivalNo = Number(this.editForm.get(['festivalNo'])!.value);

      let festivalCount = 0;
      this.selectedFestival = this.festivalBonusDetails.find(x => {
        festivalCount++;
        return x.id === festivalNo;
      });
      this.setFestivalSlNo(festivalCount);

      this.employeeSalary = {
        ...new EmployeeSalary(),
        payableGrossBasicSalary: 0,
        payableGrossHouseRent: 0,
        payableGrossMedicalAllowance: 0,
        payableGrossConveyanceAllowance: 0,
        entertainment: 0,
        utility: 0,
        payableGrossSalary: 0,
        allowance01: 0,
        otherDeduction: 0,
        otherAddition: 0,
        salaryAdjustment: 0,
        arrearSalary: 0,
        mobileBillDeduction: 0,
        taxDeduction: 0,
        pfDeduction: 0,
        pfArrear: 0,
        welfareFundDeduction: 0,

        festivalBonus: this.selectedFestival!.bonusAmount,
        netPay: this.selectedFestival!.bonusAmount,
      };
      this.loadEmployeeSalary = true;
    }
  }

  public downloadAsPDF(): void {
    window.print();
  }

  loadFestivalDetailList($event: any): void {
    if ($event.target.value) {
      this.selectedYear = $event.target.value;
      this.payslipService.getYearWiseFestivalDetailsList($event.target.value).subscribe(res => {
        this.festivalBonusDetails = res;
      });
    }
  }

  setFestivalSlNo(festivalNo: number): void {
    if (festivalNo < 10) {
      this.selectedFestivalSlNo = '0' + festivalNo;
    } else {
      this.selectedFestivalSlNo = festivalNo.toString();
    }
  }

  processFestivalSlNumber(festivalSlNumber: number): string {
    if (festivalSlNumber < 10) {
      return '0' + festivalSlNumber;
    }
    return festivalSlNumber.toString();
  }

  getFestivalBonusPayslipLetterHead(): string {
    return OrganizationFilesUrl.FESTIVAL_BONUS_PAYSLIP_LETTER_HEAD;
  }
}
