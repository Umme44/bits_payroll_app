import {Component, OnInit} from '@angular/core';
import {HttpResponse} from '@angular/common/http';
import {ActivatedRoute} from '@angular/router';
import {Observable} from 'rxjs';
import dayjs from 'dayjs/esm';
import {
  FlexScheduleApplicationFormGroup,
  FlexScheduleApplicationFormService
} from './flex-schedule-application-form.service';
import {IFlexScheduleApplication} from '../flex-schedule-application.model';
import {FlexScheduleApplicationService} from '../service/flex-schedule-application.service';
import {AlertError} from 'app/shared/alert/alert-error.model';
import {EventManager, EventWithContent} from 'app/core/util/event-manager.service';
import {DataUtils, FileLoadError} from 'app/core/util/data-util.service';
import {IUser} from 'app/entities/user/user.model';
import {UserService} from 'app/entities/user/user.service';
import {ITimeSlot} from 'app/entities/time-slot/time-slot.model';
import {TimeSlotService} from 'app/entities/time-slot/service/time-slot.service';
import {Status} from 'app/entities/enumerations/status.model';
import {NgbDateStruct} from '@ng-bootstrap/ng-bootstrap';
import {EmployeeService} from '../../../shared/legacy/legacy-service/employee.service';
import {IEmployee} from '../../../shared/legacy/legacy-model/employee.model';
import {DATE_FORMAT} from '../../../config/input.constants';
import {calculateDurationOnDays} from '../../../shared/util/date-util';
import {
  swalOnRequestErrorWithBackEndErrorTitle,
  swalOnSavedSuccess,
  swalOnUpdatedSuccess
} from "../../../shared/swal-common/swal-common";
import Swal from 'sweetalert2';

@Component({
  selector: 'jhi-flex-schedule-application-update',
  templateUrl: './flex-schedule-application-update.component.html',
})
export class FlexScheduleApplicationUpdateComponent implements OnInit {
  isSaving = false;
  flexScheduleApplication: IFlexScheduleApplication | null = null;
  statusValues = Object.keys(Status);

  employees: IEmployee[] = [];
  users: IUser[] = [];
  timeslots: ITimeSlot[] = [];

  effectiveDateErrorMsg = '';
  effectiveFromMinDate: NgbDateStruct;
  effectiveToMinDate: NgbDateStruct;

  employeesSharedCollection: IEmployee[] = [];
  usersSharedCollection: IUser[] = [];
  timeSlotsSharedCollection: ITimeSlot[] = [];

  editForm: FlexScheduleApplicationFormGroup = this.flexScheduleApplicationFormService.createFlexScheduleApplicationFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected flexScheduleApplicationService: FlexScheduleApplicationService,
    protected flexScheduleApplicationFormService: FlexScheduleApplicationFormService,
    protected employeeService: EmployeeService,
    protected userService: UserService,
    protected timeSlotService: TimeSlotService,
    protected activatedRoute: ActivatedRoute
  ) {
    this.effectiveToMinDate = this.effectiveFromMinDate = {
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1,
      day: new Date().getDate(),
    };
  }

  // compareEmployee = (o1: IEmployee | null, o2: IEmployee | null): boolean => this.employeeService.compareEmployee(o1, o2);
  //
  // compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);
  //
  // compareTimeSlot = (o1: ITimeSlot | null, o2: ITimeSlot | null): boolean => this.timeSlotService.compareTimeSlot(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({flexScheduleApplication}) => {
      this.flexScheduleApplication = flexScheduleApplication;
      if (flexScheduleApplication) {
        this.updateForm(flexScheduleApplication);
      }

      this.loadRelationshipsOptions();
    });
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

  changeEmployee(employee: any): void {
    if (employee) {
      this.editForm.get('requesterId')!.setValue(employee.id);
    }
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('bitsHrPayrollApp.error', {
          ...err,
          key: 'error.file.' + err.key
        })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  trackById(index: number, item: any): any {
    return item.id;
  }

  save(): void {
    this.isSaving = true;
    const flexScheduleApplication = this.flexScheduleApplicationFormService.getFlexScheduleApplication(this.editForm);
    if (flexScheduleApplication.id !== null) {
      this.subscribeToSaveResponse(this.flexScheduleApplicationService.update(flexScheduleApplication));
    } else {
      this.subscribeToSaveResponse(this.flexScheduleApplicationService.create(flexScheduleApplication as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFlexScheduleApplication>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      err => this.onSaveError(err)
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    if (!this.editForm.get('id')!.value) swalOnSavedSuccess();
    else swalOnUpdatedSuccess();
    this.previousState();
  }

  protected onSaveError(err: any): void {
    this.showErrorForInvalidInput()
    //swalOnRequestErrorWithBackEndErrorTitle(err.error.title, 3000);
    this.isSaving = false;
  }

  showErrorForInvalidInput(){
    Swal.fire({
      icon: 'error',
      text: 'Invalid input',
      timer: 3500,
      showConfirmButton: false,
    });
  }

  protected updateForm(flexScheduleApplication: IFlexScheduleApplication): void {

    this.flexScheduleApplication = flexScheduleApplication;
    this.flexScheduleApplicationFormService.resetForm(this.editForm, flexScheduleApplication);

    // this.employeesSharedCollection = this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(
    //   this.employeesSharedCollection,
    //   flexScheduleApplication.requesterId
    // );
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   flexScheduleApplication.sanctionedById,
    //   flexScheduleApplication.appliedBy,
    //   flexScheduleApplication.createdBy,
    //   flexScheduleApplication.updatedBy
    // );
    // this.timeSlotsSharedCollection = this.timeSlotService.addTimeSlotToCollectionIfMissing<ITimeSlot>(
    //   this.timeSlotsSharedCollection,
    //   flexScheduleApplication.timeSlot
    // );
  }

  protected loadRelationshipsOptions(): void {
    this.employeeService.getAllMinimal().subscribe(res => (this.employees = res.body ?? []));

    this.timeSlotService.query().subscribe(res => (this.timeslots = res.body ?? []));
    // this.employeeService
    //   .query()
    //   .pipe(map((res: HttpResponse<IEmployee[]>) => res.body ?? []))
    //   .pipe(
    //     map((employees: IEmployee[]) =>
    //       this.employeeService.addEmployeeToCollectionIfMissing<IEmployee>(employees, this.flexScheduleApplication?.requester)
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
    //         this.flexScheduleApplication?.sanctionedBy,
    //         this.flexScheduleApplication?.appliedBy,
    //         this.flexScheduleApplication?.createdBy,
    //         this.flexScheduleApplication?.updatedBy
    //       )
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
    //
    // this.timeSlotService
    //   .query()
    //   .pipe(map((res: HttpResponse<ITimeSlot[]>) => res.body ?? []))
    //   .pipe(
    //     map((timeSlots: ITimeSlot[]) =>
    //       this.timeSlotService.addTimeSlotToCollectionIfMissing<ITimeSlot>(timeSlots, this.flexScheduleApplication?.timeSlot)
    //     )
    //   )
    //   .subscribe((timeSlots: ITimeSlot[]) => (this.timeSlotsSharedCollection = timeSlots));
  }

  protected readonly Date = Date;

  dayjsToNgbDateStruct(value: dayjs.Dayjs): NgbDateStruct {
    const ngbDate: NgbDateStruct = {
      year: value.year(),
      month: value.month() + 1, // NgbDateStruct month starts from 1 (January is 1)
      day: value.date(),
    };

    return ngbDate;
  }
}
