import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IndividualArrearSalaryFormGroup, IndividualArrearSalaryFormService } from './individual-arrear-salary-form.service';
import { IIndividualArrearSalary } from '../individual-arrear-salary.model';
import { IndividualArrearSalaryService } from '../service/individual-arrear-salary.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';

@Component({
  selector: 'jhi-individual-arrear-salary-update',
  templateUrl: './individual-arrear-salary-update.component.html',
})
export class IndividualArrearSalaryUpdateComponent implements OnInit {
  isSaving = false;
  individualArrearSalary: IIndividualArrearSalary | null = null;
  effectiveDateDp: any;

  employeesSharedCollection: IEmployee[] = [];

  editForm: IndividualArrearSalaryFormGroup = this.individualArrearSalaryFormService.createIndividualArrearSalaryFormGroup();

  constructor(
    protected individualArrearSalaryService: IndividualArrearSalaryService,
    protected individualArrearSalaryFormService: IndividualArrearSalaryFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  // compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ individualArrearSalary }) => {
      this.individualArrearSalary = individualArrearSalary;
      if (individualArrearSalary) {
        this.updateForm(individualArrearSalary);
      }
      this.loadRelationshipsOptions();
    });
  }

  loadEmployees(): void {
    this.employeeService.getAllMinimal().subscribe(res => {
      this.employeesSharedCollection = res.body || [];
      this.employeesSharedCollection = this.employeesSharedCollection.map(item => {
        return {
          id: item.id,
          fullName: item.pin + ' - ' + item.fullName,
        };
      });
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const individualArrearSalary = this.individualArrearSalaryFormService.getIndividualArrearSalary(this.editForm);
    if (individualArrearSalary.id !== null) {
      this.subscribeToSaveResponse(this.individualArrearSalaryService.update(individualArrearSalary));
    } else {
      this.subscribeToSaveResponse(this.individualArrearSalaryService.create(individualArrearSalary as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IIndividualArrearSalary>>): void {
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

  protected updateForm(individualArrearSalary: IIndividualArrearSalary): void {
    this.individualArrearSalary = individualArrearSalary;
    this.individualArrearSalaryFormService.resetForm(this.editForm, individualArrearSalary);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   individualArrearSalary.employee
    // );
  }

  protected loadRelationshipsOptions(): void {
    this.loadEmployees();

    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.individualArrearSalary?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
