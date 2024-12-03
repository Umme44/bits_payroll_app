import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IFloor, NewFloor } from '../floor.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IFloor for edit and NewFloorFormGroupInput for create.
 */
type FloorFormGroupInput = IFloor | PartialWithRequiredKeyOf<NewFloor>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IFloor | NewFloor> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type FloorFormRawValue = FormValueOf<IFloor>;

type NewFloorFormRawValue = FormValueOf<NewFloor>;

type FloorFormDefaults = Pick<NewFloor, 'id' | 'createdAt' | 'updatedAt'>;

type FloorFormGroupContent = {
  id: FormControl<FloorFormRawValue['id'] | NewFloor['id']>;
  floorName: FormControl<FloorFormRawValue['floorName']>;
  remarks: FormControl<FloorFormRawValue['remarks']>;
  createdAt: FormControl<FloorFormRawValue['createdAt']>;
  updatedAt: FormControl<FloorFormRawValue['updatedAt']>;
  createdBy: FormControl<FloorFormRawValue['createdBy']>;
  updatedBy: FormControl<FloorFormRawValue['updatedBy']>;
  building: FormControl<FloorFormRawValue['building']>;
};

export type FloorFormGroup = FormGroup<FloorFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class FloorFormService {
  createFloorFormGroup(floor: FloorFormGroupInput = { id: null }): FloorFormGroup {
    const floorRawValue = this.convertFloorToFloorRawValue({
      ...this.getFormDefaults(),
      ...floor,
    });
    return new FormGroup<FloorFormGroupContent>({
      id: new FormControl(
        { value: floorRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      floorName: new FormControl(floorRawValue.floorName, {
        validators: [Validators.required, Validators.minLength(0), Validators.maxLength(250)],
      }),
      remarks: new FormControl(floorRawValue.remarks, {
        validators: [Validators.minLength(0), Validators.maxLength(250)],
      }),
      createdAt: new FormControl(floorRawValue.createdAt),
      updatedAt: new FormControl(floorRawValue.updatedAt),
      createdBy: new FormControl(floorRawValue.createdBy),
      updatedBy: new FormControl(floorRawValue.updatedBy),
      building: new FormControl(floorRawValue.building),
    });
  }

  getFloor(form: FloorFormGroup): IFloor | NewFloor {
    return this.convertFloorRawValueToFloor(form.getRawValue() as FloorFormRawValue | NewFloorFormRawValue);
  }

  resetForm(form: FloorFormGroup, floor: FloorFormGroupInput): void {
    const floorRawValue = this.convertFloorToFloorRawValue({ ...this.getFormDefaults(), ...floor });
    form.reset(
      {
        ...floorRawValue,
        id: { value: floorRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): FloorFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertFloorRawValueToFloor(rawFloor: FloorFormRawValue | NewFloorFormRawValue): IFloor | NewFloor {
    return {
      ...rawFloor,
      createdAt: dayjs(rawFloor.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawFloor.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertFloorToFloorRawValue(
    floor: IFloor | (Partial<NewFloor> & FloorFormDefaults)
  ): FloorFormRawValue | PartialWithRequiredKeyOf<NewFloorFormRawValue> {
    return {
      ...floor,
      createdAt: floor.createdAt ? floor.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: floor.updatedAt ? floor.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
