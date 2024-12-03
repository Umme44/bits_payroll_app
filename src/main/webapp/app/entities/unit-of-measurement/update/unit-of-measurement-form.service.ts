import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IUnitOfMeasurement, NewUnitOfMeasurement } from '../unit-of-measurement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IUnitOfMeasurement for edit and NewUnitOfMeasurementFormGroupInput for create.
 */
type UnitOfMeasurementFormGroupInput = IUnitOfMeasurement | PartialWithRequiredKeyOf<NewUnitOfMeasurement>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IUnitOfMeasurement | NewUnitOfMeasurement> = Omit<T, 'createdAt' | 'updatedAt'> & {
  createdAt?: string | null;
  updatedAt?: string | null;
};

type UnitOfMeasurementFormRawValue = FormValueOf<IUnitOfMeasurement>;

type NewUnitOfMeasurementFormRawValue = FormValueOf<NewUnitOfMeasurement>;

type UnitOfMeasurementFormDefaults = Pick<NewUnitOfMeasurement, 'id' | 'createdAt' | 'updatedAt'>;

type UnitOfMeasurementFormGroupContent = {
  id: FormControl<UnitOfMeasurementFormRawValue['id'] | NewUnitOfMeasurement['id']>;
  name: FormControl<UnitOfMeasurementFormRawValue['name']>;
  remarks: FormControl<UnitOfMeasurementFormRawValue['remarks']>;
  createdAt: FormControl<UnitOfMeasurementFormRawValue['createdAt']>;
  updatedAt: FormControl<UnitOfMeasurementFormRawValue['updatedAt']>;
  createdById: FormControl<UnitOfMeasurementFormRawValue['createdById']>;
  updatedById: FormControl<UnitOfMeasurementFormRawValue['updatedById']>;
};

export type UnitOfMeasurementFormGroup = FormGroup<UnitOfMeasurementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class UnitOfMeasurementFormService {
  createUnitOfMeasurementFormGroup(unitOfMeasurement: UnitOfMeasurementFormGroupInput = { id: null }): UnitOfMeasurementFormGroup {
    const unitOfMeasurementRawValue = this.convertUnitOfMeasurementToUnitOfMeasurementRawValue({
      ...this.getFormDefaults(),
      ...unitOfMeasurement,
    });
    return new FormGroup<UnitOfMeasurementFormGroupContent>({
      id: new FormControl(
        { value: unitOfMeasurementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(unitOfMeasurementRawValue.name, {
        validators: [Validators.required, Validators.maxLength(255)],
      }),
      remarks: new FormControl(unitOfMeasurementRawValue.remarks, {
        validators: [Validators.maxLength(255)],
      }),
      createdAt: new FormControl(unitOfMeasurementRawValue.createdAt, {
        validators: [Validators.required],
      }),
      updatedAt: new FormControl(unitOfMeasurementRawValue.updatedAt),
      createdById: new FormControl(unitOfMeasurementRawValue.createdById),
      updatedById: new FormControl(unitOfMeasurementRawValue.updatedById),
    });
  }

  getUnitOfMeasurement(form: UnitOfMeasurementFormGroup): IUnitOfMeasurement | NewUnitOfMeasurement {
    return this.convertUnitOfMeasurementRawValueToUnitOfMeasurement(
      form.getRawValue() as UnitOfMeasurementFormRawValue | NewUnitOfMeasurementFormRawValue
    );
  }

  resetForm(form: UnitOfMeasurementFormGroup, unitOfMeasurement: UnitOfMeasurementFormGroupInput): void {
    const unitOfMeasurementRawValue = this.convertUnitOfMeasurementToUnitOfMeasurementRawValue({
      ...this.getFormDefaults(),
      ...unitOfMeasurement,
    });
    form.reset(
      {
        ...unitOfMeasurementRawValue,
        id: { value: unitOfMeasurementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): UnitOfMeasurementFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdAt: currentTime,
      updatedAt: currentTime,
    };
  }

  private convertUnitOfMeasurementRawValueToUnitOfMeasurement(
    rawUnitOfMeasurement: UnitOfMeasurementFormRawValue | NewUnitOfMeasurementFormRawValue
  ): IUnitOfMeasurement | NewUnitOfMeasurement {
    return {
      ...rawUnitOfMeasurement,
      createdAt: dayjs(rawUnitOfMeasurement.createdAt, DATE_TIME_FORMAT),
      updatedAt: dayjs(rawUnitOfMeasurement.updatedAt, DATE_TIME_FORMAT),
    };
  }

  private convertUnitOfMeasurementToUnitOfMeasurementRawValue(
    unitOfMeasurement: IUnitOfMeasurement | (Partial<NewUnitOfMeasurement> & UnitOfMeasurementFormDefaults)
  ): UnitOfMeasurementFormRawValue | PartialWithRequiredKeyOf<NewUnitOfMeasurementFormRawValue> {
    return {
      ...unitOfMeasurement,
      createdAt: unitOfMeasurement.createdAt ? unitOfMeasurement.createdAt.format(DATE_TIME_FORMAT) : undefined,
      updatedAt: unitOfMeasurement.updatedAt ? unitOfMeasurement.updatedAt.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
