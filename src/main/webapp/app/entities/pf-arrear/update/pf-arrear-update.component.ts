import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PfArrearFormService, PfArrearFormGroup } from './pf-arrear-form.service';
import { IPfArrear } from '../pf-arrear.model';
import { PfArrearService } from '../service/pf-arrear.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Month } from 'app/entities/enumerations/month.model';

@Component({
  selector: 'jhi-pf-arrear-update',
  templateUrl: './pf-arrear-update.component.html',
})
export class PfArrearUpdateComponent implements OnInit {
  isSaving = false;
  pfArrear: IPfArrear | null = null;
  monthValues = Object.keys(Month);

  employeesSharedCollection: IEmployee[] = [];

  editForm: PfArrearFormGroup = this.pfArrearFormService.createPfArrearFormGroup();
  currentYear: number = new Date().getFullYear();

  constructor(
    protected pfArrearService: PfArrearService,
    protected pfArrearFormService: PfArrearFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.years = this.getLastFewYears();
  }

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);
  years: number[];

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfArrear }) => {
      this.pfArrear = pfArrear;
      if (pfArrear) {
        this.updateForm(pfArrear);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pfArrear = this.pfArrearFormService.getPfArrear(this.editForm);
    if (pfArrear.id !== null) {
      this.subscribeToSaveResponse(this.pfArrearService.update(pfArrear));
    } else {
      this.subscribeToSaveResponse(this.pfArrearService.create(pfArrear as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfArrear>>): void {
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

  protected updateForm(pfArrear: IPfArrear): void {
    this.pfArrear = pfArrear;
    this.pfArrearFormService.resetForm(this.editForm, pfArrear);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      pfArrear.employee
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.pfArrear?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
  changeEmployee(employee: IEmployee): void {
    const deptHeadFormControl = this.editForm.get(['employeeId'])!;
    deptHeadFormControl.markAsTouched();
    if (employee) {
      deptHeadFormControl.setValue(employee.id);
    } else {
      deptHeadFormControl.setValue(null);
    }
  }

  private getLastFewYears(): number[] {
    const years = [];
    for (let i = 0; i < 5; i++) {
      years.push(this.currentYear - i);
    }
    return years;
  }

  patchEmployeeID($event: any) {
    this.editForm.patchValue({
      employeeId: $event.id,
    });
  }
}
