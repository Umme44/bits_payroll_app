import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { NomineeFormService, NomineeFormGroup } from './nominee-form.service';
import { INominee } from '../nominee.model';
import { NomineeService } from '../service/nominee.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { Status } from 'app/entities/enumerations/status.model';
import { NomineeType } from 'app/entities/enumerations/nominee-type.model';
import { IdentityType } from 'app/entities/enumerations/identity-type.model';

@Component({
  selector: 'jhi-nominee-update',
  templateUrl: './nominee-update.component.html',
})
export class NomineeUpdateComponent implements OnInit {
  isSaving = false;
  nominee: INominee | null = null;
  statusValues = Object.keys(Status);
  nomineeTypeValues = Object.keys(NomineeType);
  identityTypeValues = Object.keys(IdentityType);

  employeesSharedCollection: IEmployee[] = [];

  editForm: NomineeFormGroup = this.nomineeFormService.createNomineeFormGroup();

  constructor(
    protected nomineeService: NomineeService,
    protected nomineeFormService: NomineeFormService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ nominee }) => {
      this.nominee = nominee;
      if (nominee) {
        this.updateForm(nominee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const nominee = this.nomineeFormService.getNominee(this.editForm);
    if (nominee.id !== null) {
      this.subscribeToSaveResponse(this.nomineeService.update(nominee));
    } else {
      this.subscribeToSaveResponse(this.nomineeService.create(nominee as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<INominee>>): void {
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

  protected updateForm(nominee: INominee): void {
    this.nominee = nominee;
    this.nomineeFormService.resetForm(this.editForm, nominee);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      nominee.employee,
      nominee.approvedBy,
      nominee.witness,
      nominee.member
    );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
            employees,
            this.nominee?.employee,
            this.nominee?.approvedBy,
            this.nominee?.witness,
            this.nominee?.member
          )
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
