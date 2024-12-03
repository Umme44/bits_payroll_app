import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { HoldSalaryDisbursementFormGroup, HoldSalaryDisbursementFormService } from './hold-salary-disbursement-form.service';
import { IHoldSalaryDisbursement } from '../hold-salary-disbursement.model';
import { HoldSalaryDisbursementService } from '../service/hold-salary-disbursement.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { IEmployeeSalary } from 'app/entities/employee-salary/employee-salary.model';
import { EmployeeSalaryService } from 'app/entities/employee-salary/service/employee-salary.service';

@Component({
  selector: 'jhi-hold-salary-disbursement-update',
  templateUrl: './hold-salary-disbursement-update.component.html',
})
export class HoldSalaryDisbursementUpdateComponent implements OnInit {
  isSaving = false;
  holdSalaryDisbursement: IHoldSalaryDisbursement | null = null;

  usersSharedCollection: IUser[] = [];
  employeeSalariesSharedCollection: IEmployeeSalary[] = [];

  editForm: HoldSalaryDisbursementFormGroup = this.holdSalaryDisbursementFormService.createHoldSalaryDisbursementFormGroup();

  constructor(
    protected holdSalaryDisbursementService: HoldSalaryDisbursementService,
    protected holdSalaryDisbursementFormService: HoldSalaryDisbursementFormService,
    protected userService: UserService,
    protected employeeSalaryService: EmployeeSalaryService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareEmployeeSalary = (o1: IEmployeeSalary | null, o2: IEmployeeSalary | null): boolean =>
    this.employeeSalaryService.compareEmployeeSalary(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ holdSalaryDisbursement }) => {
      this.holdSalaryDisbursement = holdSalaryDisbursement;
      if (holdSalaryDisbursement) {
        this.updateForm(holdSalaryDisbursement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const holdSalaryDisbursement = this.holdSalaryDisbursementFormService.getHoldSalaryDisbursement(this.editForm);
    if (holdSalaryDisbursement.id !== null) {
      this.subscribeToSaveResponse(this.holdSalaryDisbursementService.update(holdSalaryDisbursement));
    } else {
      this.subscribeToSaveResponse(this.holdSalaryDisbursementService.create(holdSalaryDisbursement as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHoldSalaryDisbursement>>): void {
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

  protected updateForm(holdSalaryDisbursement: IHoldSalaryDisbursement): void {
    this.holdSalaryDisbursement = holdSalaryDisbursement;
    this.holdSalaryDisbursementFormService.resetForm(this.editForm, holdSalaryDisbursement);

    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   holdSalaryDisbursement.user
    // );
    // this.employeeSalariesSharedCollection = this.employeeSalaryService.addEmployeeSalaryToCollectionIfMissing<IEmployeeSalary>(
    //   this.employeeSalariesSharedCollection,
    //   holdSalaryDisbursement.employeeSalary
    // );
  }

  protected loadRelationshipsOptions(): void {
    //   this.userService
    //     .query()
    //     .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //     .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.holdSalaryDisbursement?.user)))
    //     .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
    //
    //   this.employeeSalaryService
    //     .query()
    //     .pipe(map((res: HttpResponse<IEmployeeSalary[]>) => res.body ?? []))
    //     .pipe(
    //       map((employeeSalaries: IEmployeeSalary[]) =>
    //         this.employeeSalaryService.addEmployeeSalaryToCollectionIfMissing<IEmployeeSalary>(
    //           employeeSalaries,
    //           this.holdSalaryDisbursement?.employeeSalary
    //         )
    //       )
    //     )
    //     .subscribe((employeeSalaries: IEmployeeSalary[]) => (this.employeeSalariesSharedCollection = employeeSalaries));
  }
}
