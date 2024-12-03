import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { FlexScheduleFormGroup, FlexScheduleFormService } from './flex-schedule-form.service';
import { IFlexSchedule } from '../flex-schedule.model';
import { FlexScheduleService } from '../service/flex-schedule.service';
import { IEmployee } from 'app/entities/employee/employee.model';
import { EmployeeService } from 'app/entities/employee/service/employee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';

@Component({
  selector: 'jhi-flex-schedule-update',
  templateUrl: './flex-schedule-update.component.html',
})
export class FlexScheduleUpdateComponent implements OnInit {
  isSaving = false;
  flexSchedule: IFlexSchedule | null = null;

  employeesSharedCollection: IEmployee[] = [];
  usersSharedCollection: IUser[] = [];

  editForm: FlexScheduleFormGroup = this.flexScheduleFormService.createFlexScheduleFormGroup();

  constructor(
    protected flexScheduleService: FlexScheduleService,
    protected flexScheduleFormService: FlexScheduleFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flexSchedule }) => {
      this.flexSchedule = flexSchedule;
      if (flexSchedule) {
        this.updateForm(flexSchedule);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const flexSchedule = this.flexScheduleFormService.getFlexSchedule(this.editForm);
    if (flexSchedule.id !== null) {
      this.subscribeToSaveResponse(this.flexScheduleService.update(flexSchedule));
    } else {
      this.subscribeToSaveResponse(this.flexScheduleService.create(flexSchedule as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlexSchedule>>): void {
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

  protected updateForm(flexSchedule: IFlexSchedule): void {
    this.flexSchedule = flexSchedule;
    this.flexScheduleFormService.resetForm(this.editForm, flexSchedule);

    this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
      this.employeesSharedCollection,
      flexSchedule.employee
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, flexSchedule.createdBy);
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService
      .query()
      .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
      .pipe(
        map((employees: IEmployee[]) =>
          this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.flexSchedule?.employee)
        )
      )
      .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.flexSchedule?.createdBy)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
