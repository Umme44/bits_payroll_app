import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SalaryDeductionFormService, SalaryDeductionFormGroup } from './salary-deduction-form.service';
import { ISalaryDeduction } from '../salary-deduction.model';
import { SalaryDeductionService } from '../service/salary-deduction.service';
import { IDeductionType } from 'app/entities/deduction-type/deduction-type.model';
import { DeductionTypeService } from 'app/entities/deduction-type/service/deduction-type.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';

@Component({
  selector: 'jhi-salary-deduction-update',
  templateUrl: './salary-deduction-update.component.html',
})
export class SalaryDeductionUpdateComponent implements OnInit {
  isSaving = false;
  salaryDeduction: ISalaryDeduction | null = null;

  deductionTypesSharedCollection: IDeductionType[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm: SalaryDeductionFormGroup = this.salaryDeductionFormService.createSalaryDeductionFormGroup();

  constructor(
    protected salaryDeductionService: SalaryDeductionService,
    protected salaryDeductionFormService: SalaryDeductionFormService,
    protected deductionTypeService: DeductionTypeService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareDeductionType = (o1: IDeductionType | null, o2: IDeductionType | null): boolean =>
    this.deductionTypeService.compareDeductionType(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salaryDeduction }) => {
      this.salaryDeduction = salaryDeduction;
      if (salaryDeduction) {
        this.updateForm(salaryDeduction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const salaryDeduction = this.salaryDeductionFormService.getSalaryDeduction(this.editForm);
    if (salaryDeduction.id !== null) {
      this.subscribeToSaveResponse(this.salaryDeductionService.update(salaryDeduction));
    } else {
      this.subscribeToSaveResponse(this.salaryDeductionService.create(salaryDeduction as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISalaryDeduction>>): void {
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

  protected updateForm(salaryDeduction: ISalaryDeduction): void {
    this.salaryDeduction = salaryDeduction;
    this.salaryDeductionFormService.resetForm(this.editForm, salaryDeduction);

    // this.deductionTypesSharedCollection = this.deductionTypeService.addDeductionTypeToCollectionIfMissing<IDeductionType>(
    //   this.deductionTypesSharedCollection,
    //   salaryDeduction.deductionType
    // );
    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   salaryDeduction.employee
    // );
  }

  protected loadRelationshipsOptions(): void {
    this.deductionTypeService
      .query()
      .subscribe((res: HttpResponse<IDeductionType[]>) => (this.deductionTypesSharedCollection = res.body || []));

    this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employeesSharedCollection = res.body || []));

    // this.deductionTypeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IDeductionType[]>) => res.body ?? []))
    //   .pipe(
    //     map((deductionTypes: IDeductionType[]) =>
    //       this.deductionTypeService.addDeductionTypeToCollectionIfMissing<IDeductionType>(
    //         deductionTypes,
    //         this.salaryDeduction?.deductionType
    //       )
    //     )
    //   )
    //   .subscribe((deductionTypes: IDeductionType[]) => (this.deductionTypesSharedCollection = deductionTypes));
    //
    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.salaryDeduction?.employeeId)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
