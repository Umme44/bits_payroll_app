import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import dayjs from 'dayjs/esm';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import Swal from 'sweetalert2';
import { UserWorkFromHomeApplicationService } from '../service/user-work-from-home-application.service';
import {
  SWAL_APPLIED_TEXT,
  SWAL_APPROVE_REJECT_TIMER,
  SWAL_APPROVED_ICON,
  SWAL_REJECTED,
  SWAL_REJECTED_ICON,
} from '../../../../shared/swal-common/swal.properties.constant';
import { IUser } from '../../../../entities/user/user.model';
import { IEmployee } from '../../../../shared/legacy/legacy-model/employee.model';
import { UserService } from '../../../../entities/user/user.service';
import {
  UserWorkFromHomeApplicationFormGroup,
  UserWorkFromHomeApplicationFormService,
} from './user-work-from-home-application.form.service';
import { IUserWorkFromHomeApplication, UserWorkFromHomeApplication } from '../user-work-from-home-application.model';
import {swalPatternError} from "../../../../shared/swal-common/swal-common";

type SelectableEntity = IUser | IEmployee;

@Component({
  selector: 'jhi-user-work-from-home-application-update',
  templateUrl: './user-work-from-home-application-update.component.html',
})
export class UserWorkFromHomeApplicationUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  employees: IEmployee[] = [];
  startDateDp: any;
  endDateDp: any;
  minDate: any;
  selectedEmployeeId?: number;
  isSubmitted!: Boolean;
  startTime!: dayjs.Dayjs;
  endTime!: dayjs.Dayjs;
  isInvalid = false;
  isInvalidDuration = false;
  durationCal!: number;

  workFromHomeApplication: IUserWorkFromHomeApplication | null = null;

  editForm: UserWorkFromHomeApplicationFormGroup = this.userWorkFromHomeApplicationFormService.createUserWorkFromHomeApplicationFormGroup();

  constructor(
    protected userWorkFromHomeApplicationService: UserWorkFromHomeApplicationService,
    protected userWorkFromHomeApplicationFormService: UserWorkFromHomeApplicationFormService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {
    const current = new Date();
    this.minDate = {
      year: current.getFullYear(),
      month: current.getMonth() + 1,
      day: current.getDate(),
    };
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ workFromHomeApplication }) => {
      if (!workFromHomeApplication.id) {
        const today = dayjs();
        workFromHomeApplication.appliedAt = today;
        workFromHomeApplication.createdAt = today;
      }

      if (workFromHomeApplication.id) {
        this.updateForm(workFromHomeApplication);
        this.selectedEmployeeId = workFromHomeApplication.employeeId!;
      }
      this.updateForm(workFromHomeApplication);
    });
  }

  checkDaysDuration(event: any): void {
    this.startTime = this.editForm.get(['startDate'])!.value;
    this.endTime = this.editForm.get(['endDate'])!.value;

    this.durationCal = this.endTime.diff(this.startTime, 'days');

    if (this.durationCal >= 30) {
      this.isInvalidDuration = true;
    } else {
      this.isInvalidDuration = false;
    }
  }

  updateForm(workFromHomeApplication: IUserWorkFromHomeApplication): void {
    this.workFromHomeApplication = workFromHomeApplication;
    this.userWorkFromHomeApplicationFormService.resetForm(this.editForm, workFromHomeApplication);
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const userWorkFromHomeApplication = this.userWorkFromHomeApplicationFormService.getUserWorkFromHomeApplication(this.editForm);
    if (userWorkFromHomeApplication.id !== undefined && userWorkFromHomeApplication.id !== null) {
      this.subscribeToSaveResponse(this.userWorkFromHomeApplicationService.update(userWorkFromHomeApplication));
    } else {
      this.subscribeToSaveResponse(this.userWorkFromHomeApplicationService.create(userWorkFromHomeApplication));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IUserWorkFromHomeApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    if (this.isSaving) {
      Swal.fire({
        icon: SWAL_APPROVED_ICON,
        text: SWAL_APPLIED_TEXT,
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
    setTimeout(() => {
      this.previousState();
    }, 1000);
  }

  protected onSaveError(): void {
    this.isSaving = false;
    swalPatternError()
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  isApplied(event: any): void {
    const bookingStartDate = this.editForm.get(['startDate'])!.value;
    const bookingEndDate = this.editForm.get(['endDate'])!.value;

    if (bookingStartDate !== undefined && bookingEndDate !== undefined && bookingStartDate !== null && bookingEndDate !== null) {
      const workFromHomeApplication = this.userWorkFromHomeApplicationFormService.getUserWorkFromHomeApplication(this.editForm);
      this.userWorkFromHomeApplicationService.isAppliedForWorkFromHome(workFromHomeApplication).subscribe(res => {
        this.isSubmitted = res.body!;
        if (!this.isSubmitted) {
          this.checkDaysDuration(event);
        }
      });
    }
  }

  checkDateValidation(event: any): void {
    this.isInvalidDuration = false;
    this.isSubmitted = false;
    const bookingStartDate = this.editForm.get(['startDate'])!.value;
    const bookingEndDate = this.editForm.get(['endDate'])!.value;

    if (bookingStartDate && bookingEndDate && bookingStartDate > bookingEndDate) {
      this.isInvalid = true;
    } else {
      this.isInvalid = false;
      this.isApplied(event);
    }
  }
}
