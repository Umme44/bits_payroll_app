import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { SpecialShiftTimingFormService, SpecialShiftTimingFormGroup } from './special-shift-timing-form.service';
import { ISpecialShiftTiming } from '../special-shift-timing.model';
import { SpecialShiftTimingService } from '../service/special-shift-timing.service';
import { ITimeSlot } from 'app/entities/time-slot/time-slot.model';
import { TimeSlotService } from 'app/entities/time-slot/service/time-slot.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-special-shift-timing-update',
  templateUrl: './special-shift-timing-update.component.html',
})
export class SpecialShiftTimingUpdateComponent implements OnInit {
  isSaving = false;
  specialShiftTiming: ISpecialShiftTiming | null = null;

  // timeSlotsSharedCollection: ITimeSlot[] = [];
  // usersSharedCollection: IUser[] = [];


  users: IUser[] = [];
  timeslots: ITimeSlot[] = [];
  minDateForEndDateDP!: NgbDateStruct;

  editForm: SpecialShiftTimingFormGroup = this.specialShiftTimingFormService.createSpecialShiftTimingFormGroup();

  constructor(
    protected specialShiftTimingService: SpecialShiftTimingService,
    protected specialShiftTimingFormService: SpecialShiftTimingFormService,
    protected timeSlotService: TimeSlotService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTimeSlot = (o1: ITimeSlot | null, o2: ITimeSlot | null): boolean => this.timeSlotService.compareTimeSlot(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ specialShiftTiming }) => {
      this.specialShiftTiming = specialShiftTiming;
      if (specialShiftTiming) {
        this.updateForm(specialShiftTiming);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const specialShiftTiming = this.specialShiftTimingFormService.getSpecialShiftTiming(this.editForm);
    if (specialShiftTiming.id !== null) {
      this.subscribeToSaveResponse(this.specialShiftTimingService.update(specialShiftTiming));
    } else {
      this.subscribeToSaveResponse(this.specialShiftTimingService.create(specialShiftTiming as any));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpecialShiftTiming>>): void {
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

  protected updateForm(specialShiftTiming: ISpecialShiftTiming): void {
    this.specialShiftTiming = specialShiftTiming;
    this.specialShiftTimingFormService.resetForm(this.editForm, specialShiftTiming);

    // this.timeSlotsSharedCollection = this.timeSlotService.addTimeSlotToCollectionIfMissing<ITimeSlot>(
    //   this.timeSlotsSharedCollection,
    //   specialShiftTiming.timeSlot
    // );
    // this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(
    //   this.usersSharedCollection,
    //   specialShiftTiming.createdBy,
    //   specialShiftTiming.updatedBy
    // );
  }

  protected loadRelationshipsOptions(): void {

    this.timeSlotService.query().subscribe((res: HttpResponse<ITimeSlot[]>) => (this.timeslots = res.body || []));

    this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

    // this.timeSlotService
    //   .query()
    //   .pipe(map((res: HttpResponse<ITimeSlot[]>) => res.body ?? []))
    //   .pipe(
    //     map((timeSlots: ITimeSlot[]) =>
    //       this.timeSlotService.addTimeSlotToCollectionIfMissing<ITimeSlot>(timeSlots, this.specialShiftTiming?.timeSlot)
    //     )
    //   )
    //   .subscribe((timeSlots: ITimeSlot[]) => (this.timeSlotsSharedCollection = timeSlots));
    //
    // this.userService
    //   .query()
    //   .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
    //   .pipe(
    //     map((users: IUser[]) =>
    //       this.userService.addUserToCollectionIfMissing<IUser>(
    //         users,
    //         this.specialShiftTiming?.createdBy,
    //         this.specialShiftTiming?.updatedBy
    //       )
    //     )
    //   )
    //   .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }

  trackById(index: number, item: any): any {
    return item.id;
  }

  startDateSelect(): void {
    if (this.editForm.get('startDate')!.value) {
      const statDate = (this.editForm.get('startDate')!.value).toDate();
      this.minDateForEndDateDP = {
        year: statDate.getFullYear(),
        month: statDate.getMonth() + 1,
        day: statDate.getDate(),
      };
    }
  }
}
