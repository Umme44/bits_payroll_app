import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import dayjs from 'dayjs/esm';

import { FlexScheduleApplicationUserService } from './flex-schedule-application-user.service';
import { Status } from '../../../shared/model/enumerations/status.model';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import {
  swalOnAppliedSuccess,
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnUpdatedSuccess,
} from '../../../shared/swal-common/swal-common';

import { calculateDurationOnDays } from '../../../shared/util/date-util';
import { IEmployee } from '../../../shared/legacy/legacy-model/employee.model';
import { IUser } from '../../../entities/user/user.model';
import { ITimeSlot } from '../../../entities/time-slot/time-slot.model';
import { EmployeeService } from '../../../shared/legacy/legacy-service/employee.service';
import { UserService } from '../../../entities/user/user.service';
import { TimeSlotService } from '../../../shared/legacy/legacy-service/time-slot.service';
import { FlexScheduleApplication, IFlexScheduleApplication } from '../../../shared/legacy/legacy-model/flex-schedule-application.model';
import { DATE_FORMAT, DATE_TIME_FORMAT } from '../../../config/input.constants';
import { CustomValidator } from '../../../validators/custom-validator';

type SelectableEntity = IEmployee | IUser | ITimeSlot;

@Component({
  selector: 'jhi-flex-schedule-application-update',
  templateUrl: './flex-schedule-application-update-user.component.html',
})
export class FlexScheduleApplicationUpdateUserComponent implements OnInit {
  isSaving = false;
  employees: IEmployee[] = [];
  timeslots: ITimeSlot[] = [];
  users: IUser[] = [];
  effectiveFromDp: any;
  effectiveToDp: any;

  editForm = this.fb.group({
    id: [],
    effectiveFrom: [null as any, [Validators.required]],
    effectiveTo: [null as any, [Validators.required]],
    reason: ['', [CustomValidator.naturalTextValidator(), Validators.maxLength(200)]],
    status: [Status.PENDING],
    sanctionedAt: [],
    appliedAt: [],
    createdAt: [],
    updatedAt: [],
    requesterId: [-1],
    sanctionedById: [],
    appliedById: [],
    createdById: [-1],
    updatedById: [],
    timeSlotId: [0, [Validators.required]],
    iAgreeChecked: [false, Validators.required],
  });

  effectiveFromMinDate: NgbDateStruct;
  effectiveToMinDate: NgbDateStruct;

  effectiveDateErrorMsg = '';

  constructor(
    protected flexScheduleApplicationService: FlexScheduleApplicationUserService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    protected timeSlotService: TimeSlotService,
    private fb: FormBuilder
  ) {
    this.effectiveToMinDate = this.effectiveFromMinDate = {
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1,
      day: new Date().getDate(),
    };
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ flexScheduleApplication }) => {
      if (!flexScheduleApplication.id) {
        const today = dayjs().startOf('day');
        flexScheduleApplication.sanctionedAt = today;
        flexScheduleApplication.appliedAt = today;
        flexScheduleApplication.createdAt = today;
        flexScheduleApplication.updatedAt = today;
        flexScheduleApplication.createdById = -1;
        flexScheduleApplication.requesterId = -1;
        flexScheduleApplication.status = 'PENDING';
      } else {
        this.editForm.get('iAgreeChecked')!.setValue(true);
      }

      this.updateForm(flexScheduleApplication);

      this.employeeService.query().subscribe((res: HttpResponse<IEmployee[]>) => (this.employees = res.body || []));

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.timeSlotService.getAllTimeSlots().subscribe((res: HttpResponse<ITimeSlot[]>) => (this.timeslots = res.body || []));
    });
  }

  updateForm(flexScheduleApplication: IFlexScheduleApplication): void {
    this.editForm.patchValue({
      id: flexScheduleApplication.id,
      effectiveFrom: flexScheduleApplication.effectiveFrom,
      effectiveTo: flexScheduleApplication.effectiveTo,
      reason: flexScheduleApplication.reason,
      status: flexScheduleApplication.status,
      sanctionedAt: flexScheduleApplication.sanctionedAt ? flexScheduleApplication.sanctionedAt.format(DATE_TIME_FORMAT) : null,
      appliedAt: flexScheduleApplication.appliedAt ? flexScheduleApplication.appliedAt.format(DATE_TIME_FORMAT) : null,
      createdAt: flexScheduleApplication.createdAt ? flexScheduleApplication.createdAt.format(DATE_TIME_FORMAT) : null,
      updatedAt: flexScheduleApplication.updatedAt ? flexScheduleApplication.updatedAt.format(DATE_TIME_FORMAT) : null,
      requesterId: flexScheduleApplication.requesterId,
      sanctionedById: flexScheduleApplication.sanctionedById,
      appliedById: flexScheduleApplication.appliedById,
      createdById: flexScheduleApplication.createdById,
      updatedById: flexScheduleApplication.updatedById,
      timeSlotId: flexScheduleApplication.timeSlotId,
    });
  }

  // byteSize(base64String: string): string {
  //   return this.dataUtils.byteSize(base64String);
  // }
  //
  // openFile(contentType: string, base64String: string): void {
  //   this.dataUtils.openFile(contentType, base64String);
  // }

  // setFileData(event: any, field: string, isImage: boolean): void {
  //   this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe(null, (err: JhiFileLoadError) => {
  //     this.eventManager.broadcast(
  //       new JhiEventWithContent<AlertError>('bitsHrPayrollApp.error', { ...err, key: 'error.file.' + err.key })
  //     );
  //   });
  // }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const flexScheduleApplication = this.createFromForm();
    if (flexScheduleApplication.id !== undefined) {
      this.subscribeToSaveResponse(this.flexScheduleApplicationService.update(flexScheduleApplication));
    } else {
      this.subscribeToSaveResponse(this.flexScheduleApplicationService.create(flexScheduleApplication));
    }
  }

  private createFromForm(): IFlexScheduleApplication {
    return {
      ...new FlexScheduleApplication(),
      id: this.editForm.get(['id'])!.value,
      effectiveFrom: this.editForm.get(['effectiveFrom'])!.value,
      effectiveTo: this.editForm.get(['effectiveTo'])!.value,
      reason: this.editForm.get(['reason'])!.value,
      status: this.editForm.get(['status'])!.value,
      sanctionedAt: this.editForm.get(['sanctionedAt'])!.value
        ? dayjs(this.editForm.get(['sanctionedAt'])!.value, DATE_TIME_FORMAT)
        : undefined,
      appliedAt: this.editForm.get(['appliedAt'])!.value ? dayjs(this.editForm.get(['appliedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      createdAt: this.editForm.get(['createdAt'])!.value ? dayjs(this.editForm.get(['createdAt'])!.value, DATE_TIME_FORMAT) : undefined,
      updatedAt: this.editForm.get(['updatedAt'])!.value ? dayjs(this.editForm.get(['updatedAt'])!.value, DATE_TIME_FORMAT) : undefined,
      requesterId: this.editForm.get(['requesterId'])!.value,
      sanctionedById: this.editForm.get(['sanctionedById'])!.value,
      appliedById: this.editForm.get(['appliedById'])!.value,
      createdById: this.editForm.get(['createdById'])!.value,
      updatedById: this.editForm.get(['updatedById'])!.value,
      timeSlotId: this.editForm.get(['timeSlotId'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlexScheduleApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    if (this.editForm.get('id')!.value) swalOnUpdatedSuccess();
    else swalOnAppliedSuccess();
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(err: any): void {
    swalOnRequestErrorWithBackEndErrorTitle(err.error.title, 5000);
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }

  checkEffectiveDate(): void {
    const effectiveFrom = this.editForm.get('effectiveFrom')!.value;
    let effectiveTo = this.editForm.get('effectiveTo')!.value;
    this.effectiveDateErrorMsg = '';

    //auto initialize effectiveTo date
    if (effectiveFrom && !effectiveTo) {
      effectiveTo = dayjs(effectiveFrom, DATE_FORMAT).add(29, 'days');
      this.editForm.get('effectiveTo')!.setValue(effectiveTo);
    }

    if (effectiveFrom && effectiveTo) {
      if (dayjs(effectiveTo).isBefore(effectiveFrom)) {
        this.effectiveDateErrorMsg = 'End date must be greater than start date';
      } else if (calculateDurationOnDays(effectiveFrom, effectiveTo, true) < 30) {
        this.effectiveDateErrorMsg = 'Flex schedule minimum duration is 30 days';
      }
    }
  }

  changePolicyChecked(event: any): void {
    const isChecked = event.target.checked;
    if (!isChecked) {
      this.editForm.get('iAgreeChecked')!.setValue(null);
    }
  }
}
