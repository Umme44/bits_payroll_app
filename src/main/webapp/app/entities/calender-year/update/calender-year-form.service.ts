import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ICalenderYear, NewCalenderYear } from '../calender-year.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICalenderYear for edit and NewCalenderYearFormGroupInput for create.
 */
type CalenderYearFormGroupInput = ICalenderYear | PartialWithRequiredKeyOf<NewCalenderYear>;

type CalenderYearFormDefaults = Pick<NewCalenderYear, 'id'>;

type CalenderYearFormGroupContent = {
  id: FormControl<ICalenderYear['id'] | NewCalenderYear['id']>;
  year: FormControl<ICalenderYear['year']>;
  startDate: FormControl<ICalenderYear['startDate']>;
  endDate: FormControl<ICalenderYear['endDate']>;
};

export type CalenderYearFormGroup = FormGroup<CalenderYearFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CalenderYearFormService {
  createCalenderYearFormGroup(calenderYear: CalenderYearFormGroupInput = { id: null }): CalenderYearFormGroup {
    const calenderYearRawValue = {
      ...this.getFormDefaults(),
      ...calenderYear,
    };
    return new FormGroup<CalenderYearFormGroupContent>({
      id: new FormControl(
        { value: calenderYearRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      year: new FormControl(calenderYearRawValue.year, {
        validators: [Validators.required, Validators.min(1900), Validators.max(2100)],
      }),
      startDate: new FormControl(calenderYearRawValue.startDate, {
        validators: [Validators.required],
      }),
      endDate: new FormControl(calenderYearRawValue.endDate, {
        validators: [Validators.required],
      }),
    });
  }

  getCalenderYear(form: CalenderYearFormGroup): ICalenderYear | NewCalenderYear {
    return form.getRawValue() as ICalenderYear | NewCalenderYear;
  }

  resetForm(form: CalenderYearFormGroup, calenderYear: CalenderYearFormGroupInput): void {
    const calenderYearRawValue = { ...this.getFormDefaults(), ...calenderYear };
    form.reset(
      {
        ...calenderYearRawValue,
        id: { value: calenderYearRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): CalenderYearFormDefaults {
    return {
      id: null,
    };
  }
}
