import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import dayjs from 'dayjs/esm';
import {
  WorkFromHomeApplicationFormGroup,
  WorkFromHomeApplicationFormService
} from './work-from-home-application-form.service';
import {IWorkFromHomeApplication} from '../work-from-home-application.model';
import {WorkFromHomeApplicationService} from '../service/work-from-home-application.service';
import {IUser} from 'app/entities/user/user.model';
import {UserService} from 'app/entities/user/user.service';
import {IEmployee} from 'app/entities/employee/employee.model';
import {EmployeeService} from 'app/entities/employee/service/employee.service';
import {Status} from 'app/entities/enumerations/status.model';
import {EmployeeMinimalListType} from "../../../shared/model/enumerations/employee-minimal-list-type.model";
import {
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_REJECTED,
  SWAL_REJECTED_ICON
} from "../../../shared/swal-common/swal.properties.constant";
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-work-from-home-application-update',
  templateUrl: './work-from-home-application-update.component.html',
})
export class WorkFromHomeApplicationUpdateComponent implements OnInit {
  isSaving = false;
  workFromHomeApplication: IWorkFromHomeApplication | null = null;
  statusValues = Object.keys(Status);

  usersSharedCollection: IUser[] = [];
  employeesSharedCollection: IEmployee[] = [];

  users: IUser[] = [];
  employees: IEmployee[] = [];
  startDateDp: any;
  endDateDp: any;
  minDate: any;
  selectedEmployeeId?: number;
  employeeSelectListType = EmployeeMinimalListType.ACTIVE;
  isSubmitted!: Boolean;
  startTime!: dayjs.Dayjs;
  endTime!: dayjs.Dayjs;
  isInvalid = false;

  editForm: WorkFromHomeApplicationFormGroup = this.workFromHomeApplicationFormService.createWorkFromHomeApplicationFormGroup();

  constructor(
    protected workFromHomeApplicationService: WorkFromHomeApplicationService,
    protected workFromHomeApplicationFormService: WorkFromHomeApplicationFormService,
    protected userService: UserService,
    protected employeeService: EmployeeService,
    protected activatedRoute: ActivatedRoute
  ) {
    const current = new Date();
    this.minDate = {
      year: current.getFullYear(),
      month: current.getMonth() + 1,
      day: current.getDate(),
    };
  }

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workFromHomeApplication }) => {
      this.workFromHomeApplication = workFromHomeApplication;

      // if (!workFromHomeApplication.id) {
      //   const today = dayjs().startOf('day');
      //   workFromHomeApplication.appliedAt = today;
      //   workFromHomeApplication.createdAt = today;
      // }

      if (workFromHomeApplication) {
        this.updateForm(workFromHomeApplication);
        this.selectedEmployeeId = workFromHomeApplication.employeeId!;
        this.editForm.get(['employeeId'])!.setValue(this.selectedEmployeeId);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const workFromHomeApplication = this.workFromHomeApplicationFormService.getWorkFromHomeApplication(this.editForm);
    if (workFromHomeApplication.id !== null) {
      this.subscribeToSaveResponseUpdated(this.workFromHomeApplicationService.update(workFromHomeApplication));
    } else {
      workFromHomeApplication.status=Status.PENDING;
      this.subscribeToSaveResponse(this.workFromHomeApplicationService.create(workFromHomeApplication as any));
    }
  }

  protected subscribeToSaveResponseUpdated(result: Observable<HttpResponse<IWorkFromHomeApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccessUpdated(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccessUpdated(): void {
    if (this.isSaving) {
      Swal.fire({
        icon: SWAL_APPROVED_ICON,
        text: 'Updated',
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: SWAL_REJECTED,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    }
    this.isSaving = false;
    this.editForm.reset();
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IWorkFromHomeApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    if (this.isSaving) {
      Swal.fire({
        icon: SWAL_APPROVED_ICON,
        text: 'Applied and Approved',
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    } else {
      Swal.fire({
        icon: SWAL_REJECTED_ICON,
        text: SWAL_REJECTED,
        timer: SWAL_APPROVE_REJECT_TIMER,
        showConfirmButton: false,
      });
    }
    this.isSaving = false;
    this.editForm.reset();
    this.selectedEmployeeId = undefined;
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  protected updateForm(workFromHomeApplication: IWorkFromHomeApplication): void {
    this.workFromHomeApplication = workFromHomeApplication;
    this.workFromHomeApplicationFormService.resetForm(this.editForm, workFromHomeApplication);

    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   workFromHomeApplication.appliedBy,
    //   workFromHomeApplication.createdBy,
    //   workFromHomeApplication.updatedBy,
    //   workFromHomeApplication.sanctionedBy
    // );
    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   workFromHomeApplication.employee
    // );
  }

  protected loadRelationshipsOptions(): void {



    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(
    //         users,
    //         this.workFromHomeApplication?.appliedBy,
    //         this.workFromHomeApplication?.createdBy,
    //         this.workFromHomeApplication?.updatedBy,
    //         this.workFromHomeApplication?.sanctionedBy
    //       )
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.workFromHomeApplication?.employee)
    //     )
    //   )
    //   .subscribe((employees: IEmployee[]) => (this.employeesSharedCollection = employees));
  }

  isApplied(): void {
    const bookingStartDate = this.editForm.get(['startDate'])!.value;
    const bookingEndDate = this.editForm.get(['endDate'])!.value;
    const employeeId = this.selectedEmployeeId;
    if (
      bookingStartDate !== undefined &&
      bookingEndDate !== undefined &&
      bookingStartDate !== null &&
      bookingEndDate !== null &&
      employeeId !== undefined &&
      employeeId !== null
    ) {
      const workFromHomeApplication = this.workFromHomeApplicationFormService.getWorkFromHomeApplication(this.editForm);
      this.workFromHomeApplicationService.isAppliedForWorkFromHome(workFromHomeApplication).subscribe(res => {
        this.isSubmitted = res.body!;
      });
    }
  }

  changeEmployee(employee: any): void {
    if (employee) {
      this.selectedEmployeeId = employee.id;
      this.editForm.get(['employeeId'])!.setValue(this.selectedEmployeeId);
      this.isApplied();
    } else {
      this.editForm.controls.employeeId.reset();
    }
  }

  checkDateValidation(event: any): void {
    this.isSubmitted = false;
    const bookingStartDate = this.editForm.get(['startDate'])!.value;
    const bookingEndDate = this.editForm.get(['endDate'])!.value;

    if (bookingStartDate && bookingEndDate && bookingStartDate > bookingEndDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
    }
    if (bookingStartDate && bookingEndDate && bookingStartDate > bookingEndDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
      this.isApplied();
    }
  }
}
