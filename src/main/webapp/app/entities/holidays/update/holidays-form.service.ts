import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IHolidays, NewHolidays } from '../holidays.model';
import {CustomValidator} from "../../../validators/custom-validator";

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IHolidays for edit and NewHolidaysFormGroupInput for create.
 */
type HolidaysFormGroupInput = IHolidays | PartialWithRequiredKeyOf<NewHolidays>;

type HolidaysFormDefaults = Pick<NewHolidays, 'id' | 'isMoonDependent'>;

type HolidaysFormGroupContent = {
  id: FormControl<IHolidays['id'] | NewHolidays['id']>;
  holidayType: FormControl<IHolidays['holidayType']>;
  description: FormControl<IHolidays['description']>;
  startDate: FormControl<IHolidays['startDate']>;
  endDate: FormControl<IHolidays['endDate']>;
  isMoonDependent: FormControl<IHolidays['isMoonDependent']>;
};

export type HolidaysFormGroup = FormGroup<HolidaysFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class HolidaysFormService {
  createHolidaysFormGroup(holidays: HolidaysFormGroupInput = { id: null }): HolidaysFormGroup {
    const holidaysRawValue = {
      ...this.getFormDefaults(),
      ...holidays,
    };
    return new FormGroup<HolidaysFormGroupContent>({
      id: new FormControl(
        { value: holidaysRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      holidayType: new FormControl(holidaysRawValue.holidayType),
      description: new FormControl(holidaysRawValue.description,
        {
          validators: [Validators.maxLength(255), CustomValidator.naturalTextValidator()]
        }
      ),
      startDate: new FormControl(holidaysRawValue.startDate,
        {
          validators: [Validators.required]
        }),
      endDate: new FormControl(holidaysRawValue.endDate,{
        validators: [Validators.required]
      }),
      isMoonDependent: new FormControl(holidaysRawValue.isMoonDependent),
    });
  }

  getHolidays(form: HolidaysFormGroup): IHolidays | NewHolidays {
    return form.getRawValue() as IHolidays | NewHolidays;
  }

  resetForm(form: HolidaysFormGroup, holidays: HolidaysFormGroupInput): void {
    const holidaysRawValue = { ...this.getFormDefaults(), ...holidays };
    form.reset(
      {
        ...holidaysRawValue,
        id: { value: holidaysRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): HolidaysFormDefaults {
    return {
      id: null,
      isMoonDependent: false,
    };
  }
}
