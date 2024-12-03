import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IRoomType, NewRoomType } from '../room-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IRoomType for edit and NewRoomTypeFormGroupInput for create.
 */
type RoomTypeFormGroupInput = IRoomType | PartialWithRequiredKeyOf<NewRoomType>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IRoomType | NewRoomType> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type RoomTypeFormRawValue = FormValueOf<IRoomType>;

type NewRoomTypeFormRawValue = FormValueOf<NewRoomType>;

type RoomTypeFormDefaults = Pick<NewRoomType, 'id' | 'createdAt' | 'updatedAt'>;

type RoomTypeFormGroupContent = {
  id: FormControl<RoomTypeFormRawValue['id'] | NewRoomType['id']>;
  typeName: FormControl<RoomTypeFormRawValue['typeName']>;
  remarks: FormControl<RoomTypeFormRawValue['remarks']>;
  createdAt: FormControl<RoomTypeFormRawValue['createdAt']>;
  updatedAt: FormControl<RoomTypeFormRawValue['updatedAt']>;
  createdBy: FormControl<RoomTypeFormRawValue['createdBy']>;
  updatedBy: FormControl<RoomTypeFormRawValue['updatedBy']>;
};

export type RoomTypeFormGroup = FormGroup<RoomTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class RoomTypeFormService {
  createRoomTypeFormGroup(roomType: RoomTypeFormGroupInput = { id: null }): RoomTypeFormGroup {
    const roomTypeRawValue = this.convertRoomTypeToRoomTypeRawValue({
      ...this.getFormDefaults(),
      ...roomType,
    });
    return new FormGroup<RoomTypeFormGroupContent>({
      id: new FormControl(
        { value: roomTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      typeName: new FormControl(roomTypeRawValue.typeName, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(250)],
      }),
      remarks: new FormControl(roomTypeRawValue.remarks, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      createdAt: new FormControl(roomTypeRawValue.createdAt),
      updatedAt: new FormControl(roomTypeRawValue.updatedAt),
      createdBy: new FormControl(roomTypeRawValue.createdBy),
      updatedBy: new FormControl(roomTypeRawValue.updatedBy),
    });
  }

  getRoomType(form: RoomTypeFormGroup): IRoomType | NewRoomType {
    return this.convertRoomTypeRawValueToRoomType(form.getRawValue() as RoomTypeFormRawValue | NewRoomTypeFormRawValue);
  }

  resetForm(form: RoomTypeFormGroup, roomType: RoomTypeFormGroupInput): void {
    const roomTypeRawValue = this.convertRoomTypeToRoomTypeRawValue({ ...this.getFormDefaults(), ...roomType });
    form.reset(
      {
        ...roomTypeRawValue,
        id: { value: roomTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): RoomTypeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertRoomTypeRawValueToRoomType(rawRoomType: RoomTypeFormRawValue | NewRoomTypeFormRawValue): IRoomType | NewRoomType {
    return {
      ...rawRoomType,
      createdAt: dayjs(rawRoomType.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawRoomType.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertRoomTypeToRoomTypeRawValue(
    roomType: IRoomType | (Partial<NewRoomType> & RoomTypeFormDefaults)
  ): RoomTypeFormRawValue | PartialWithRequiredKeyOf<NewRoomTypeFormRawValue> {
    return {
      ...roomType,
      createdAt: roomType.createdAt ? roomType.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: roomType.updatedAt ? roomType.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
