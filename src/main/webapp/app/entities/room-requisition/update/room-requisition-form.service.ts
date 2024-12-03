import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRoomRequisition, NewRoomRequisition } from '../room-requisition.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoomRequisition for edit and NewRoomRequisitionFormGroupInput for create.
 */
type RoomRequisitionFormGroupInput = IRoomRequisition | PartialWithRequiredKeyOf<NewRoomRequisition>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRoomRequisition | NewRoomRequisition> = Omit<T, 'createdAt' | 'updatedAt' | 'sanctionedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
  sanctionedAt?: string | null;
};

type RoomRequisitionFormRawValue = FormValueOf<IRoomRequisition>;

type NewRoomRequisitionFormRawValue = FormValueOf<NewRoomRequisition>;

type RoomRequisitionFormDefaults = Pick<NewRoomRequisition, 'id' | 'createdAt' | 'updatedAt' | 'sanctionedAt' | 'isFullDay'>;

type RoomRequisitionFormGroupContent = {
  id: FormControl<RoomRequisitionFormRawValue['id'] | NewRoomRequisition['id']>;
  status: FormControl<RoomRequisitionFormRawValue['status']>;
  bookingTrn: FormControl<RoomRequisitionFormRawValue['bookingTrn']>;
  createdAt: FormControl<RoomRequisitionFormRawValue['createdAt']>;
  updatedAt: FormControl<RoomRequisitionFormRawValue['updatedAt']>;
  sanctionedAt: FormControl<RoomRequisitionFormRawValue['sanctionedAt']>;
  participantList: FormControl<RoomRequisitionFormRawValue['participantList']>;
  rejectedReason: FormControl<RoomRequisitionFormRawValue['rejectedReason']>;
  bookingStartDate: FormControl<RoomRequisitionFormRawValue['bookingStartDate']>;
  bookingEndDate: FormControl<RoomRequisitionFormRawValue['bookingEndDate']>;
  startTime: FormControl<RoomRequisitionFormRawValue['startTime']>;
  endTime: FormControl<RoomRequisitionFormRawValue['endTime']>;
  title: FormControl<RoomRequisitionFormRawValue['title']>;
  agenda: FormControl<RoomRequisitionFormRawValue['agenda']>;
  optionalParticipantList: FormControl<RoomRequisitionFormRawValue['optionalParticipantList']>;
  isFullDay: FormControl<RoomRequisitionFormRawValue['isFullDay']>;
  createdBy: FormControl<RoomRequisitionFormRawValue['createdBy']>;
  updatedBy: FormControl<RoomRequisitionFormRawValue['updatedBy']>;
  sanctionedBy: FormControl<RoomRequisitionFormRawValue['sanctionedBy']>;
  requester: FormControl<RoomRequisitionFormRawValue['requester']>;
  room: FormControl<RoomRequisitionFormRawValue['room']>;
};

export type RoomRequisitionFormGroup = FormGroup<RoomRequisitionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoomRequisitionFormService {
  createRoomRequisitionFormGroup(roomRequisition: RoomRequisitionFormGroupInput = { id: null }): RoomRequisitionFormGroup {
    const roomRequisitionRawValue = this.convertRoomRequisitionToRoomRequisitionRawValue({
      ...this.getFormDefaults(),
      ...roomRequisition,
    });
    return new FormGroup<RoomRequisitionFormGroupContent>({
      id: new FormControl(
        { value: roomRequisitionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      status: new FormControl(roomRequisitionRawValue.status, {
        validators: [Validators.required],
      }),
      bookingTrn: new FormControl(roomRequisitionRawValue.bookingTrn, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      createdAt: new FormControl(roomRequisitionRawValue.createdAt),
      updatedAt: new FormControl(roomRequisitionRawValue.updatedAt),
      sanctionedAt: new FormControl(roomRequisitionRawValue.sanctionedAt),
      participantList: new FormControl(roomRequisitionRawValue.participantList, {
        validators: [Validators.minLength(0), Validators.maxLength(500)],
      }),
      rejectedReason: new FormControl(roomRequisitionRawValue.rejectedReason, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      bookingStartDate: new FormControl(roomRequisitionRawValue.bookingStartDate, {
        validators: [Validators.required],
      }),
      bookingEndDate: new FormControl(roomRequisitionRawValue.bookingEndDate, {
        validators: [Validators.required],
      }),
      startTime: new FormControl(roomRequisitionRawValue.startTime, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      endTime: new FormControl(roomRequisitionRawValue.endTime, {
        validators: [Validators.required, Validators.min(0), Validators.max(100)],
      }),
      title: new FormControl(roomRequisitionRawValue.title, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(250)],
      }),
      agenda: new FormControl(roomRequisitionRawValue.agenda, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      optionalParticipantList: new FormControl(roomRequisitionRawValue.optionalParticipantList),
      isFullDay: new FormControl(roomRequisitionRawValue.isFullDay),
      createdBy: new FormControl(roomRequisitionRawValue.createdBy),
      updatedBy: new FormControl(roomRequisitionRawValue.updatedBy),
      sanctionedBy: new FormControl(roomRequisitionRawValue.sanctionedBy),
      requester: new FormControl(roomRequisitionRawValue.requester),
      room: new FormControl(roomRequisitionRawValue.room),
    });
  }

  getRoomRequisition(form: RoomRequisitionFormGroup): IRoomRequisition | NewRoomRequisition {
    return this.convertRoomRequisitionRawValueToRoomRequisition(
      form.getRawValue() as RoomRequisitionFormRawValue | NewRoomRequisitionFormRawValue
    );
  }

  resetForm(form: RoomRequisitionFormGroup, roomRequisition: RoomRequisitionFormGroupInput): void {
    const roomRequisitionRawValue = this.convertRoomRequisitionToRoomRequisitionRawValue({ ...this.getFormDefaults(), ...roomRequisition });
    form.reset(
      {
        ...roomRequisitionRawValue,
        id: { value: roomRequisitionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RoomRequisitionFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
      sanctionedAt: currentTime,
      isFullDay: false,
    };
  }

  private convertRoomRequisitionRawValueToRoomRequisition(
    rawRoomRequisition: RoomRequisitionFormRawValue | NewRoomRequisitionFormRawValue
  ): IRoomRequisition | NewRoomRequisition {
    return {
      ...rawRoomRequisition,
      createdAt: dayjs(rawRoomRequisition.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawRoomRequisition.updatedAt, DATE_TIME_FORMAT),
      sanctionedAt: dayjs(rawRoomRequisition.sanctionedAt, DATE_TIME_FORMAT),
    };
  }

  private convertRoomRequisitionToRoomRequisitionRawValue(
    roomRequisition: IRoomRequisition | (Partial<NewRoomRequisition> & RoomRequisitionFormDefaults)
  ): RoomRequisitionFormRawValue | PartialWithRequiredKeyOf<NewRoomRequisitionFormRawValue> {
    return {
      ...roomRequisition,
      createdAt: roomRequisition.createdAt ? roomRequisition.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: roomRequisition.updatedAt ? roomRequisition.updatedAt.format(DATE_TIME_FORMAT) : undefined,
      sanctionedAt: roomRequisition.sanctionedAt ? roomRequisition.sanctionedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
