import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ISalaryGenerationModel, SalaryGenerationModel } from '../../shared/model/salary-generation.model';
import { SalaryGenerationService } from '../salary-generation/salary-generation.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { SalaryGenerationUtilService } from '../salary-generation/salary-generation-util.Service';
import { ISalaryGenerationPreValidation } from 'app/shared/model/salary-generation-pre-validation.model';
import { ISalaryGeneratorMaster } from '../../shared/legacy/legacy-model/salary-generator-master.model';
import { IEmployee } from '../../shared/legacy/legacy-model/employee.model';
import { SalaryGeneratorMasterService } from '../../shared/legacy/legacy-service/salary-generator-master.service';
import { EmployeeService } from '../../shared/legacy/legacy-service/employee.service';
import { MobileBillService } from '../../shared/legacy/legacy-service/mobile-bill.service';
import { PfLoanRepaymentService } from '../../shared/legacy/legacy-service/pf-loan-repayment.service';
import { AttendanceSummaryService } from '../../shared/legacy/legacy-service/attendance-summary.service';
import { IEmployeeSalary } from '../../shared/legacy/legacy-model/employee-salary.model';

// noinspection DuplicatedCode,JSUnusedLocalSymbols
@Component({
  selector: 'jhi-single-salary-generation',
  templateUrl: './single-salary-generation.component.html',
  styleUrls: ['single-salary-generation.component.scss'],
})
export class SingleSalaryGenerationComponent implements OnInit {
  salaryGenerationModel: ISalaryGenerationModel;
  salaryGeneratorMasters: ISalaryGeneratorMaster[] = [];
  salaries!: ISalaryGeneratorMaster;

  isEmployeeSelected = false;

  ready = false;
  currentYear: number = new Date().getFullYear();
  years: number[];

  employees: IEmployee[] = [];

  months = [
    { Value: 1, Text: 'January' },
    { Value: 2, Text: 'February' },
    { Value: 3, Text: 'March' },
    { Value: 4, Text: 'April' },
    { Value: 5, Text: 'May' },
    { Value: 6, Text: 'June' },
    { Value: 7, Text: 'July' },
    { Value: 8, Text: 'August' },
    { Value: 9, Text: 'September' },
    { Value: 10, Text: 'October' },
    { Value: 11, Text: 'November' },
    { Value: 12, Text: 'December' },
  ];

  salaryGenerationPreValidation!: ISalaryGenerationPreValidation;
  editForm = this.fb.group({
    employeeId: this.fb.group({
      employeeId: [],
    }),
    year: [],
    month: [],
  });

  constructor(
    protected salaryGenerationService: SalaryGenerationService,
    protected salaryGenerationUtilService: SalaryGenerationUtilService,
    protected salaryGeneratorMasterService: SalaryGeneratorMasterService,
    protected employeeService: EmployeeService,
    protected mobileBillService: MobileBillService,
    protected fb: FormBuilder,
    protected pfLoanRepaymentService: PfLoanRepaymentService,
    protected attendanceSummaryService: AttendanceSummaryService,
    protected router: Router
  ) {
    this.years = [
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
    ];
    this.salaryGenerationModel = new SalaryGenerationModel();
  }

  get employeeIdForm(): FormGroup {
    return this.editForm.get('employeeId') as FormGroup;
  }

  ngOnInit(): void {
    this.editForm.patchValue({
      year: this.years[0],
      month: this.months[0].Value,
    });
  }

  trackId(index: number, item: IEmployeeSalary): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  generate(): void {
    const employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    if (employeeId !== undefined) {
      this.salaryGenerationService
        .generateSingleSalary(employeeId, this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        .subscribe((resp: HttpResponse<boolean>) => {
          this.router.navigate(['employee-salary', employeeId, this.editForm.get(['year'])?.value, this.editForm.get(['month'])?.value]);
        });
    }
  }

  clickNext(): void {
    const employeeId = this.employeeIdForm.get(['employeeId'])!.value;
    if (employeeId !== undefined) {
      this.isEmployeeSelected = true;
    }

    this.ready = !this.ready;

    this.salaryGenerationUtilService
      .getSalaryGenerationPreValidation(this.editForm.get(['year'])!.value, this.editForm.get(['month'])!.value)
      .subscribe((resp: HttpResponse<ISalaryGenerationPreValidation>) => {
        this.salaryGenerationPreValidation = resp.body!;
      });

    this.salaryGeneratorMasterService.query().subscribe((res: HttpResponse<ISalaryGeneratorMaster[]>) => {
      this.salaryGeneratorMasters = res.body || [];
      const month: string = this.editForm.get(['month'])!.value + '';
      const year: string = this.editForm.get(['year'])!.value + '';
      if (this.salaryGeneratorMasters !== undefined && this.salaryGeneratorMasters.length > 0) {
        this.salaries = this.salaryGeneratorMasters.filter(x => x.month === month && x.year === year)[0];
      }
    });
  }
}
