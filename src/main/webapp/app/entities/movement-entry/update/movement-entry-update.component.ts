import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MovementEntryFormService, MovementEntryFormGroup } from './movement-entry-form.service';
import { IMovementEntry } from '../movement-entry.model';
import { MovementEntryService } from '../service/movement-entry.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { MovementType } from 'app/entities/enumerations/movement-type.model';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  selector: 'jhi-movement-entry-update',
  templateUrl: './movement-entry-update.component.html',
})
export class MovementEntryUpdateComponent implements OnInit {
  isSaving = false;
  movementEntry: IMovementEntry | null = null;
  movementTypeValues = Object.keys(MovementType);
  statusValues = Object.keys(Status);

  // employeesSharedCollection: IEmployee[] = [];
  // usersSharedCollection: IUser[] = [];

  employees: IEmployee[] = [];
  users: IUser[] = [];
  startDateDp: any;
  endDateDp: any;
  createdAtDp: any;
  updatedAtDp: any;

  editForm: MovementEntryFormGroup = this.movementEntryFormService.createMovementEntryFormGroup();

  constructor(
    protected movementEntryService: MovementEntryService,
    protected movementEntryFormService: MovementEntryFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ movementEntry }) => {
      this.movementEntry = movementEntry;
      if (movementEntry) {
        this.updateForm(movementEntry);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const movementEntry = this.movementEntryFormService.getMovementEntry(this.editForm);
    if (movementEntry.id !== null) {
      this.subscribeToSaveResponse(this.movementEntryService.update(movementEntry));
    } else {
      this.subscribeToSaveResponse(this.movementEntryService.create(movementEntry as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMovementEntry>>): void {
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

  protected updateForm(movementEntry: IMovementEntry): void {
    this.movementEntry = movementEntry;
    this.movementEntryFormService.resetForm(this.editForm, movementEntry);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   movementEntry.employee
    // );
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   movementEntry.createdByLogin,
    //   movementEntry.updatedByLogin,
    //   movementEntry.sanctionByLogin
    // );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

    this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.movementEntry?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
    //
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(
    //         users,
    //         this.movementEntry?.createdByLogin,
    //         this.movementEntry?.updatedByLogin,
    //         this.movementEntry?.sanctionByLogin
    //       )
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  trackById(index: number, item: any): any {
    return item.id;
  }
}
