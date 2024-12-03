import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { FinalSettlementFormGroup, FinalSettlementFormService } from './final-settlement-form.service';
import { IFinalSettlement } from '../final-settlement.model';
import { FinalSettlementService } from '../service/final-settlement.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';

@Component({
  selector: 'jhi-final-settlement-update',
  templateUrl: './final-settlement-update.component.html',
})
export class FinalSettlementUpdateComponent implements OnInit {
  isSaving = false;
  finalSettlement: IFinalSettlement | null = null;

  employeesSharedCollection: IEmployee[] = [];
  usersSharedCollection: IUser[] = [];

  createdAtDp: any;
  updatedAtDp: any;

  editForm: FinalSettlementFormGroup = this.finalSettlementFormService.createFinalSettlementFormGroup();

  constructor(
    protected finalSettlementService: FinalSettlementService,
    protected finalSettlementFormService: FinalSettlementFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  // compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);
  //
  // compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ finalSettlement }) => {
      this.finalSettlement = finalSettlement;
      if (finalSettlement) {
        this.updateForm(finalSettlement);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const finalSettlement = this.finalSettlementFormService.getFinalSettlement(this.editForm);
    if (finalSettlement.id !== null) {
      this.subscribeToSaveResponse(this.finalSettlementService.update(finalSettlement));
    } else {
      this.subscribeToSaveResponse(this.finalSettlementService.create(finalSettlement as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFinalSettlement>>): void {
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

  protected updateForm(finalSettlement: IFinalSettlement): void {
    this.finalSettlement = finalSettlement;
    this.finalSettlementFormService.resetForm(this.editForm, finalSettlement);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   finalSettlement.employee
    // );
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   finalSettlement.createdBy,
    //   finalSettlement.updatedBy
    // );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService.getAllMinimal().subscribe(res => {
      this.employeesSharedCollection = res.body ?? [];
    });

    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.finalSettlement?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
    //
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(users, this.finalSettlement?.createdBy, this.finalSettlement?.updatedBy)
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
