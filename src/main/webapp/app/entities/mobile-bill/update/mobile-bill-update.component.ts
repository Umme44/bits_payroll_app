import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { MobileBillFormGroup, MobileBillFormService } from './mobile-bill-form.service';
import { IMobileBill } from '../mobile-bill.model';
import { MobileBillService } from '../service/mobile-bill.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { SalaryLockService } from '../../salary-generator-master/salary-lock/salary-lock-service';
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-mobile-bill-update',
  templateUrl: './mobile-bill-update.component.html',
})
export class MobileBillUpdateComponent implements OnInit {
  isSaving = false;
  mobileBill: IMobileBill | null = null;
  employees: IEmployee[] = [];

  currentYear: number = new Date().getFullYear();
  years: number[];
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

  editForm: MobileBillFormGroup = this.mobileBillFormService.createMobileBillFormGroup();

  constructor(
    protected mobileBillService: MobileBillService,
    protected mobileBillFormService: MobileBillFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute,
    protected salaryLockService: SalaryLockService
  ) {
    this.years = [
      this.currentYear,
      this.currentYear - 1,
      this.currentYear - 2,
      this.currentYear - 3,
      this.currentYear - 4,
      this.currentYear - 5,
    ];
  }

  // compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ mobileBill }) => {
      this.mobileBill = mobileBill;
      if (mobileBill) {
        this.updateForm(mobileBill);
      }

      this.loadRelationshipsOptions(mobileBill);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const mobileBill = this.mobileBillFormService.getMobileBill(this.editForm);
    if (mobileBill.id !== null) {
      this.subscribeToSaveResponse(this.mobileBillService.update(mobileBill));
    } else {
      this.subscribeToSaveResponse(this.mobileBillService.create(mobileBill as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMobileBill>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(mobileBill: IMobileBill): void {
    this.mobileBill = mobileBill;
    this.mobileBillFormService.resetForm(this.editForm, mobileBill);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   mobileBill.employee
    // );
  }

  checkSalaryLock(month: any, year: any): void {
    this.salaryLockService.isSalaryLocked(month.toString(), year.toString()).subscribe(res => {
      if (res.body && res.body === true) {
        Swal.fire({
          icon: 'error',
          title: 'Oops...',
          text: `${month}, ${year} Salary is Locked!`,
        });
        this.previousState();
      }
    });
  }

  protected loadRelationshipsOptions(mobileBill: IMobileBill): void {
    this.checkSalaryLock(mobileBill.month, mobileBill.year);
    this.employeeService.getAllMinimal().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.mobileBill?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
