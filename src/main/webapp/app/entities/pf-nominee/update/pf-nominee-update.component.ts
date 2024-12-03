import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PfNomineeFormService, PfNomineeFormGroup } from './pf-nominee-form.service';
import { IPfNominee } from '../pf-nominee.model';
import { PfNomineeService } from '../service/pf-nominee.service';
import { IPfAccount } from 'app/entities/pf-account/pf-account.model';
import { PfAccountService } from 'app/entities/pf-account/service/pf-account.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IdentityType } from 'app/entities/enumerations/identity-type.model';

@Component({
  selector: 'jhi-pf-nominee-update',
  templateUrl: './pf-nominee-update.component.html',
})
export class PfNomineeUpdateComponent implements OnInit {
  isSaving = false;
  pfNominee: IPfNominee | null = null;
  identityTypeValues = Object.keys(IdentityType);

  pfAccountsSharedCollection: IPfAccount[] = [];
  employeesSharedCollection: IEmployee[] = [];

  editForm: PfNomineeFormGroup = this.pfNomineeFormService.createPfNomineeFormGroup();

  constructor(
    protected pfNomineeService: PfNomineeService,
    protected pfNomineeFormService: PfNomineeFormService,
    protected pfAccountService: PfAccountService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  comparePfAccount = (o1: IPfAccount | null, o2: IPfAccount | null): boolean => this.pfAccountService.comparePfAccount(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pfNominee }) => {
      this.pfNominee = pfNominee;
      if (pfNominee) {
        this.updateForm(pfNominee);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pfNominee = this.pfNomineeFormService.getPfNominee(this.editForm);
    if (pfNominee.id !== null) {
      this.subscribeToSaveResponse(this.pfNomineeService.update(pfNominee));
    } else {
      this.subscribeToSaveResponse(this.pfNomineeService.create(pfNominee as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPfNominee>>): void {
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

  protected updateForm(pfNominee: IPfNominee): void {
    this.pfNominee = pfNominee;
    this.pfNomineeFormService.resetForm(this.editForm, pfNominee);

    this.pfAccountsSharedCollection = this.pfAccountService.addPfAccountToCollectionIfMissing<IPfAccount>(
      this.pfAccountsSharedCollection,
      pfNominee.pfAccount
    );
    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      pfNominee.pfWitness,
      pfNominee.approvedBy
    );
  }

  protected loadRelationshipsOptions(): void {
    this.pfAccountService
      .query()
      .pipe(map((res: HttpResponse<IPfAccount[]>) => res.body ?? []))
      .pipe(
        map((pfAccounts: IPfAccount[]) =>
          this.pfAccountService.addPfAccountToCollectionIfMissing<IPfAccount>(pfAccounts, this.pfNominee?.pfAccount)
        )
      )
      .subscribe((pfAccounts: IPfAccount[]) => (this.pfAccountsSharedCollection = pfAccounts));

    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.pfNominee?.pfWitness, this.pfNominee?.approvedBy)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }
}
